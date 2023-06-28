package likelion15.mutsa.service;

import jakarta.transaction.Transactional;
import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.Likes;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import likelion15.mutsa.entity.enums.YesOrNo;
import likelion15.mutsa.repository.*;
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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final BoardPageRepository boardPageRepository;
    private final BoardsRepository boardsRepository;
    private final JoinService joinService;

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
    /////////////////////////////////////////////////////////////////
    // 한 유저가 쓴 모든 글 조회
    public List<Board> findOnesBoards(Long userId) {return boardsRepository.findByUserId(userId);}
    public Page<Board> readBoardPaged(int pageNum, int pageLimit, User user) {
        // PagingAndSortingRepository 메소드에 전달하는 용도
        // 조회하고 싶은 페이지의 정보를 담는 객체
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        Page<Board> boardPages
                = boardPageRepository.findAllByUser(user, pageable);
        // map: 전달받은 함수를 각 원소에 인자로 전달한 결과를 다시모아서 Stream으로
        // Page.map: 전달받은 함수를 각 원소에 인자로 전달한 결과를 다시 모아서 Page로

        return boardPages;
    }
    public List<Board> findOnesLikesBoards(Long userId) {return boardsRepository.findAllByLikesAndUserId(
            userRepository.findOne(userId)
    );}

    // 한 유저가 쓴 모든 댓글 조회
    public List<Comment> findOnesComments(String userName) {return commentRepository.findByUserName(userName);};
    public List<Comment> findOnesLikesComments(String userName) {return commentRepository.findAllByLikesAndUserName(userName);};

    @Transactional
    public Long writeArticle(Long userId, String title, String content) {
        // 게시글 등록
        User user = userRepository.findOne(userId);

        Content content1 = Content.builder()
                .title(title)
                .content(content)
                .status(VisibleStatus.VISIBLE)
                .isDeleted(DeletedStatus.NONE)
                .build();

        Board board = Board.builder()
                .content(content1)
                .user(userRepository.findOne(userId))
                .build();

        boardRepository.save(board);
        return board.getId();
    }
    @Transactional
    public Long writeComment(Long userId, Long boardId, String content) {
        // 게시글 등록
        User user = userRepository.findOne(userId);

        Comment comment = Comment.builder()
                .comment(content)
                .board(boardsRepository.findOne(boardId))
                .username(userRepository.findOne(userId).getName())
                .isDeleted(DeletedStatus.NONE)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }
    @Transactional
    public Long likeArticle(Long userId, Long boardId) {
        // 게시글 등록
        User user = userRepository.findOne(userId);

        Likes likes = Likes.builder()
                .user(user)
                .board(boardsRepository.findOne(boardId))
                .isLike(YesOrNo.YES)
                .isDeleted(DeletedStatus.NONE)
                .build();

        likesRepository.save(likes);
        return likes.getId();
    }
    @Transactional
    public Long likeComment(Long userId, Long commentId) {
        // 게시글 등록
        User user = userRepository.findOne(userId);

        Likes likes = Likes.builder()
                .user(user)
                .comment(commentRepository.findOne(commentId))
                .isLike(YesOrNo.YES)
                .isDeleted(DeletedStatus.NONE)
                .build();

        likesRepository.save(likes);
        return likes.getId();
    }

}