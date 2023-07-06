package likelion15.mutsa.service;

import jakarta.servlet.http.HttpServletResponse;
import likelion15.mutsa.entity.File;
import likelion15.mutsa.entity.FileCon;
import likelion15.mutsa.repository.BoardRepository;
import likelion15.mutsa.repository.FileConRepository;
import likelion15.mutsa.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
@RequiredArgsConstructor
@Service
public class FileService {
    private FileRepository fileRepository;
    private final FileConRepository fileConRepository;
    @Autowired
    private BoardRepository boardRepository;


    public File readFile(Long id) {
        for (FileCon fileCon :
                fileConRepository.findAll()) {

            if (fileCon.getNotice().getId().equals(id)){
                File file = fileCon.getFile();
                return file;
            }
        }

        return null;
    }


    public void downFile(File file, HttpServletResponse response) throws IOException {
        String uploadFolder = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\file";

        java.io.File saveFile = new java.io.File(uploadFolder+"\\"+file.getName());

        // file 다운로드 설정
        response.setContentType("application/download");
        response.setContentLength((int)saveFile.length());
        response.setHeader("Content-disposition", "attachment;filename=\"" + file.getName() + "\"");
        // response 객체를 통해서 서버로부터 파일 다운로드
        OutputStream os = response.getOutputStream();
        // 파일 입력 객체 생성
        FileInputStream fis = new FileInputStream(saveFile);
        FileCopyUtils.copy(fis, os);
        fis.close();
        os.close();
    }



//    public  FileDTO saveFile(MultipartFile file) throws IOException {
//        String projectPath = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\files";
//        UUID uuid = UUID.randomUUID();
//        String fileName = uuid + "_" + StringUtils.cleanPath(file.getOriginalFilename());
//
//        File fileEntity = File.builder()
//                .path(projectPath)
//                .name(fileName)
//                .size(file.getSize())
//                .isDeleted(DeletedStatus.NOT_DELETED)
//                .build();
//
//        file.transferTo(new java.io.File(fileEntity.getPath() + "\\" + fileEntity.getName()));
//
//        File savedFile = fileRepository.save(fileEntity);
//        return FileDTO.builder()
//                .id(savedFile.getId())
//                .fileName(savedFile.getName())
//                .filePath(savedFile.getPath())
//                .fileSize(savedFile.getSize())
//                .build();
//    }
//    @Transactional
//    public FileDTO getFile(Long id) {
//        File file = fileRepository.findById(id).get();
//
//        FileDTO fileDTO = FileDTO.builder()
//                .id(id)
//                .fileName(file.getName())
//                .filePath(file.getPath())
//                .fileSize(file.getSize())
//                .build();
//        return fileDTO;
//    }
//    public void saveFileCon(Long boardId, Long fileId) {
//        Optional<Board> optionalBoard = boardRepository.findById(boardId);
//        Board board = optionalBoard.orElseThrow(() -> new IllegalArgumentException("Board not found with id: " + boardId));
//
//        Optional<File> optionalFile = fileRepository.findById(fileId);
//        File file = optionalFile.orElseThrow(() -> new IllegalArgumentException("File not found with id: " + fileId));
//
//        FileCon fileCon = new FileCon();
//        fileCon.setBoard(board);
//        fileCon.setFile(file);
//
//        fileConRepository.save(fileCon);
//    }

}
