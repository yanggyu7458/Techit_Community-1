package likelion15.mutsa.controller;

import jakarta.servlet.http.HttpSession;
import likelion15.mutsa.dto.JoinDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.service.JoinService;
import likelion15.mutsa.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final JoinService joinService;

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(){
//        Long userid = (Long)session.getAttribute("userId");
        return "login";
    }
    // 로그인
    @PostMapping("/login")
    public String login(JoinDto joinDto,
                        HttpSession session
    ) {
        Long userid = loginService.login(joinDto.getEmail(), joinDto.getPassword());
        System.out.println(joinDto.getEmail() + " /" + joinDto.getPassword());
        if (userid != null) { // 로그인 성공
            session.setAttribute("realName", joinDto.getRealName());
            return "redirect:/home";
        } else {
            return "redirect:/login"; // 로그인 실패
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        // 세션 무효화
        session.invalidate();
        return "redirect:/login";
    }
    @GetMapping("/home")
    public String home(HttpSession session, Model model){
        // 세션에서 사용자 정보 확인

        String realName = (String) session.getAttribute("realName");
        if (realName != null) {
            // 로그인한 사용자 정보 전달
            model.addAttribute("realName", realName);
        }
        return "home";
    }
}
