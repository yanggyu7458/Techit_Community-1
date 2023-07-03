package likelion15.mutsa.dto;

import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.enums.DeletedStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommentDTO {
    private Long id;
    private Long pid;
    private String comment;
    private String username;
    private DeletedStatus isDeleted;
    private Long boardId;

    public static CommentDTO fromEntity(Comment entity) {
        return CommentDTO.builder()
                .id(entity.getId())
                .pid(entity.getPid())
                .comment(entity.getComment())
                .username(entity.getUsername())
                .isDeleted(entity.getIsDeleted())
                .boardId(entity.getBoard().getId())
                .build();
    }
}
