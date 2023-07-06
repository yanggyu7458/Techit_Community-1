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
            SessionDto sessionDto = new SessionDto(user);

            // 세션에 sessionDto를 넣어줌
            session.setAttribute("uuid", sessionDto);
            session.setMaxInactiveInterval(1800); // 1800=30분

            // 세션을 세션리스트에 추가(세션 리스트 확인용)
            // getid가 아니고 uuid 추가해야하는지 알아보고 수정하기[ ]
            sessionList.put(session.getId(), session);

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
            // 로그아웃 시 세션 리스트에서도 제거
            sessionList.remove(session.getId());

            session.invalidate();
        }

        return "redirect:/login";
    }
    @GetMapping("/home")
    public String home(Model model,
                       @SessionAttribute(name="uuid",required = false) SessionDto sessionDto){

        if ((sessionDto != null)) {
            model.addAttribute("name", sessionDto.getName());
        }
        return "home";
    }
    // 세션 리스트 확인용
    // {세션id : uuid, 세션id, uuid,..} 형태로 로그인한 유저확인
    @GetMapping("/session-list")
    @ResponseBody
    public Map<String,String> sessionList(){
        Enumeration elements = sessionList.elements();
        Map<String,String> lists = new HashMap<>();
        while (elements.hasMoreElements()) {

            HttpSession session = (HttpSession) elements.nextElement();

            try{
                lists.put(session.getId(), ((SessionDto) session.getAttribute("uuid")).getUuid());
            }catch (IllegalStateException e){
                // 세션이 이미 무효화된 경우, 해당 세션은 무시하고 다음 세션으로 진행
            }
        }
        return lists;
    }
}