package likelion15.mutsa.controller;

import likelion15.mutsa.dto.User;
import likelion15.mutsa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserController {
    private final UserService userService;
    @GetMapping("/join")
    public String join(){

        return "join";
    }
    @PostMapping("/complete-join")
    public String completeJoin(
            @RequestParam("username")
            String username,
            @RequestParam("email")
            String email,
            @RequestParam("password")
            String password,
            @RequestParam("phoneNumber")
            String phoneNumber,
            Model model){
        User user = userService.joinUser(username,email,password,phoneNumber);
        System.out.println("user = " + user);
        model.addAttribute("username", username);
        return "complete-join";
    }
    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(){
        return "login";
    }
    // 로그인
    @PostMapping("/login")
    public String login(){
//        User user = userService.login(email,password);
        return "redirect:/home";
    }
}
