package likelion15.mutsa.controller;

import likelion15.mutsa.dto.NoticeDto;
import likelion15.mutsa.service.NoticeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class NoticeController {

    private final NoticeService noticeService;

    public NoticeController(
            NoticeService noticeService
    ){
        this.noticeService = noticeService;
    }

    @GetMapping("/notice")
    public String getNotice(Model model) {
        model.addAttribute(
                "noticeList",
                        noticeService.readNoticeAll()
        );
        return "notice";
    }

    @GetMapping("/add-view")
    public String addNoticeView() {
        return "add";
    }

    @PostMapping("/add")
    public String addNotice(
            @RequestParam("title") String title,
            @RequestParam("content") String content) {

        NoticeDto noticeDto
                = noticeService.createNotice(title, content);

        return "redirect:/notice";
    }

    @GetMapping("/{id}")
    public String read(
            @PathVariable("id") Long id,
            Model model
    ) {
//        studentService.readStudent(id);
        model.addAttribute(
                "notice",
                noticeService.readNotice(id)
        );
        return "read";
    }

    // TODO url 설정 / ("/{id}/edit") 또는 ("/{id}/update-view") / @GetMapping
    @GetMapping("/{id}/update-view")
    public String updateView(
            // TODO 아이디와 Model 받아오기 / Long id, Model model
            @PathVariable("id") Long id,
            Model model
    ){
        // TODO Model에 student 데이터 부여 / studentService.readStudent
        model.addAttribute(
                "notice",
                noticeService.readNotice(id)
        );

        // TODO update.html 돌려주기 / "update"
        return "update";
    }

    @PostMapping("/{id}/update")
    public String update(
            // TODO StudentController.readOne()를 참조
            @PathVariable("id") Long id,
            // TODO StudentController.create()를 참조
            @RequestParam("title") String title,
            @RequestParam("content") String content
    ) {
        // service 활용하기
        noticeService.updateNotice(id, title, content);
        // 상세보기 페이지로 redirect
        return String.format("redirect:/%s", id);
    }

    // TODO
    // deleteView 메소드 만들기
    // GetMapping 을 써서...
    // Long id는 어떻게...
    // studentDto 를 가지고...
    // return...
    @GetMapping("/{id}/delete-view")
    public String deleteView(
            @PathVariable("id")
            Long id,
            Model model
    ) {
        NoticeDto noticeDto
                = noticeService.readNotice(id);
        model.addAttribute("notice", noticeDto);
        return "delete";
    }

    @PostMapping("/{id}/delete")
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
