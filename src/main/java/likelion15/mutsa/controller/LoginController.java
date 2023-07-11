package likelion15.mutsa.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import likelion15.mutsa.config.security.CustomUserDetails;
import likelion15.mutsa.dto.JoinDto;
import likelion15.mutsa.dto.SessionDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.service.JoinService;
import likelion15.mutsa.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final JoinService joinService;

    // 세션리스트 확인용
    private final static Hashtable sessionList = new Hashtable();

    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(){
//        Long userid = (Long)session.getAttribute("userId");
        return "login";
    }
    // 로그인
    @PostMapping("/login")
    public String login(JoinDto joinDto,
                        RedirectAttributes re
    ) {

        // 추후 수정해야함
        Long userId = loginService.login(joinDto.getEmail(), joinDto.getPassword());
        System.out.println(joinDto.getEmail() + " /" + joinDto.getPassword());

        if (userId != null) { // 로그인 성공

            return "redirect:/home";
        } else { // 로그인 실패
            if(!joinService.IsExistEmail(joinDto.getEmail())){
                re.addFlashAttribute("loginError","존재하지 않는 이메일입니다.");
            }else{
                re.addFlashAttribute("loginError","비밀번호가 일치하지 않습니다.");
            }
            return "redirect:/login"; // 로그인 실패
        }
    }

    @GetMapping("/logout")
    public String logout() {

        return "redirect:/login";
    }
    @GetMapping("/home")
    public String home(Model model,
                        Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String nickname = userDetails.getName();
            model.addAttribute("name", nickname);
        return "home";
    }
}