package likelion15.mutsa.service;

import jakarta.persistence.EntityManager;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    EntityManager em;
    @Test
    @Transactional
    void join() throws Exception {
        User user = new User();
        user.setName("성광현");

        Long saveId = userService.join(user);

        em.flush();
        assertEquals(user, userRepository.findOne(saveId));
    }
}