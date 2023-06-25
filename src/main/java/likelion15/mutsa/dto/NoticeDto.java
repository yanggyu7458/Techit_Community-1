package likelion15.mutsa.dto;

import likelion15.mutsa.entity.Notice;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import lombok.Data;

@Data
public class NoticeDto {
    private Long id;
    private String title;
    private String content;
    private DeletedStatus isDeleted;
    private VisibleStatus status;


    public static NoticeDto fromEntity(Notice entity) {
        NoticeDto noticeDto = new NoticeDto();
        noticeDto.setId(entity.getId());

        // Content에서 필요한 정보 추출
        Content content = entity.getContent();
        noticeDto.setTitle(content.getTitle());
        noticeDto.setContent(content.getContent());
        noticeDto.setIsDeleted(content.getIsDeleted());
        noticeDto.setStatus(content.getStatus());


        return noticeDto;
    }
}