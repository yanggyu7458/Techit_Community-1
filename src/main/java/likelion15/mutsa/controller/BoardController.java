package likelion15.mutsa.controller;

import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.dto.CommentDTO;
import likelion15.mutsa.dto.SessionDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final JoinService joinService;
    private final LoginService loginService;
    private final MyActivityService myActivityService;
    private final MyProfileService myProfileService;

    @GetMapping("/board/create-view") //게시글 생성 페이지
    public String createView() {
        return "boardCreate";
    }

    @PostMapping("/board/create")
    public String create(BoardDTO boardDTO, MultipartFile file, @SessionAttribute(name="uuid",required = false) SessionDto sessionDto) throws Exception {
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        boardService.createBoard(boardDTO, file, loginedUser);
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

    @GetMapping("/board/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model,
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto
    ) {
        BoardDTO boardDTO = boardService.readBoard(id);
        if (boardDTO != null) {
            boardService.increaseViewCount(id); // 조회수 증가

            model.addAttribute("board", boardDTO);
            model.addAttribute("file", boardDTO.getFile());  // fileCon 변수를 모델에 추가
            model.addAttribute("commentList", commentService.readAllComments(id));
            model.addAttribute("sessionDto", sessionDto); // sessionDto를 모델에 추가

            return "readBoard";
        } else {
            // 게시글이 존재하지 않을 경우 예외 처리
            // 예를 들어, 오류 페이지로 리다이렉트 또는 오류 메시지를 표시할 수 있습니다.
            return "redirect:/board";
        }
    }

    @GetMapping("/board/{id}/download")
    public ResponseEntity<byte[]> downloadFile(@PathVariable("id") Long id) throws IOException {
        BoardDTO boardDTO = boardService.readBoard(id);
        if (boardDTO != null && boardDTO.getFile() != null) {
            MultipartFile multipartFile = boardDTO.getFile();
            byte[] fileContent = multipartFile.getBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentDispositionFormData("attachment", multipartFile.getOriginalFilename());

            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } else {
            // 파일이 존재하지 않을 경우 예외 처리
            // 예를 들어, 오류 페이지로 리다이렉트 또는 오류 메시지를 표시할 수 있습니다.
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/{id}/update-view")
    public String updateView(
            @PathVariable("id") Long id,
            Model model) {
        BoardDTO dto = boardService.readBoard(id);
        model.addAttribute("board", dto);
        return "boardUpdate";
    }
    @PostMapping("/{id}/update-view")
    public String update(
            @PathVariable("id")
            Long id,
            BoardDTO boardDTO,
            @SessionAttribute(name="uuid",required = false) SessionDto sessionDto
    ) {
        User loginUser = myProfileService.readByName(sessionDto.getName());
        //service 활용하기
        boardService.updateBoard(id, boardDTO, loginUser);
        //상세보기 페이지로 PRG
        return String.format("redirect:/board/%s", id);
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
    public String delete(@PathVariable("id") Long boardId, @SessionAttribute(name="uuid",required = false) SessionDto sessionDto) {
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        boardService.deleteBoard(boardId, loginedUser);
        return "redirect:/board";

    }
    @PostMapping("/board/{id}")
    public String commentWrite(@PathVariable("id") Long boardId,
                               //@RequestParam("id") Long id,
                               @RequestParam("comment") String comment,
                               @SessionAttribute(name="uuid",required = false) SessionDto sessionDto
                               ) {
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        CommentDTO commentDTO = CommentDTO.builder()
                .boardId(boardId)
                //.id(id)
                .comment(comment)
                .username(loginedUser.getName())
                .build();

        commentService.createComment(commentDTO, loginedUser);

        return "redirect:/board/" + boardId;
    }
    @PostMapping("/board/{boardId}/comment/{commentId}/update")
    public String updateComment(
            @PathVariable("boardId") Long boardId,
            @PathVariable("commentId") Long commentId,
            @RequestParam("comment") String comment,
            @SessionAttribute(name = "uuid", required = false) SessionDto sessionDto
    ) {
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        BoardDTO boardDTO = boardService.readBoard(boardId);

        if (boardDTO != null && boardDTO.getComments() != null) {
            for (CommentDTO commentDTO : boardDTO.getComments()) {
                if (commentDTO.getId().equals(commentId)) {
                    if (commentDTO.getUsername().equals(sessionDto.getName())) {
                        commentDTO.setComment(comment);
                        commentDTO.setEditButton(true); // 해당 댓글의 작성자일 경우 editButton 값을 true로 설정
                        commentService.updateComment(commentId, commentDTO, loginedUser);
                    } else {
                        commentDTO.setEditButton(false); // 해당 댓글의 작성자가 아닐 경우 editButton 값을 false로 설정
                    }
                    break;
                }
            }
        }

        return "redirect:/board/" + boardId;
    }

    @PostMapping("/board/{boardId}/comment/{commentId}/delete")
    public String deleteComment(@PathVariable("boardId") Long boardId,
                                @PathVariable("commentId") Long commentId,
                                @SessionAttribute(name="uuid",required = false) SessionDto sessionDto) {
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        commentService.deleteComment(commentId, loginedUser);
        return "redirect:/board/" + boardId;
    }


    @GetMapping("/board/list")
    public String searchBoards(
            @RequestParam("keyword") String keyword,
            @RequestParam("searchOption") String searchOption,
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("id").descending()); // 한 페이지에 표시할 게시글 수를 5로 설정, ID 역순으로 정렬
        Page<BoardDTO> boardPage = boardService.searchBoardsPaged(keyword, searchOption, pageable);

        model.addAttribute("boardList", boardPage.getContent());
        model.addAttribute("currentPage", boardPage.getNumber() + 1);
        model.addAttribute("totalPages", boardPage.getTotalPages());
        model.addAttribute("keyword", keyword);
        model.addAttribute("searchOption", searchOption);
        return "list";
    }


    //좋아요 기능
//    @PostMapping("/board/{id}/like")  // 경로 변수명을 boardId로 변경
//    public ResponseEntity<String> likeBoard(@PathVariable("id") Long boardId) {  // 매개변수명을 boardId로 변경
//        boardService.likeBoard(boardId);
//        return ResponseEntity.ok("좋아요가 반영되었습니다.");
//    }
//
//    @PostMapping("/board/{id}/unlike")
//    public ResponseEntity<String> unlikeBoard(@PathVariable("id") Long boardId) {
//        boardService.unlikeBoard(boardId);
//        return ResponseEntity.ok("좋아요가 취소되었습니다.");
//    }
    @PostMapping("/board/{id}/like")  // 경로 변수명을 boardId로 변경
    public ResponseEntity<String> likeBoard(@PathVariable("id") Long boardId,
                                            @SessionAttribute(name="uuid",required = false) SessionDto sessionDto) {  // 매개변수명을 boardId로 변경
        User loginedUser = myProfileService.readByName(sessionDto.getName());
        myActivityService.likeBoard(loginedUser.getId(), boardId);
        return ResponseEntity.ok("좋아요가 반영되었습니다.");
    }

}
