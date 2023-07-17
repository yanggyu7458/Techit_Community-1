package likelion15.mutsa.controller;

import likelion15.mutsa.config.security.CustomUserDetails;
import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.dto.JoinDto;
import likelion15.mutsa.dto.NoticeDto;
import likelion15.mutsa.service.BoardService;
import likelion15.mutsa.service.JoinService;
import likelion15.mutsa.service.LoginService;
import likelion15.mutsa.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class LoginController {

    private final LoginService loginService;
    private final JoinService joinService;
    private final NoticeService noticeService;
    private final BoardService boardService;


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
        //joinDto의 username 은 email임
        Long userId = loginService.login(joinDto.getUsername(), joinDto.getPassword());
        System.out.println(joinDto.getUsername() + " /" + joinDto.getPassword());

        if (userId != null) { // 로그인 성공

            return "redirect:/home";
        } else { // 로그인 실패
            if(!joinService.IsExistEmail(joinDto.getUsername())){
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
                       @RequestParam(value = "page", defaultValue = "1") int page,
                        Authentication authentication){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String nickname = userDetails.getName();
        Page<BoardDTO> board = boardService.readBoardAllPaged(page);
        Page<NoticeDto> notice = noticeService.readNoticeAllPaged(page);

        model.addAttribute("name", nickname);
        model.addAttribute("boardList", board);
        model.addAttribute("noticeList", notice);
        return "home";
    }
}