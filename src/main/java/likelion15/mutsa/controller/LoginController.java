package likelion15.mutsa.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import likelion15.mutsa.dto.JoinDto;
import likelion15.mutsa.dto.SessionDto;
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
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                        RedirectAttributes re,
                        HttpServletRequest httpServletRequest
    ) {

        Long userId = loginService.login(joinDto.getEmail(), joinDto.getPassword());
        System.out.println(joinDto.getEmail() + " /" + joinDto.getPassword());
        if (userId != null) { // 로그인 성공
            // 세션을 생성하기 전에 기존의 세션 파기
            httpServletRequest.getSession().invalidate();
            HttpSession session = httpServletRequest.getSession(true);

            // sessionDto
            User user = loginService.getLoginUser(userId);
            SessionDto sessionDto = loginService.createSessionDto(user);

            // 세션에 sessionDto를 넣어줌
            session.setAttribute("sessionDto", sessionDto);
            session.setMaxInactiveInterval(1800); // 1800=30분
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
    public String logout(HttpServletRequest request) {

        //세션이 없으면 null return
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        return "redirect:/login";
    }
    @GetMapping("/home")
    public String home(Model model,
                       @SessionAttribute(name="sessionDto",required = false) SessionDto sessionDto){

        if ((sessionDto != null)) {
            model.addAttribute("name", sessionDto.getName());
        }

        return "home";
    }
}