package likelion15.mutsa.service;


import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.Likes;
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

    // board 등록
    @Transactional
    public Long writeBoard(Long userId, String title, String content) {
        Board board = Board.builder()
                .content(
                        Content.builder()
                                .title(title)
                                .content(content)
                                .status(VisibleStatus.VISIBLE)
                                .isDeleted(DeletedStatus.NONE)
                                .build()
                )
                .user(userRepository.findById(userId).get())
                .build();
        boardRepository.save(board);
        return board.getId();
    }

    // comment 등록
    @Transactional
    public Long writeComment(Long userId, Long boardId, String content) {
        Comment comment = Comment.builder()
                .comment(content)
                .board(boardRepository.findById(boardId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST)))
                .username(userRepository.findById(userId).get().getName())
                .isDeleted(DeletedStatus.NONE)
                .build();
        return commentRepository.save(comment).getId();
    }

    // board 좋아요
    @Transactional
    public Long likeBoard(Long userId, Long boardId) {
        Optional<Likes> optionalLikes
                = likesRepository.findByUser_IdAndBoard_Id(userId, boardId);

        System.out.println("변경전" + likesRepository.countLikesByBoard_Id(boardId));

        if (optionalLikes.isPresent()) {
            Likes like = optionalLikes.get();

            if (like.getIsLike() == YesOrNo.YES) like.updateLikesYesOrNo(YesOrNo.NO);
            else like.updateLikesYesOrNo(YesOrNo.YES);
            return likesRepository.save(like).getId();

        } else {
            Likes like = Likes.builder()
                    .user(userRepository.findById(userId).get())
                    .board(boardRepository.findById(boardId).get())
                    .isLike(YesOrNo.YES)
                    .isDeleted(DeletedStatus.NONE)
                    .build();
            return likesRepository.save(like).getId();
        }


    }

    // comment 좋아요
    @Transactional
    public Long likeComment(Long userId, Long commentId) {
        Optional<Likes> optionalLikes
                = likesRepository.findByUser_IdAndComment_Id(userId, commentId);

        if (optionalLikes.isPresent()) {
            Likes like = optionalLikes.get();

            if (like.getIsLike().equals(YesOrNo.YES)) like.updateLikesYesOrNo(YesOrNo.NO);
            else like.updateLikesYesOrNo(YesOrNo.YES);
            return likesRepository.save(like).getId();

        } else {
            Likes like = Likes.builder()
                    .user(userRepository.findById(userId).get())
                    .comment(commentRepository.findById(commentId).get())
                    .isLike(YesOrNo.YES)
                    .isDeleted(DeletedStatus.NONE)
                    .build();
            return likesRepository.save(like).getId();
        }
    }

    // board 삭제
    @Transactional
    public Long deleteBoard(Long boardId) {
        Board board = boardRepository.findById(boardId).get();
        boardRepository.updateIsDeletedById(boardId);
        return boardRepository.findById(boardId).get().getId();
    }

    // comment 삭제
    @Transactional
    public Long deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        commentRepository.updateIsDeletedById(commentId);
        return commentRepository.findById(commentId).get().getId();
    }

    // 내가 쓴 모든 board 조회
    public Page<Board> readMyBoardsPaged(int pageNum, int pageLimit, Long userId) {
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        return boardRepository.findAllByUser_IdAndContent_IsDeleted(userId, pageable);
    }

    // 내가 쓴 모든 comment 조회
    public Page<Comment> readMyCommentsPaged(int pageNum, int pageLimit, String username) {
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        return commentRepository.findAllByUsernameAndIsDeleted(username, pageable);
    }

    // 내가 좋아요 한 모든 글 조회
    public Page<Board> readMyLikesBoardsPaged(int pageNum, int pageLimit, Long userId ) {
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        return boardRepository.findAllByUserIdWithLikes(userId, pageable);
    }

    // 내가 좋아요 한 모든 댓글 조회
    public Page<Comment> readMyLikesCommentsPaged(int pageNum, int pageLimit,Long userId) {
        Pageable pageable = PageRequest.of(
                pageNum, pageLimit, Sort.by("id").descending()
        );
        return commentRepository.findAllByUsernameWithLikes(userId, pageable);
    }

}
