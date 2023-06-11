package likelion15.mutsa.service;

import jakarta.persistence.EntityManager;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.BoardRepository;
import likelion15.mutsa.repository.CommentRepository;
import likelion15.mutsa.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class BoardServiceTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    BoardService boardService;
    @Autowired
    BoardRepository boardRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    EntityManager em;


    @Test
    @Transactional
    void writeArticle() {
        //회원 등록
        User user1 = new User();
        user1.setName("성광현");
        Long saveId1 = userService.join(user1);
        em.flush();

        User user2 = new User();
        user2.setName("김땡땡");
        Long saveId2 = userService.join(user2);
        em.flush();

        //글 작성
        String title1 = "작성글 1";
        String content1 = "작성글 1의 내용입니다.";
        Long articleId1 = boardService.writeArticle(saveId1, title1, content1);
        em.flush();


        String title2 = "작성글 2";
        String content2 = "작성글 2의 내용입니다.";
        Long articleId2 = boardService.writeArticle(saveId1, title2, content2);
        em.flush();

        List<Board> result = boardService.findOnesBoards(saveId1);
        for (Board article :
                result) {
            System.out.printf("작성자: %s, 글제목: %s, 글내용: %s\n",
                    article.getUser().getName(),
                    article.getTitle(),
                    article.getContent()
                    );
        }

    }

}