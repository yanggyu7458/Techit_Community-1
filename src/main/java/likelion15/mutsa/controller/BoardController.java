package likelion15.mutsa.controller;

import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.dto.CommentDTO;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.service.BoardService;
import likelion15.mutsa.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
public String create(BoardDTO boardDTO) {
    Board board = boardService.createBoard(boardDTO);
    return "redirect:/board";
}

    @GetMapping("/board")
    public String board(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {
        Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("id").descending()); // 한 페이지에 표시할 게시글 수를 5로 설정, ID 역순으로 정렬
        Page<BoardDTO> boardPage = boardService.readBoardAllPaged(page);

        model.addAttribute("boardList", boardPage.getContent());
        model.addAttribute("currentPage", boardPage.getNumber() + 1);
        model.addAttribute("totalPages", boardPage.getTotalPages());

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
            BoardDTO boardDTO
    ) {
        //service 활용하기
        boardService.updateBoard(id, boardDTO);
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
    public String commentWrite(@PathVariable("id") Long boardId,
                               //@RequestParam("id") Long id,
                               @RequestParam("comment") String comment
                               ) {
        CommentDTO commentDTO = CommentDTO.builder()
                .boardId(boardId)
                //.id(id)
                .comment(comment)
                //.username(username)
                .build();

        Comment createdComment = commentService.createComment(commentDTO);

        return "redirect:/board/" + boardId;
    }
//    @PostMapping("/board/{id}")
//    public String commentWrite(@PathVariable("id") Long boardId,  @RequestParam("comment") String comment, Principal principal) {
//        if (boardId == null) {
//            throw new IllegalArgumentException("게시글 ID를 찾을 수 없습니다.");
//        }
//        //String username = principal.getName();
//        CommentDTO commentDTO = CommentDTO.builder()
//                .comment(comment)
//                //.username(username)
//                .boardId(boardId)
//                .build();
//
//        commentService.createComment(commentDTO);
//
//        return "redirect:/board/" + boardId;
//    }

    //좋아요 기능
    @PostMapping("/board/{id}/like")  // 경로 변수명을 boardId로 변경
    public String likeBoard(@PathVariable("id") Long boardId) {  // 매개변수명을 boardId로 변경
        boardService.likeBoard(boardId);
        return "redirect:/board/" + boardId;  // 경로 변수명을 boardId로 변경
    }

    @PostMapping("/board/{id}/unlike")
    public String unlikeBoard(@PathVariable("id") Long boardId) {
        boardService.unlikeBoard(boardId);
        return "redirect:/board/{id}";
    }

}
