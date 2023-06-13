package likelion15.mutsa.repository;

import jakarta.persistence.EntityManager;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Comment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {em.persist(comment);}

    public Comment findOne(Long id) {return em.find(Comment.class, id);}

    public List<Comment> findByUserId(Long userId) {
        return em.createQuery("select c from Comment c where c.user.id = :user_id", Comment.class)
                .setParameter("user_id", userId)
                .getResultList();
    }
}
