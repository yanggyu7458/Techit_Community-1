package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.YesOrNo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //userId로 작성한 게시물 전체 조회
    Page<Comment> findAllByUsernameAndIsDeleted(String username, DeletedStatus isDeleted, Pageable pageable);

    // 게시글 ID에 해당하는 댓글들 조회
    List<Comment> findByBoardId(Long boardId);
    // username으로 작성된 comment 전체 조회
    default Page<Comment> findAllByUsernameAndIsDeleted(String username, Pageable pageable) {
        return findAllByUsernameAndIsDeleted(username, DeletedStatus.NONE, pageable);
    }
    //findByBoardId

    // userId로 좋아요한 게시물 전체 조회
    @Query("SELECT c " +
            "from Comment c " +
            "join fetch Likes l on c.id = l.comment.id " +
            "WHERE l.user.id = ?1 and c.isDeleted = ?2 and l.isLike = ?3")
    Page<Comment> findAllByUsernameWithLikes(Long userId, DeletedStatus isDeleted, YesOrNo isLike, Pageable pageable);

    default Page<Comment> findAllByUsernameWithLikes(Long userId, Pageable pageable) {
        return findAllByUsernameWithLikes(userId, DeletedStatus.NONE, YesOrNo.YES, pageable);
    }

    // 댓글 삭제
    @Query("update Comment c set c.isDeleted = ?2 where c.id = ?1")
    void updateIsDeletedById(Long commentId, DeletedStatus isDeleted);

    default void updateIsDeletedById(Long commentId) {
        updateIsDeletedById(commentId, DeletedStatus.DELETE);
    }
    Optional<Comment> findById(Long boardId);

}