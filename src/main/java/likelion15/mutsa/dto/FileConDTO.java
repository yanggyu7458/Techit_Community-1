package likelion15.mutsa.dto;

import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.FileCon;
import lombok.Data;

@Data
public class FileConDTO {
    private Long id;
    private FileDTO file; // FileCon과 File 사이의 연결을 위한 속성 추가
    private Long fileId;
    private Long profileId;
    private Long boardId;
    private Long noticeId;
    private String name;
    // 추가적인 필드나 메서드가 필요한 경우 추가

    public void setBoardIdFromEntity(Board board) {
        this.boardId = board.getId();
    }
    public void setFileFromEntity(FileCon fileCon) {
        this.file = FileDTO.fromEntity(fileCon.getFile());
    }
    public static FileConDTO fromEntity(FileCon entity) {
        FileConDTO fileConDTO = new FileConDTO();
        fileConDTO.setId(entity.getId());
        fileConDTO.setFileFromEntity(entity); // FileDTO 설정
        fileConDTO.setName(entity.getFile().getName());
        fileConDTO.setFileId(entity.getFile().getId());
        fileConDTO.setBoardId(entity.getBoard().getId());

        return fileConDTO;
    }
}