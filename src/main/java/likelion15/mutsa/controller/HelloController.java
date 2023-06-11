package likelion15.mutsa.controller;

import likelion15.mutsa.dto.HelloResponse;
import likelion15.mutsa.entity.Hello;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.service.BoardService;
import likelion15.mutsa.service.HelloService;
import likelion15.mutsa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class HelloController {
    public final HelloService helloService;
    public final BoardService boardService;
    public final UserService userService;

    @GetMapping("/hello")
    @ResponseBody
    public ModelAndView hello(Map<String, Object> repsonse) {
        Hello hi = helloService.save(Hello.builder()
                .name("hi")
                .build()
        );

        List<HelloResponse> byHello = helloService.findByHello();
        ModelAndView modelAndView = new ModelAndView("/hello");

        modelAndView.addObject("hello", byHello);

        log.info("test hello");
        return modelAndView;
    }

    @GetMapping("/mypage")
    public String myPage(Model model) {

        User user1 = new User();
        user1.setName("성광현");
        Long saveId1 = userService.join(user1);


        User user2 = new User();
        user2.setName("김땡땡");
        Long saveId2 = userService.join(user2);


        //글 작성
        String title1 = "작성글 1";
        String content1 = "작성글 1의 내용입니다.";
        Long articleId1 = boardService.writeArticle(saveId1, title1, content1);



        String title2 = "작성글 2";
        String content2 = "작성글 2의 내용입니다.";
        Long articleId2 = boardService.writeArticle(saveId1, title2, content2);

        String title3 = "작성글 3";
        String content3 = "작성글 3의 내용입니다.";
        Long articleId3 = boardService.writeArticle(saveId2, title3, content3);



        String title4 = "작성글 4";
        String content4 = "작성글 4의 내용입니다.";
        Long articleId4 = boardService.writeArticle(saveId2, title4, content4);

        // id가 1인 유저가 쓴 모든 글 조회
        // 로그인된 사용자의 id 값이 1이라고 가정
        Long logInId = 1L;
        model.addAttribute("myArticleList", boardService.findOnesBoards(logInId));
        return "mypage";
    }

}
