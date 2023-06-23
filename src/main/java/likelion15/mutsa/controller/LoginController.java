package likelion15.mutsa.controller;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class LoginController {

    private final LoginService loginService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(){
//        Long userid = (Long)session.getAttribute("userId");
        return "login";
    }
    // 로그인
    @PostMapping("/login")
    public String login(User user
//                        @RequestParam("email") String email,
//                        @RequestParam("password") String password
    ){
        Long userid = loginService.login(user.getEmail(), user.getPassword());
        System.out.println(user.getEmail()+" /" +user.getPassword());
        if (userid != null) { //로그인 성공
            return "redirect:/home";
        }
        else return "redirect:/login"; //로그인 실패
    }
    @GetMapping("/home")
    public String home(){

        return "home";
    }
}
