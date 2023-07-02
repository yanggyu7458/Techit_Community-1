package likelion15.mutsa.service;

import likelion15.mutsa.repository.BoardRepository;
import likelion15.mutsa.repository.FileConRepository;
import likelion15.mutsa.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FileService {
    private FileRepository fileRepository;
    private FileConRepository fileConRepository;
    @Autowired
    private BoardRepository boardRepository;


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
