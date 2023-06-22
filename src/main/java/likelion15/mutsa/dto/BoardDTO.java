package likelion15.mutsa.dto;

import likelion15.mutsa.entity.User;
import lombok.Data;

@Data
public class BoardDTO {
    private Long id;
    private User user;
    private String title;
    private String content;
    public BoardDTO(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

//    public static BoardDTO fromEntity(Board entity) {
//        BoardDTO boardDTO = new BoardDTO();
//        boardDTO.setId(entity.getId());
//        boardDTO.setContent(entity.getContent());
//        boardDTO.setUser(entity.getUser());
//
//        return boardDTO;
//
//    }
//    public BoardDTO build() {
//        BoardDTO boardDTO = new BoardDTO();
//        boardDTO.setId(id);
//        boardDTO.setTitle(title);
//        boardDTO.setContent(content);
//        boardDTO.setUser(user);
//        return boardDTO;
//    }

}
