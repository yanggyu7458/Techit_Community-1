package likelion15.mutsa.dto;

import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardDTO {
    private Long id;
    private String title;
    private User user; // 업데이트: User 대신 UserDTO
    private String content;
    private DeletedStatus isDeleted;
    private VisibleStatus status;
    private Long fileId;
    private int likesCount; //좋아요 수
    private int viewCount; // 조회수
    private LocalDateTime createAt; // 작성일


    public static BoardDTO fromEntity(Board entity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(entity.getId());
        boardDTO.setLikesCount(entity.getLikesCount());
        boardDTO.increaseViewCount();
        //파일 id
        boardDTO.setFileId(entity.getFileId());
        // 작성일 설정
        boardDTO.setCreateAt(entity.getCreateAt());
        // Content에서 필요한 정보 추출
        Content content = entity.getContent();
        boardDTO.setTitle(content.getTitle());
        boardDTO.setContent(content.getContent());
        boardDTO.setIsDeleted(content.getIsDeleted());
        boardDTO.setStatus(content.getStatus());
        // User 정보를 UserDTO로 변환하여 설정
        User user = entity.getUser();
        //UserDto userDTO = UserDto.fromEntity(user);
        boardDTO.setUser(user);

        return boardDTO;
    }
    public void increaseViewCount() {
        this.viewCount++;
    }

}

