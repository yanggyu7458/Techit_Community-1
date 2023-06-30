package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.enums.DeletedStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    //userId로 작성한 게시물 전체 조회
    Page<Comment> findAllByUsername(String username, Pageable pageable);

    // userId로 좋아요한 게시물 전체 조회
    @Query("SELECT c FROM Comment c JOIN FETCH c.likes l WHERE c.username = :username and c.isDeleted = :isDeleted")
    Page<Comment> findAllByUsernameWithLikes(@Param("username") String username, @Param("isDeleted") DeletedStatus deletedStatus, Pageable pageable);

    default Page<Comment> findAllByUsernameWithLikes(String username, Pageable pageable) {
        return findAllByUsernameWithLikes(username, DeletedStatus.NONE, pageable);
    }

    // 댓글 삭제
    @Query("update Comment c set c.isDeleted = :isDeleted where c.id = :commentId")
    void updateIsDeletedById(@Param("commentId") Long commentId, @Param("isDeleted") DeletedStatus deletedStatus);

    default void updateIsDeletedById(Long commentId) {
        updateIsDeletedById(commentId, DeletedStatus.DELETE);
    }

}