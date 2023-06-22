package likelion15.mutsa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentDTO {
    private Long id;
    private String comment;

    public CommentDTO(Long id, String comment) {
        this.id = id;
        this.comment = comment;
    }
}
