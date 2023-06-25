package likelion15.mutsa.service;


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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    private final BoardPageRepository boardPageRepository;
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
                .board(boardRepository.findOne(boardId))
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
                .board(boardRepository.findOne(boardId))
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


    @Transactional
    public void deleteBoard(Long id) {
        Optional<Board> board =
                Optional.ofNullable(this.boardRepository.findOne(id));
        if (board.isPresent()) {
            this.boardRepository.deleteBoard(id);
        }
        else {
            System.out.println("could not find");
        }
    }
    @Transactional
    public void deleteComment(Long id) {
        Optional<Comment> comment =
                Optional.ofNullable(this.commentRepository.findOne(id));
        if (comment.isPresent()) {
            this.commentRepository.deleteComment(id);
        }
        else {
            System.out.println("could not find");
        }
    }

    // 한 유저가 쓴 모든 글 조회
    public List<Board> findOnesBoards(Long userId) {return boardRepository.findByUserId(userId);}
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
    public List<Board> findOnesLikesBoards(Long userId) {return boardRepository.findAllByLikesAndUserId(
            userRepository.findOne(userId)
    );}

    // 한 유저가 쓴 모든 댓글 조회
    public List<Comment> findOnesComments(String userName) {return commentRepository.findByUserName(userName);};
    public List<Comment> findOnesLikesComments(String userName) {return commentRepository.findAllByLikesAndUserName(userName);};

}
