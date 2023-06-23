package likelion15.mutsa.controller;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.service.JoinService;
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
@Slf4j
public class JoinController {

    private final JoinService joinService;

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

        user = joinService.joinUser(user);

        System.out.println("user = " + user);

        re.addAttribute("realName", user.getRealName());
        return "redirect:/complete-join";
    }
}
