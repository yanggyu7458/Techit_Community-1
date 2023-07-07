package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Likes;
import likelion15.mutsa.entity.enums.YesOrNo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    // userId, boardId로 좋아요 찾기
    Optional<Likes> findByUser_IdAndBoard_Id(Long userId, Long boardId);

    // userId, commentId로 좋아요 찾기
    Optional<Likes> findByUser_IdAndComment_Id(Long userId, Long commentId);

    // likesId로 찾아서 YesOrNo 상태 바꾸기
    // @Query("update Likes l set l.isLike = ?2 where l.id = ?1")
    //  Likes updateIsLike(Long likesId, YesOrNo isLike);

    // 한 게시글의 좋아요 갯수
    @Query("select count(l) from Likes l where l.board.id = ?1 and l.isLike = ?2")
    public int countLikesByBoard_Id(Long boardId, YesOrNo isLike);
    default int countLikesByBoard_Id(Long boardId){
        return countLikesByBoard_Id(boardId, YesOrNo.YES);
    }

    // 한 댓글의 좋아요 갯수
    @Query("select count(l) from Likes l where l.comment.id = ?1 and l.isLike = ?2")
    public int countLikesByComment_Id(Long commentId, YesOrNo isLike);
    default int countLikesByComment_Id(Long commentId){
        return countLikesByComment_Id(commentId, YesOrNo.YES);
    }
}
