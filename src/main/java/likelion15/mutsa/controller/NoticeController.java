package likelion15.mutsa.controller;

import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.dto.NoticeDto;
import likelion15.mutsa.entity.Notice;
import likelion15.mutsa.service.FileService;
import likelion15.mutsa.service.NoticeService;
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
import org.springframework.web.multipart.MultipartFile;

@Controller
public class NoticeController {

    private final NoticeService noticeService;
    private final FileService fileService;

    public NoticeController(
            NoticeService noticeService,
            FileService fileService){
        this.noticeService = noticeService;
        this.fileService = fileService;
    }

    //공지 페이지
    @GetMapping("/notice")
    public String getNotice(
            @RequestParam(value = "page", defaultValue = "1") int page,
            Model model) {
        Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("id").descending()); // 한 페이지에 표시할 게시글 수를 5로 설정, ID 역순으로 정렬
        Page<NoticeDto> noticePage = noticeService.readNoticeAllPaged(page);

        model.addAttribute("noticeList", noticePage.getContent());
        model.addAttribute("currentPage", noticePage.getNumber() + 1);
        model.addAttribute("totalPages", noticePage.getTotalPages());
        //notice.html 불러옴
        return "notice";
    }

    //공지추가페이지로 이동
    @GetMapping("/notice/add-view")
    public String addNoticeView() {
        return "noticeAdd";
    }
    //noticeAdd.html페이지 전송

    @PostMapping("/notice/add")
    public String addNotice(
            // noticeAdd.html파일에서 입력한 제목, 내용 정보를 가져옴
            NoticeDto noticeDto,
            // noticeAdd.html파일에서 가져온 파일 정보
            @RequestParam("files") MultipartFile file) {
        Notice notice =
                noticeService.createNotice(noticeDto, file);

        return "redirect:/notice";
    }

    @GetMapping("/notice/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model
    ) {
//        studentService.readStudent(id);
        model.addAttribute(
                "notice",
                noticeService.readNotice(id)

        );
        model.addAttribute(
                "file",
                fileService.readFile(id)

        );

        return "noticeRead";
    }




    @GetMapping("/notice/{id}/update-view")
    public String updateView(

            @PathVariable("id") Long id,
            Model model
    ){

        NoticeDto dto = noticeService.readNotice(id);
        model.addAttribute("notice", dto);


        return "noticeUpdate";
    }

    @PostMapping("/notice/{id}/update")
    public String update(

            @PathVariable("id") Long id,

            NoticeDto noticeDto
    ) {
        // service 활용하기
        noticeService.updateNotice(id, noticeDto);
        // 상세보기 페이지로 redirect
        return String.format("redirect:/notice/%s", id);
    }

    @GetMapping("/notice/{id}/delete-view")
    public String deleteView(
            @PathVariable("id")
            Long id,
            Model model
    ) {
        NoticeDto noticeDto
                = noticeService.readNotice(id);
        model.addAttribute("notice", noticeDto);
        return "noticeDelete";
    }

    @PostMapping("/notice/{id}/delete")
    public String delete(
            @PathVariable("id")
            Long id
    ) {
        noticeService.deleteNotice(id);
        // update 때는 데이터가 남아있지만
        // delete 는 돌아갈 상세보기가 없다
        // 그래서 홈으로 돌아간다.
        return "redirect:/notice";
    }

}