package likelion15.mutsa.controller;

import likelion15.mutsa.dto.HelloResponse;
import likelion15.mutsa.entity.Hello;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.repository.CommentRepository;
import likelion15.mutsa.service.BoardService;
import likelion15.mutsa.service.HelloService;
import likelion15.mutsa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static likelion15.mutsa.controller.TempSettings.logInId;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class MyPageController {
    public final BoardService boardService;
    public final UserService userService;



    @GetMapping("/my-page")
    public String myPage(Model model) {

        Long logInId = 1L;
        model.addAttribute("myArticleList", boardService.findOnesBoards(logInId));
        model.addAttribute("myCommentList", boardService.findOnesComments(logInId));
        return "my-page";
    }

}
