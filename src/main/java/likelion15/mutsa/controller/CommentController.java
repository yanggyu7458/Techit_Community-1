package likelion15.mutsa.controller;

import likelion15.mutsa.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;

@Configuration
@RequiredArgsConstructor
@Controller
public class CommentController {

    private final CommentService commentService;
//    public CommentController(CommentService commentService) {
//        this.commentService = commentService;
//    }
//    @PostMapping("/read")
//    public String commentWrite(
//            @PathVariable("id") Long id,
//            @RequestParam("comment") String comment) throws Exception {
//        commentService.createComment(comment);
//        BoardDTO boardDTO = new BoardDTO(id);
//        return String.format("redirect:/%s", id);
//    }
//    @GetMapping("/{id}")
//    public String comment(
//            @PathVariable("id") Long id,
//            Model model) {
//        model.addAttribute(
//                "commentList",
//                commentService.readComment(id)
//        );
//        return "read";
//    }
}
