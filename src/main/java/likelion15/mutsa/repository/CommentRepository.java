package likelion15.mutsa.repository;

import jakarta.persistence.EntityManager;

import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.enums.DeletedStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class CommentRepository {

    private final EntityManager em;

    public void save(Comment comment) {em.persist(comment);}

    public Comment findOne(Long id) {return em.find(Comment.class, id);}

    public List<Comment> findByUserName(String userName) {
        return em.createQuery("select c from Comment c where c.username = :user_name and c.isDeleted =:isDeleted", Comment.class)
                .setParameter("user_name", userName)
                .setParameter("isDeleted", DeletedStatus.NONE)
                .getResultList();
    }

    public void deleteComment(Long id) {
        em.createQuery("delete from Comment c where c.id =:id", Comment.class)
                .setParameter("id", id);
    }
}
