package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.YesOrNo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Override
    Optional<Board> findById(Long id);
    @Query("SELECT b FROM Board b WHERE b.content.title like  %:title%" )
    Page<Board> searchByTitleLike(@Param("title") String keyword, Pageable pageable);

    @Query("SELECT b FROM Board b WHERE b.content.title LIKE %:keyword% OR b.content.content LIKE %:keyword%")
    Page<Board> searchByTitleOrContentLike(@Param("keyword") String keyword, Pageable pageable);

    //userId로 작성한 게시물 전체 조회
    Page<Board> findAllByUser_IdAndContent_IsDeleted(Long userId, DeletedStatus isDeleted, Pageable pageable);

    default Page<Board> findAllByUser_IdAndContent_IsDeleted(Long userId, Pageable pageable) {
        return findAllByUser_IdAndContent_IsDeleted(userId, DeletedStatus.NONE, pageable);
    }

    // userId로 좋아요한 게시물 전체 조회
    @Query("SELECT b " +
            "FROM Board b " +
            "JOIN FETCH Likes l on b.id = l.board.id " +
            "WHERE l.user.id = ?1 and b.content.isDeleted = ?2 and l.isLike = ?3")
    Page<Board> findAllByUserIdWithLikes(Long userId, DeletedStatus isDeleted, YesOrNo isLike, Pageable pageable);

    default Page<Board> findAllByUserIdWithLikes(Long userId, Pageable pageable) {
        return findAllByUserIdWithLikes(userId, DeletedStatus.NONE, YesOrNo.YES, pageable);
    }

    // 게시글 삭제
    @Query("update Board b set b.content.isDeleted = ?2 where b.id = ?1")
    void updateIsDeletedById(Long boardId, DeletedStatus isDeleted);

    default void updateIsDeletedById(Long boardId) {
        updateIsDeletedById(boardId, DeletedStatus.DELETE);
    }

}
