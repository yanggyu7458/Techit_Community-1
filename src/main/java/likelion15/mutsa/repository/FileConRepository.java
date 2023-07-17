package likelion15.mutsa.repository;

import likelion15.mutsa.entity.FileCon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileConRepository extends JpaRepository<FileCon, Long> {
    Optional<FileCon> findByProfile_User_Id(Long userId);
    List<FileCon> findByBoard_Id(Long boardId);
}
