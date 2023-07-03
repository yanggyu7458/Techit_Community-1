package likelion15.mutsa.dto;

import likelion15.mutsa.entity.Board;
import lombok.Data;

@Data
public class FileConDTO {
    private Long id;
    private Long fileId;
    private Long profileId;
    private Long boardId;
    private Long noticeId;
    // 추가적인 필드나 메서드가 필요한 경우 추가

    public void setBoardIdFromEntity(Board board) {
        this.boardId = board.getId();
    }
}