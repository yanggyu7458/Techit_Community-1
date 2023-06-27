package likelion15.mutsa.service;

import likelion15.mutsa.dto.NoticeDto;
import likelion15.mutsa.entity.Notice;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.VisibleStatus;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService {

    private final List<NoticeDto> noticeList = new ArrayList<>();

    private final NoticeRepository noticeRepository;


    private Long nextIdN = 1L;
    private Long nextIdC = 1L;

    public Notice createNotice(NoticeDto noticeDto) {
        Content content = Content.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .isDeleted(DeletedStatus.NONE)
                .status(VisibleStatus.VISIBLE)
                .build();
        Notice notice = Notice.builder()
                .content(content)
                .build();
        return noticeRepository.save(notice);
    }


    public List<NoticeDto> readNoticeAll() {
        List<NoticeDto> noticeList = new ArrayList<>();
        for (Notice notice :
                noticeRepository.findAll()) {
            noticeList.add(NoticeDto.fromEntity(notice));
        }
        return noticeList;
    }



    // 단일 StudentDto를 주는 메소드
    public NoticeDto readNotice(Long id) {
        Optional<Notice> optionalNotice = noticeRepository.findById(id);
        if (optionalNotice.isPresent()) {
            Notice notice = optionalNotice.get();
            return NoticeDto.fromEntity(notice);
        }

        return null;
    }


    public Notice updateNotice(Long id, NoticeDto noticeDto) {
        Optional<Notice> optionalNotice = noticeRepository.findById(id);
        if (optionalNotice.isPresent()) {
            Notice notice = optionalNotice.get();
            Content content = notice.getContent();
            content.setTitle(noticeDto.getTitle());
            content.setContent(noticeDto.getContent());
            return noticeRepository.save(notice);
        } throw new IllegalArgumentException("Notice not found with id: " + id);
    }

    public void deleteNotice(Long id) {
        Optional<Notice> optionalNotice = noticeRepository.findById(id);
        if (optionalNotice.isPresent())
            noticeRepository.deleteById(id);
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
}
