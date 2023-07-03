package likelion15.mutsa.repository;

import likelion15.mutsa.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
