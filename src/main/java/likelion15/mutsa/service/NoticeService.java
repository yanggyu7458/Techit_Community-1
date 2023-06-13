package likelion15.mutsa.service;

import likelion15.mutsa.dto.NoticeDto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoticeService {

    private final List<NoticeDto> noticeList = new ArrayList<>();

    private Long nextId = 1L;

    public NoticeService(){
        createNotice("교안 업로드", "교안이 올라왔습니다.");
        createNotice("커뮤니티 규칙", "예의 바르게");
    }

    public NoticeDto createNotice(String title, String content) {
        NoticeDto newNotice = new NoticeDto(nextId, title, content);
        nextId++;
        noticeList.add(newNotice);
        return newNotice;
    }

    public List<NoticeDto> readNoticeAll() {
        return noticeList;
    }

    // 단일 StudentDto를 주는 메소드
    public NoticeDto readNotice(Long id) {
        for (NoticeDto noticeDto: noticeList) {
            if (noticeDto.getId().equals(id))
                return noticeDto;
        }

        return null;
    }

    public NoticeDto updateNotice(Long id, String title, String content){
        NoticeDto targetDto = this.readNotice(id);
        if (targetDto != null) {
            targetDto.setTitle(title);
            targetDto.setContent(content);
            return targetDto;
        } else return null;
    }

    public boolean deleteNotice(Long id) {
        int target = -1;
        // 학생 리스트를 살펴보며
        for (int i = 0; i < noticeList.size(); i++) {
            // 대상을 선정한다.
            if (noticeList.get(i).getId().equals(id)) {
                target = i;
                break;
            }
        }
        // 검색 성공시
        if (target != -1){
            // 삭제
            noticeList.remove(target);
            return true;
        }
        else return false;
    }
}
