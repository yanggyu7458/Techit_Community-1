package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    // 한 게시글의 좋아요 갯수
    @Query("select count(l) from Likes l where l.board.id = :boardId")
    public int countLikesByBoard_Id(Long boardId);

    // 한 댓글의 좋아요 갯수
    @Query("select count(l) from Likes l where l.comment.id = :commentId")
    public int countLikesByComment_Id(Long commentId);
}
