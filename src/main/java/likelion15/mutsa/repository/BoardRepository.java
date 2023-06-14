package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Modifying
    @Query("update Board p set p.count = p.count + 1 where p.id = :id")
    int updateCount(Long id);

}
