package likelion15.mutsa.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.dto.NoticeDto;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.File;
import likelion15.mutsa.entity.FileCon;
import likelion15.mutsa.entity.Notice;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.VisibleStatus;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.repository.FileConRepository;
import likelion15.mutsa.repository.FileRepository;
import likelion15.mutsa.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class NoticeService {

    private final List<NoticeDto> noticeList = new ArrayList<>();

    private final NoticeRepository noticeRepository;

    private final FileRepository fileRepository;

    private final FileConRepository fileConRepository;

    private final FileService fileService;


    public Notice createNotice(NoticeDto noticeDto, MultipartFile file) {
        Content content = Content.builder() // content 엔티티 생성
                .title(noticeDto.getTitle()) // controller에서 전달 받은 데이터
                .content(noticeDto.getContent())
                .isDeleted(DeletedStatus.NONE)
                .status(VisibleStatus.VISIBLE)
                .build();
        Notice notice = Notice.builder() // notice 엔티티 생성
                .content(content) // 위에 있는 content 엔티티
                .build();
        if (!file.isEmpty()) { // 첨부 파일이 존재한다면
            File fileEntity = fileService.createFile(file); // 파일 업로드

            FileCon fileCon = FileCon.builder() //fileCon 엔티티 생성 fileid랑 공지 id 등록함으로써 연관 지음
                    .file(fileEntity)
                    .notice(notice)
                    .build();
            fileConRepository.save(fileCon); // 엔티티 저장

        }

        return noticeRepository.save(notice); // 엔티티 저장
    }


    public Page<NoticeDto> readNoticeAllPaged(int page) {
        Pageable pageable = PageRequest.of(page - 1, 5, Sort.by("id").descending());
        Page<Notice> noticePage = noticeRepository.findAll(pageable);
        return noticePage.map(NoticeDto::fromEntity);
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
