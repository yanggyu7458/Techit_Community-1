package likelion15.mutsa.service;

import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.dto.NoticeDto;
import likelion15.mutsa.entity.*;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import likelion15.mutsa.entity.enums.YesOrNo;
import likelion15.mutsa.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;
    private final JoinService joinService;
    private final FileService fileService;
    private final FileConRepository fileConRepository;

    private final List<BoardDTO> boardList = new ArrayList<>();
    private static final int PAGE_SIZE = 5;
    private final LikesRepository likesRepository;

    public BoardDTO readBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            BoardDTO boardDTO = BoardDTO.fromEntity(board);

            return boardDTO;
        }
        return null;
    }


    public Page<BoardDTO> readBoardAllPaged(int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(BoardDTO::fromEntity);
    }

    public Board createBoard(BoardDTO boardDTO, MultipartFile file, User loginUser) throws Exception {

        Content content = Content.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .isDeleted(DeletedStatus.NOT_DELETED)
                .status(VisibleStatus.VISIBLE)
                .build();
        Board board = Board.builder()
                .user(loginUser)  // Set the user for the board
                .createdBy(loginUser.getName())
                .content(content)
                .build();

        if (!file.isEmpty()) { // 첨부 파일이 존재한다면
            File fileEntity = fileService.createFile(file); // 파일 업로드

            FileCon fileCon = FileCon.builder() //fileCon 엔티티 생성 fileid랑 공지 id 등록함으로써 연관 지음
                    .file(fileEntity)
                    .board(board)
                    .build();
            fileConRepository.save(fileCon); // 엔티티 저장

        }
        // 파일 정보 저장
//        if (file != null && !file.isEmpty()) {
//            String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//            UUID uuid = UUID.randomUUID();
//            String fileName = uuid + "_" + StringUtils.cleanPath(file.getOriginalFilename());
//
//            File fileEntity = File.builder()
//                    .path(projectPath)
//                    .name(fileName)
//                    .size(file.getSize())
//                    .isDeleted(DeletedStatus.NOT_DELETED)
//                    .build();
//
//            file.transferTo(new java.io.File(fileEntity.getPath() + "\\" + fileEntity.getName()));
//
//            // 파일 정보를 Board 엔티티에 저장
//            board.setFile(fileEntity);
//
//            // FileCon 엔티티 생성
//            FileCon fileCon = FileCon.builder()
//                    .file(fileEntity)
//                    .board(board)
//                    .build();
//            fileConRepository.save(fileCon);
//
//
//            // FileConDTO 객체 생성 및 board_id 설정
//            FileConDTO fileConDTO = new FileConDTO();
//            fileConDTO.setBoardIdFromEntity(board); // 저장된 board의 id 설정
//
//        }

        return boardRepository.save(board);
    }

    public Board updateBoard(Long id, BoardDTO boardDTO, User loginUser) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            if(board.getUser().getName().equals(loginUser.getName())) {
                Content content = board.getContent();
                content.setTitle(boardDTO.getTitle());
                content.setContent(boardDTO.getContent());
                board.setContent(content);
                return boardRepository.save(board);
            } else throw new IllegalArgumentException("수정 권한이 없습니다.");
        } throw new IllegalArgumentException("Board not found with id: " + id);
    }
    public void deleteBoard(Long boardId, User loginUser) {
        Optional<Board> optionalBoard = boardRepository.findById(boardId);
        if (optionalBoard.isPresent()){
            Board board = optionalBoard.get();
            if(board.getUser().getName().equals(loginUser.getName())) {
                boardRepository.deleteById(boardId);
            } else throw new IllegalArgumentException("삭제 권한이 없습니다.");

        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
    public void increaseViewCount(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            board.setViewCount(board.getViewCount() + 1); // 조회수 증가
            boardRepository.save(board);
        }
    }

    //좋아요 기능
    public void likeBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found with id: " + boardId));

        Likes likes = Likes.builder()
                .isLike(YesOrNo.YES)
                .isDeleted(DeletedStatus.NOT_DELETED)
                .board(board)
                .build();

        board.addLikes(likes);
        boardRepository.save(board);
    }

    public void unlikeBoard(Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Board not found with id: " + boardId));

        Likes likes = board.getLikes().stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Likes not found for the board: " + boardId));

        board.removeLikes(likes);
        boardRepository.save(board);
    }
    public List<BoardDTO> searchBoards(String keyword, String searchOption) {
        Content content = Content.builder().build();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if ("title".equals(searchOption)) {
            content.setTitle(keyword);
            matcher.withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        } else if ("titleAndContent".equals(searchOption)) {
            content.setTitle(keyword);
            content.setContent(keyword);
            matcher.withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
            matcher.withMatcher("content", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        }

        Example<Board> example = Example.of(Board.builder().content(content).build(), matcher);

        List<BoardDTO> boardList = new ArrayList<>();
        for (Board board : boardRepository.findAll(example)) {
            boardList.add(BoardDTO.fromEntity(board));
        }
        return boardList;
    }

    public Page<BoardDTO> searchBoardsPaged(String keyword, String searchOption, Pageable pageable) {
        Content content = Content.builder().build();
        ExampleMatcher matcher = ExampleMatcher.matching();

        if ("title".equals(searchOption)) {
            return boardRepository.searchByTitleLike(keyword, pageable).map(BoardDTO::fromEntity);
        } else if ("titleAndContent".equals(searchOption)) {
            return boardRepository.searchByTitleOrContentLike(keyword, pageable).map(BoardDTO::fromEntity);
        }

        Example<Board> example = Example.of(Board.builder().content(content).build(), matcher);

        Page<Board> boardPage = boardRepository.findAll(example, pageable);
        return boardPage.map(BoardDTO::fromEntity);
    }

    // comment 좋아요 갯수 반환
    public int getCntCommentLikes(Long commentId) {
        return likesRepository.countLikesByComment_Id(commentId);
    }

    // board 좋아요 갯수 반환
    public int getCntBoardLikes(Long boardId) {
        return likesRepository.countLikesByBoard_Id(boardId);
    }
}
