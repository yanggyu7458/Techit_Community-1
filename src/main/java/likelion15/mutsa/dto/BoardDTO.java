package likelion15.mutsa.dto;

import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.FileCon;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.embedded.Content;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class BoardDTO {
    private Long id;
    private String title;
    private Long userId;
    private String userName;
    private String content;
    private DeletedStatus isDeleted;
    private VisibleStatus status;
    private int likesCount; //좋아요 수
    private int viewCount; // 조회수
    private LocalDateTime createAt; // 작성일
    private MultipartFile file; // 파일을 저장할 필드 추가
    private List<FileConDTO> fileCon;
    private boolean deleteFile; // 파일 삭제 여부
    private List<CommentDTO> comments;
    private String createdBy;
    private String modifiedBy;


    //Board 엔티티 -> BoardDTO
    public static BoardDTO fromEntity(Board entity) {
        BoardDTO boardDTO = new BoardDTO();
        boardDTO.setId(entity.getId());
        boardDTO.setLikesCount(entity.getLikesCount());
        boardDTO.setViewCount(entity.getViewCount());
        //boardDTO.increaseViewCount();
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
        boardDTO.setUserId(user.getId()); // User 엔티티의 id 값을 설정
        boardDTO.setUserName(user.getName()); // User 엔티티의 name 값을 설정
        boardDTO.setCreatedBy(user.getRealName());
        //Comment에서 필요한 정보 추출
        List<Comment> commentList = entity.getComments();
        if (commentList != null) {
            List<CommentDTO> commentDTOList = new ArrayList<>();
            for (Comment comment : commentList) {
                CommentDTO commentDTO = CommentDTO.fromEntity(comment);
                commentDTOList.add(commentDTO);
            }
            boardDTO.setComments(commentDTOList);
        }
        List<FileCon> fileConList = entity.getFileCon();
        if (fileConList != null && !fileConList.isEmpty()) {
            List<FileConDTO> fileConDTOList = new ArrayList<>();
            for (FileCon fileCon : fileConList) {
                FileConDTO fileConDTO = FileConDTO.fromEntity(fileCon);
                fileConDTOList.add(fileConDTO);
            }
            boardDTO.setFileCon(fileConDTOList);
        }


        return boardDTO;
    }

}

