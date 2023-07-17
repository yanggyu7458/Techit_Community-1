package likelion15.mutsa.dto;

import likelion15.mutsa.entity.File;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
//@Builder
@NoArgsConstructor
public class FileDTO {
    private Long id;
    private String fileName;
    private String filePath;
    private Long fileSize; //크기?

    public static FileDTO fromEntity(File entity) {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setId(entity.getId());
        fileDTO.setFileName(entity.getName());

        return fileDTO;
    }

}
