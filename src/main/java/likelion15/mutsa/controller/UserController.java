package likelion15.mutsa.controller;

import jakarta.servlet.http.HttpSession;
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

    // 회원가입 페이지
    @GetMapping("/join-view")
    public String joinView(){
        return "join";
    }
    @GetMapping("/complete-join")
    public String showCompleteJoin(){
        return "complete-join";
    }
    @PostMapping("/join")
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
//        return "complete-join";
        // redirect를하면 showCompleteJoin()함수를 호출하게된다.
        return "redirect:/complete-join";
    }
    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(){
//        Long userid = (Long)session.getAttribute("userId");


        return "login";
    }
    // 로그인
    @PostMapping("/login")
    public String login(@RequestParam("email") String email,
                        @RequestParam("password") String password){
        Long userid = userService.login(email, password);

        if (userid != null) { //로그인 성공
            System.out.println("로그인 성공");
            return "redirect:/home";
        }
        else return "redirect:/login";
    }
    @GetMapping("/home")
    public String home(){

        return "home";
    }
}
