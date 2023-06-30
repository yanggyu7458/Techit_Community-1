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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
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


    private Long nextIdN = 1L;
    private Long nextIdC = 1L;

    public Notice createNotice(NoticeDto noticeDto, MultipartFile file) {
        Content content = Content.builder()
                .title(noticeDto.getTitle())
                .content(noticeDto.getContent())
                .isDeleted(DeletedStatus.NONE)
                .status(VisibleStatus.VISIBLE)
                .build();
        Notice notice = Notice.builder()
                .content(content)
                .build();
        if (!file.isEmpty()) {
            String fileRealName = file.getOriginalFilename(); //파일명을 얻어낼 수 있는 메서드!
            long size = file.getSize(); //파일 사이즈

            System.out.println("파일명 : "  + fileRealName);
            System.out.println("용량크기(byte) : " + size);
            //서버에 저장할 파일이름 fileextension으로 .jsp이런식의  확장자 명을 구함
            String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."),fileRealName.length());
            String uploadFolder = "C:\\techit\\Techit_Community\\src\\main\\java\\likelion15\\mutsa\\file";

            /*
		  파일 업로드시 파일명이 동일한 파일이 이미 존재할 수도 있고 사용자가
		  업로드 하는 파일명이 언어 이외의 언어로 되어있을 수 있습니다.
		  타인어를 지원하지 않는 환경에서는 정산 동작이 되지 않습니다.(리눅스가 대표적인 예시)
		  고유한 랜던 문자를 통해 db와 서버에 저장할 파일명을 새롭게 만들어 준다.
		 */

            UUID uuid = UUID.randomUUID();
            System.out.println(uuid.toString());
            String[] uuids = uuid.toString().split("-");

            String uniqueName = uuids[0];
            System.out.println("생성된 고유문자열" + uniqueName);
            System.out.println("확장자명" + fileExtension);

            // File saveFile = new File(uploadFolder+"\\"+fileRealName); uuid 적용 전

            File saveFile = new File(uploadFolder+"\\"+uniqueName + fileExtension);  // 적용 후
            try {
                file.transferTo(saveFile); // 실제 파일 저장메서드(filewriter 작업을 손쉽게 한방에 처리해준다.)
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
