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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MyActivityService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikesRepository likesRepository;
    @Transactional
    public Long writeArticle(Long userId, String title, String content) {
        // 게시글 등록
        Optional<User> optionalUser = userRepository.findById(userId);

        Board board = Board.builder()
                .content(
                        Content.builder()
                                .title(title)
                                .content(content)
                                .status(VisibleStatus.VISIBLE)
                                .isDeleted(DeletedStatus.NONE)
                                .build()
                )
                .user(optionalUser.get())
                .build();

        boardRepository.save(board);
        return board.getId();
    }

    @Transactional
    public Long writeComment(Long userId, Long boardId, String content) {
        // 게시글 등록
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Comment comment = Comment.builder()
                .comment(content)
                .board(boardRepository.findById(boardId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)))
                .username(optionalUser.get().getName())
                .isDeleted(DeletedStatus.NONE)
                .build();

        commentRepository.save(comment);
        return comment.getId();
    }
    @Transactional
    public Long likeArticle(Long userId, Long boardId) {
        // 게시글 등록
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Likes likes = Likes.builder()
                .user(optionalUser.get())
                .board(boardRepository.findById(boardId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)))
                .isLike(YesOrNo.YES)
                .isDeleted(DeletedStatus.NONE)
                .build();

        likesRepository.save(likes);
        return likes.getId();
    }
    @Transactional
    public Long likeComment(Long userId, Long commentId) {
        // 게시글 등록
        Optional<User> optionalUser = userRepository.findById(userId);
        optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        Likes likes = Likes.builder()
                .user(optionalUser.get())
                .comment(commentRepository.findById(commentId)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)))
                .isLike(YesOrNo.YES)
                .isDeleted(DeletedStatus.NONE)
                .build();

        likesRepository.save(likes);
        return likes.getId();
    }


    @Transactional
    public void deleteBoard(Long boardId) {
        Optional<Board> optionalBoard = this.boardRepository.findById(boardId);

        if (optionalBoard.isPresent()) {
            this.boardRepository.deleteById(boardId);
        } else System.out.println("could not find");
    }
    @Transactional
    public void deleteComment(Long commentId) {
        Optional<Comment> optionalComment = this.commentRepository.findById(commentId);
        optionalComment.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));

        commentRepository.deleteById(commentId);

    }

    // 한 유저가 쓴 모든 글 조회
    public Page<Board> readMyBoardsPaged(int pageNum, int pageLimit, Long userId) {
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        return boardRepository.findAllByUser_Id(userId, pageable);
    }

    // 한 유저가 쓴 모든 댓글 조회
    public Page<Comment> readMyCommentsPaged(int pageNum, int pageLimit, String username) {
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        return commentRepository.findAllByUsername(username, pageable);
    }

    // 한 유저가 좋아요 한 모든 글 조회
    public Page<Board> readMyLikesBoardsPaged(int pageNum, int pageLimit, Long userId ) {
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        return boardRepository.findAllByUserIdWithLikes(userId, pageable);
    }

    // 한 유저가 좋아요 한 모든 댓글 조회
    public Page<Comment> readMyLikesCommentsPaged(int pageNum, int pageLimit, String username ) {
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        return commentRepository.findAllByUsernameWithLikes(username, pageable);
    }


}
