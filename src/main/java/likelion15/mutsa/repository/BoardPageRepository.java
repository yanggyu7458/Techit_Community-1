package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPageRepository extends JpaRepository<Board, Long> {
    Page<Board> findAllByUser(User user, Pageable pageable);
}
