package likelion15.mutsa.controller;


import likelion15.mutsa.dto.PasswordDto;
import likelion15.mutsa.dto.SessionDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.service.MyActivityService;
import likelion15.mutsa.service.MyProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/my-page")
public class MyPageController {
    private final MyActivityService myActivityService;
    private final MyProfileService myProfileService;

    @GetMapping({"/boards", ""})
    public String readAllBoards(
            Model model,
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageLimit
    ) {

        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");

        User loginedUser = myProfileService.readByName(sessionDto.getName());
        model.addAttribute("myContents",
                    myActivityService.readMyBoardsPaged(pageNum, pageLimit, loginedUser.getId()));
        model.addAttribute("tabValue", "boards");
        return "my-activities";

    }

    @GetMapping("/comments")
    public String readAllComments(
            Model model,
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageLimit
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        model.addAttribute("myContents", myActivityService.readMyCommentsPaged(pageNum, pageLimit, sessionDto.getName()));
        model.addAttribute("tabValue", "comments");
        return "my-activities";
    }
    @GetMapping("/likes-boards")
    public String readAllLikesBoards(
            Model model,
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageLimit
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        model.addAttribute("myContents", myActivityService.readMyLikesBoardsPaged(pageNum, pageLimit, loginedUser.getId()));
        model.addAttribute("tabValue", "likes-boards");
        return "my-activities";
    }

    @GetMapping("/likes-comments")
    public String readAllLikesComments(
            Model model,
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageLimit
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        model.addAttribute("myContents", myActivityService.readMyLikesCommentsPaged(pageNum, pageLimit, loginedUser.getId()));
        model.addAttribute("tabValue", "likes-comments");
        return "my-activities";
    }

    @GetMapping("/profile")
    public String profile(
            Model model,
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        model.addAttribute("user", loginedUser);
        return "my-profile";
    }

    @PostMapping("/profile/{id}/realname")
    public String updateRealName(
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @PathVariable("id") Long id,
            @RequestParam("realName") String realName
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        myProfileService.updateRealName(id, realName);
        return "redirect:/my-page/profile";
    }

    @PostMapping("/profile/{id}/name")
    public String updateName(
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @PathVariable("id") Long id,
            @RequestParam("name") String name
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        myProfileService.updateName(id, name);
        return "redirect:/my-page/profile";
    }

    @PostMapping("/profile/{id}/email")
    public String updateEmail(
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @PathVariable("id") Long id,
            @RequestParam("email") String email
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        myProfileService.updateEmail(id, email);
        return "redirect:/my-page/profile";
    }

    @PostMapping("/profile/{id}/password")
    public String updatePassword(
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @PathVariable("id") Long id,
            PasswordDto passwordDto
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        myProfileService.updatePassword(id, passwordDto);
        return "redirect:/my-page/profile";
    }

    @PostMapping("/profile/{id}/phonenumber")
    public String updatePhoneNumber(
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto,
            @PathVariable("id") Long id,
            @RequestParam("phoneNumber") String phoneNumber
    ) {
        if (sessionDto == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "로그인이 필요합니다.");
        myProfileService.updatePhoneNumber(id, phoneNumber);
        return "redirect:/my-page/profile";
    }
}
