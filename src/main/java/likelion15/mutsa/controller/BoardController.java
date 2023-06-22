package likelion15.mutsa.controller;

import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.service.BoardService;
import likelion15.mutsa.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BoardController {
    //BoardService 를 Controller 에도 사용
    private final BoardService boardService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }


    @GetMapping("/board/create-view") //게시글 생성 페이지
    public String createView() {
        return "boardCreate";
    }
//    @PostMapping("/board/create")
//    public BoardDTO create(@RequestBody BoardDTO boardDTO) {
//        return boardService.createBoard(boardDTO);
//    }
    @PostMapping("/board/create")
    public String create(
            Long id,
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content
    ) {
        BoardDTO boardDTO = boardService.createBoard(id, title, content);
        return "redirect:/board";
    }

    @GetMapping("/board")
    public String board(
            Model model) {
        model.addAttribute(
                "boardList",
                boardService.readBoardAll()
        );

        return "board";
    }

    @GetMapping("/board/{id}") //{id} <- 변수
    public String read(
            @PathVariable("id") Long id,
            Model model) {
        boardService.readBoard(id);

        model.addAttribute(
                "board",
                boardService.readBoard(id)
        );
        model.addAttribute("commentList", commentService.readCommentAll());
        return "read";
    }
    @GetMapping("/{id}/update-view")
    public String updateView(
            //TODO 아이디와 Model 받아오기 / Long id, Model model
            @PathVariable("id") Long id,
            Model model) {
        //TODO Model에 student 데이터 부여 studentService.readStudent(id)
        BoardDTO dto = boardService.readBoard(id);
        model.addAttribute("board", dto);
        return "update";
    }
    @PostMapping("/{id}/update-view")
    public String update(
            //TODO StudentController.read()을 참조
            @PathVariable("id")
            Long id,
            //TODO StudentController.create()를 참조
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content
    ) {
        //service 활용하기
        boardService.updateBoard(id, title, content);
        //상세보기 페이지로 PRG
        return String.format("redirect:/%s", id);
    }
    //delete 메소드 만들기
    @GetMapping("/{id}/delete-view")
    public String deleteView(
            @PathVariable("id")
            Long id,
            Model model) {
        BoardDTO dto = boardService.readBoard(id);
        model.addAttribute("board", dto);
        return "boardDelete";
    }
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        boardService.deleteBoard(id);
        return "redirect:/board";

    }
    @PostMapping("/board/{id}")
    public String commentWrite(
            @PathVariable("id") Long boardId,
            @RequestParam("comment") String comment) throws Exception {
        commentService.createComment(boardId, comment);
        boardService.readBoard(boardId);
        return "redirect:/board/{id}";
    }

}
