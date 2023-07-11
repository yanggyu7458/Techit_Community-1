package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Override
    Optional<Notice> findById(Long id);

    @Query("SELECT b FROM Notice b WHERE b.content.title like  %:title%" )
    Page<Notice> searchByTitleLike(@Param("title") String keyword, Pageable pageable);

    @Query("SELECT b FROM Notice b WHERE b.content.title LIKE %:keyword% OR b.content.content LIKE %:keyword%")
    Page<Notice> searchByTitleOrContentLike(@Param("keyword") String keyword, Pageable pageable);

}