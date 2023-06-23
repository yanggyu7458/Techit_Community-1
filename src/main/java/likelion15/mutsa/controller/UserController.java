package likelion15.mutsa.controller;

import jakarta.servlet.http.HttpSession;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String showCompleteJoin(@RequestParam("realName") String realName, Model model) {
        model.addAttribute("realName", realName);
        return "complete-join";
    }

    @PostMapping("/join")
    public String completeJoin(User user,
                               RedirectAttributes re) {

//        User user = User.builder()
//                        .name(name)
//                        .realName(realName)
//                        .email(email)
//                        .password(password)
//                        .phoneNumber(phoneNumber)
//                        .auth(UserAuth.USER)
//                        .status(UserStatus.U)
//                        .build();
        user.setAuth(UserAuth.USER);
        user.setStatus(UserStatus.U);

        user = userService.joinUser(user);

        System.out.println("user = " + user);

        re.addAttribute("realName", user.getRealName());
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
    public String login(User user
//                        @RequestParam("email") String email,
//                        @RequestParam("password") String password
    ){
        Long userid = userService.login(user.getEmail(), user.getPassword());
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