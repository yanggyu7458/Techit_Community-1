package likelion15.mutsa.controller;

import likelion15.mutsa.config.security.CustomUserDetails;
import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.dto.CommentDTO;
import likelion15.mutsa.entity.File;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class BoardController {
    private final BoardService boardService;
    private final CommentService commentService;
    private final JoinService joinService;
    private final LoginService loginService;
    private final MyActivityService myActivityService;
    private final MyProfileService myProfileService;
    private final FileService fileService;

    @GetMapping("/board/create-view") //게시글 생성 페이지
    public String createView() {
        return "boardCreate";
    }

    @PostMapping("/board/create")
    public String create(
            BoardDTO boardDTO, MultipartFile file,
                         Authentication authentication
    ) throws Exception {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        User loginedUser = myProfileService.readUser(userDetails.getName());


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
            Authentication authentication
    ) {
        BoardDTO boardDTO = boardService.readBoard(id);
        if (boardDTO != null) {
            boardService.increaseViewCount(id); // 조회수 증가
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            int boardLikeCount = boardService.getCntBoardLikes(id);
            List<File> fileList = fileService.getFilesByBoardId(id); // 게시글에 해당하는 첨부 파일 목록 조회

            model.addAttribute("board", boardDTO);
            //model.addAttribute("file", boardDTO.getFile());  // fileCon 변수를 모델에 추가
            model.addAttribute("fileList", fileList); // 파일 목록을 모델에 추가
            model.addAttribute("commentList", commentService.readAllComments(id));
            model.addAttribute("sessionDto", userDetails); // sessionDto를 모델에 추가
            model.addAttribute("file", fileService.readFile(id));
            model.addAttribute("boardLikeCount", boardLikeCount);
            return "readBoard";
        } else {
            // 게시글이 존재하지 않을 경우 예외 처리
            // 예를 들어, 오류 페이지로 리다이렉트 또는 오류 메시지를 표시할 수 있습니다.
            return "redirect:/board";
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
            @RequestParam("file") MultipartFile file, // MultipartFile 인자로 받아옴
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User loginUser = myProfileService.readUser(userDetails.getName());

        //service 활용하기
        boardService.updateBoard(id, boardDTO, file, loginUser);
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
    public String delete(
            @PathVariable("id") Long boardId,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User loginedUser = myProfileService.readUser(userDetails.getName());
        boardService.deleteBoard(boardId, loginedUser);
        return "redirect:/board";

    }
    @PostMapping("/board/{id}")
    public String commentWrite(@PathVariable("id") Long boardId,
                               //@RequestParam("id") Long id,
                               @RequestParam("comment") String comment,
                               Authentication authentication,
                               Model model
                               ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User loginedUser = myProfileService.readUser(userDetails.getName());
        CommentDTO commentDTO = CommentDTO.builder()
                .boardId(boardId)
                //.id(id)
                .comment(comment)
                .username(loginedUser.getName())
                .build();
        model.addAttribute(
                "file",
                fileService.readFile(boardId)

        );

        commentService.createComment(commentDTO, loginedUser);

        return "redirect:/board/" + boardId;
    }
    @PostMapping("/board/{boardId}/comment/{commentId}/update")
    public String updateComment(
            @PathVariable("boardId") Long boardId,
            @PathVariable("commentId") Long commentId,
            @RequestParam("comment") String comment,
            Authentication authentication
    ) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User loginedUser = myProfileService.readUser(userDetails.getName());
        BoardDTO boardDTO = boardService.readBoard(boardId);

        if (boardDTO != null && boardDTO.getComments() != null) {
            for (CommentDTO commentDTO : boardDTO.getComments()) {
                if (commentDTO.getId().equals(commentId)) {
                    if (commentDTO.getUsername().equals(userDetails.getName())) {
                        commentDTO.setComment(comment);
                        commentDTO.setEditButton(true); // 해당 댓글의 작성자일 경우 editButton 값을 true로 설정
                        commentService.updateComment(commentDTO.getId(), commentDTO, loginedUser);
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
                                Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User loginedUser = myProfileService.readUser(userDetails.getName());
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
    @PostMapping("/board/{id}/like")  // 경로 변수명을 boardId로 변경
    public ResponseEntity<String> likeBoard(
            @PathVariable("id") Long boardId,
            Authentication authentication
                                            ) {  // 매개변수명을 boardId로 변경
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User loginedUser = myProfileService.readUser(userDetails.getName());
        myActivityService.likeBoard(loginedUser.getId(), boardId);
        return ResponseEntity.ok("좋아요가 반영되었습니다.");
    }
}
