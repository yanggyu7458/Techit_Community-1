package likelion15.mutsa.dto;

import jakarta.validation.constraints.NotBlank;
import likelion15.mutsa.entity.Notice;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NoticeDto {
    private Long id;
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 작성해주세요.")
    private String content;
    private DeletedStatus isDeleted;
    private VisibleStatus status;
    private LocalDateTime createAt; // 작성일


    public static NoticeDto fromEntity(Notice entity) {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setId(entity.getId());

        // Content에서 필요한 정보 추출
        Content content = entity.getContent();
        noticeDto.setTitle(content.getTitle());
        noticeDto.setContent(content.getContent());
        noticeDto.setIsDeleted(content.getIsDeleted());
        noticeDto.setStatus(content.getStatus());

        // 작성일 설정
        noticeDto.setCreateAt(entity.getCreateAt());

        return noticeDto;
    }
}