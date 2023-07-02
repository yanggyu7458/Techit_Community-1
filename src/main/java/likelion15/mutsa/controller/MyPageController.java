package likelion15.mutsa.controller;


import likelion15.mutsa.dto.PasswordDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.service.MyActivityService;
import likelion15.mutsa.service.MyProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/my-page")
public class MyPageController {
    private final MyActivityService myActivityService;
    private final MyProfileService myProfileService;

    @GetMapping("/boards")
    public String readAllBoards(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageLimit
    ) {
        model.addAttribute("myContents", myActivityService.readMyBoardsPaged(pageNum, pageLimit, 1L));
        model.addAttribute("tabValue", "boards");
        return "my-activities";
    }

    @GetMapping("/comments")
    public String readAllComments(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageLimit
    ) {
        User writer = myProfileService.readOne(1L);
        model.addAttribute("myContents", myActivityService.readMyCommentsPaged(pageNum, pageLimit, writer.getName()));
        model.addAttribute("tabValue", "comments");
        return "my-activities";
    }
    @GetMapping("/likes-boards")
    public String readAllLikesBoards(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageLimit
    ) {
        model.addAttribute("myContents", myActivityService.readMyLikesBoardsPaged(pageNum, pageLimit, 1L));
        model.addAttribute("tabValue", "likes-boards");
        return "my-activities";
    }

    @GetMapping("/likes-comments")
    public String readAllLikesComments(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int pageNum,
            @RequestParam(value = "limit", defaultValue = "10") int pageLimit
    ) {
        model.addAttribute("myContents", myActivityService.readMyLikesCommentsPaged(pageNum, pageLimit, 1L));
        model.addAttribute("tabValue", "likes-comments");
        return "my-activities";
    }

    @GetMapping("/profile")
    public String profile(
            Model model
    ) {
        User writer = myProfileService.readOne(1L);
        model.addAttribute("user", writer);
        return "my-profile";
    }

    @PostMapping("/profile/{id}/realname")
    public String updateRealName(@PathVariable("id") Long id, @RequestParam("realName") String realName) {
        myProfileService.updateRealName(id, realName);
        return "redirect:/my-page/profile";
    }

    @PostMapping("/profile/{id}/name")
    public String updateName(@PathVariable("id") Long id, @RequestParam("name") String name) {
        myProfileService.updateName(id, name);
        return "redirect:/my-page/profile";
    }

    @PostMapping("/profile/{id}/email")
    public String updateEmail(@PathVariable("id") Long id, @RequestParam("email") String email) {
        myProfileService.updateEmail(id, email);
        return "redirect:/my-page/profile";
    }

    @PostMapping("/profile/{id}/password")
    public String updatePassword(
            @PathVariable("id") Long id,
            PasswordDto passwordDto
    ) {
        myProfileService.updatePassword(id, passwordDto);
        return "redirect:/my-page/profile";
    }

    @PostMapping("/profile/{id}/phonenumber")
    public String updatePhoneNumber(@PathVariable("id") Long id, @RequestParam("phoneNumber") String phoneNumber) {
        myProfileService.updatePhoneNumber(id, phoneNumber);
        return "redirect:/my-page/profile";
    }
}
