package likelion15.mutsa.dto;

import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import lombok.Data;

@Data
public class BoardDTO {
    private Long id;
    private String title;
    private User user;
    private String content;
    private DeletedStatus isDeleted;
    private VisibleStatus status;

    public static BoardDTO fromEntity(Board entity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(entity.getId());

        // Content에서 필요한 정보 추출
        Content content = entity.getContent();
        boardDTO.setTitle(content.getTitle());
        boardDTO.setContent(content.getContent());
        boardDTO.setIsDeleted(content.getIsDeleted());
        boardDTO.setStatus(content.getStatus());

        return boardDTO;
    }
}

