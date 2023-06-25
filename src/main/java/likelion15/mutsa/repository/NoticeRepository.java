package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Override
    Optional<Notice> findById(Long id);
}