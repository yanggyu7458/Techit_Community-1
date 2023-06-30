package likelion15.mutsa.service;

import jakarta.transaction.Transactional;
import likelion15.mutsa.dto.FileDTO;
import likelion15.mutsa.entity.File;
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

    @Transactional
    public Long saveFile(FileDTO fileDTO) {
        return fileRepository.save(fileDTO.toEntity()).getId();
    }
    @Transactional
    public FileDTO getFile(Long id) {
        File file = fileRepository.findById(id).get();

        FileDTO fileDTO = FileDTO.builder()
                .id(id)
                .fileName(file.getName())
                .filePath(file.getPath())
                .fileSize(file.getSize())
                .build();
        return fileDTO;
    }
//    public FileCon saveFileCon(FileConDTO fileConDTO) {
//        Optional<Board> optionalBoard = boardRepository.findById(fileConDTO.getBoardId());
//        Board board = optionalBoard.orElse(null);
//        FileCon fileCon = new FileCon();
//        fileCon.setBoard(board);
//
//        return fileConRepository.save(fileCon);
//    }

}
