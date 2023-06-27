package likelion15.mutsa.service;

import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Likes;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import likelion15.mutsa.entity.enums.YesOrNo;
import likelion15.mutsa.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final List<BoardDTO> boardList = new ArrayList<>();
    private static final int PAGE_SIZE = 5;

    public List<BoardDTO> readBoardAll() {
        List<BoardDTO> boardList = new ArrayList<>();
        for (Board board :
                boardRepository.findAll()) {
            boardList.add(BoardDTO.fromEntity(board));
        }
        return boardList;
    }
    public BoardDTO readBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            BoardDTO boardDTO = BoardDTO.fromEntity(board);
            boardDTO.increaseViewCount(); // 조회수 증가
            return boardDTO;
        }
        return null;
    }
    public Page<BoardDTO> readBoardAllPaged(int page) {
        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, Sort.by("id").descending());
        Page<Board> boardPage = boardRepository.findAll(pageable);
        return boardPage.map(BoardDTO::fromEntity);
    }

    public Board createBoard(BoardDTO boardDTO) {
        Content content = Content.builder()
                .title(boardDTO.getTitle())
                .content(boardDTO.getContent())
                .isDeleted(DeletedStatus.NOT_DELETED)
                .status(VisibleStatus.VISIBLE)
                .build();
        Board board = Board.builder()
                .content(content)
                .build();
        return boardRepository.save(board);
    }


    public Board updateBoard(Long id, BoardDTO boardDTO) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            Board board = optionalBoard.get();
            Content content = board.getContent();
            content.setTitle(boardDTO.getTitle());
            content.setContent(boardDTO.getContent());
            return boardRepository.save(board);
        } throw new IllegalArgumentException("Board not found with id: " + id);
    }

    public void deleteBoard(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent())
            boardRepository.deleteById(id);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
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
            content.setTitle(keyword);
            matcher.withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        } else if ("titleAndContent".equals(searchOption)) {
            content.setTitle(keyword);
            content.setContent(keyword);
            matcher.withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                    .withMatcher("content", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        }

        Example<Board> example = Example.of(Board.builder().content(content).build(), matcher);

        Page<Board> boardPage = boardRepository.findAll(example, pageable);
        return boardPage.map(BoardDTO::fromEntity);
    }

}
