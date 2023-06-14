package likelion15.mutsa.controller;

import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.repository.BoardRepository;
import likelion15.mutsa.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.querydsl.core.types.dsl.Wildcard.count;

@Controller
public class BoardController {
    //BoardService 를 Controller 에도 사용
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/board/create-view") //게시글 생성 페이지
    public String createView() {
        return "boardCreate";
    }
    @PostMapping("/board/create")
    public String create(
            @RequestParam("title")
            String title,
            @RequestParam("content")
            String content,
            @RequestParam("count")
            Integer count
    ) {
        BoardDTO boardDTO = boardService.createBoard(title, content, count);
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

    @GetMapping("/{id}") //{id} <- 변수
    public String read(
            @PathVariable("id") Long id,
            Model model) {
        boardService.readBoard(id);
        boardService.updateCount(id);

        model.addAttribute(
                "board",
                boardService.readBoard(id)
        );
        return "read";
    }
    @GetMapping("/{id}/update-view")
    public String updateView(
            //TODO 아이디와 Model 받아오기 / Long id, Model model
            @PathVariable("id") Long id,
            Model model) {
        //TODO Model에 board 데이터 부여 boardService.readStudent(id)
        BoardDTO dto = boardService.readBoard(id);
        model.addAttribute("board", dto);
        return "update";
    }
    @PostMapping("/{id}/update-view")
    public String update(
            //TODO BoardController.read()을 참조
            @PathVariable("id")
            Long id,
            //TODO BoardController.create()를 참조
            @RequestParam("title")
            String title,
            String content
    ) {
        //service 활용하기
        BoardDTO boardDTO = boardService.updateBoard(id, title, content);
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
}
