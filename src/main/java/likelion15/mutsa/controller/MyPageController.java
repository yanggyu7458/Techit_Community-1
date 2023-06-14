package likelion15.mutsa.controller;


import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.service.BoardService;
import likelion15.mutsa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;



@Controller
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class MyPageController {
    public final BoardService boardService;
    public final UserService userService;

    @GetMapping("/ini")
    public void ini() {
        User user1 = User.builder()
                .email("qwe1@gmail.com")
                .realName("성광현")
                .name("땡땡")
                .password("1234")
                .phoneNumber("010-4949-5895")
                .auth(UserAuth.USER)
                .status(UserStatus.U)
                .build();

        Long saveId1 = userService.join(user1);

        //글 작성
        String title1 = "작성글 1";
        String content1 = "국회의원은 현행범인인 경우를 제외하고는 회기중 국회의 동의없이 체포 또는 구금되지 아니한다. 대통령이 궐위된 때 또는 대통령 당선자가 사망하거나 판결 기타의 사유로 그 자격을 상실한 때에는 60일 이내에 후임자를 선거한다.\n" +
                "\n" +
                "정부는 예산에 변경을 가할 필요가 있을 때에는 추가경정예산안을 편성하여 국회에 제출할 수 있다. 법원은 최고법원인 대법원과 각급법원으로 조직된다.";
        String title2 = "작성글 2";
        String content2 = "모든 국민은 거주·이전의 자유를 가진다. 비상계엄하의 군사재판은 군인·군무원의 범죄나 군사에 관한 간첩죄의 경우와 초병·초소·유독음식물공급·포로에 관한 죄중 법률이 정한 경우에 한하여 단심으로 할 수 있다. 다만, 사형을 선고한 경우에는 그러하지 아니하다.\n" +
                "\n" +
                "통신·방송의 시설기준과 신문의 기능을 보장하기 위하여 필요한 사항은 법률로 정한다. 국가는 농지에 관하여 경자유전의 원칙이 달성될 수 있도록 노력하여야 하며, 농지의 소작제도는 금지된다.";
        String title3 = "작성글 3";
        String content3 = "의무교육은 무상으로 한다. 재의의 요구가 있을 때에는 국회는 재의에 붙이고, 재적의원과반수의 출석과 출석의원 3분의 2 이상의 찬성으로 전과 같은 의결을 하면 그 법률안은 법률로서 확정된다.\n" +
                "\n" +
                "국회는 상호원조 또는 안전보장에 관한 조약, 중요한 국제조직에 관한 조약, 우호통상항해조약, 주권의 제약에 관한 조약, 강화조약, 국가나 국민에게 중대한 재정적 부담을 지우는 조약 또는 입법사항에 관한 조약의 체결·비준에 대한 동의권을 가진다.";


        boardService.writeArticle(saveId1, title1, content1);
        boardService.writeArticle(saveId1, title2, content2);
        boardService.writeArticle(saveId1, title3, content3);
        boardService.writeComment(saveId1, 1L, "1번 게시글에 쓴 댓글입니다.");
        boardService.writeComment(saveId1, 2L, "2번 게시글에 쓴 첫번째 댓글입니다.");
        boardService.writeComment(saveId1, 2L, "2번 게시글에 쓴 두번째 댓글입니다.");
    }

    @GetMapping("/my-page")
    public String myPage(Model model) {

        Long logInId = 1L;
        model.addAttribute("myArticleList", boardService.findOnesBoards(logInId));
        model.addAttribute("myCommentList", boardService.findOnesComments(logInId));
        return "my-page";
    }

}
