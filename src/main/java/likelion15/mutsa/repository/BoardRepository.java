package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface BoardRepository extends JpaRepository<Board, Long> {

    //userId로 작성한 게시물 전체 조회
    Page<Board> findAllByUser_Id(Long userId, Pageable pageable);

    // userId로 좋아요한 게시물 전체 조회
    @Query("SELECT b FROM Board b JOIN FETCH b.likes l WHERE b.user.id = :userId")
    Page<Board> findAllByUserIdWithLikes(Long userId, Pageable pageable);




}
