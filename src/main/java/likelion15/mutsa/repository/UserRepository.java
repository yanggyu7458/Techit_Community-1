package likelion15.mutsa.repository;

import jakarta.persistence.EntityManager;
import likelion15.mutsa.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final EntityManager em;

    public void save(User user) {em.persist(user);}

    public User findOne(Long id) {return em.find(User.class, id);}

    public User findByName(String userName) {
        return (User) em.createQuery("select  u from User u where u.name =:username")
                .setParameter("username", userName)
                .getSingleResult();
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }
}
