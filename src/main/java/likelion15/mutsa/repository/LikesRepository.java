package likelion15.mutsa.repository;

import jakarta.persistence.EntityManager;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.Likes;
import likelion15.mutsa.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class LikesRepository {



    private final EntityManager em;

    public void save(Likes likes) {
            em.persist(likes);
        }

    public Likes findOne(Long id) {return em.find(Likes.class, id);}

    public List<Likes> findAllByUser(User user) {
        return em.createQuery("select l from Likes l where l.user.id =:userId")
                .setParameter("userId", user.getId())
                .getResultList();
    }

    public List<Likes> findByBoard(Board board) {
        return em.createQuery("select l from Likes l where l.board.id =:boardId")
                .setParameter("boardId", board.getId())
                .getResultList();
    }

    public List<Likes> findByComment(Comment comment) {
        return em.createQuery("select l from Likes l where l.comment.id =:commentId")
                .setParameter("commentId", comment.getId())
                .getResultList();
    }


}
