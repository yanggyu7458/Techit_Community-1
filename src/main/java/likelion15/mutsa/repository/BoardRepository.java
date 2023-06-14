package likelion15.mutsa.repository;

import jakarta.persistence.EntityManager;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepository {

    private final EntityManager em;

    public void save(Board board) {em.persist(board);}

    public Board findOne(Long id) {return em.find(Board.class, id);}

    public List<Board> findByUserId(Long userId) {
        return em.createQuery("select b from Board b where b.user.id =:user_id", Board.class)
                .setParameter("user_id", userId)
                .getResultList();
    }
}
