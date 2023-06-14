package likelion15.mutsa.service;

import jakarta.transaction.Transactional;
import likelion15.mutsa.dto.BoardDTO;
import likelion15.mutsa.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    @Transactional
    public int updateCount(Long id) {
        return boardRepository.updateCount(id);
    }

    private final List<BoardDTO> boardList = new ArrayList<>();

    private Long nextId = 1L;
    private Integer count;

//    public BoardService(){
//        createBoard("alex", "alex@gmail.com");
//        createBoard("brad", "brad@gmail.com");
//        createBoard("chad", "chad@gmail.com");
//    }
    public List<BoardDTO> readBoardAll() {
        return boardList;
    }
    public BoardDTO readBoard(Long id) {
        for (BoardDTO boardDTO: boardList) {
            if (boardDTO.getId().equals(id)) {
                return boardDTO;
            }
        }
        return null;
    }

    public BoardDTO createBoard(String title, String content, Integer count) {
        BoardDTO newBoard = new BoardDTO(
                nextId, title, content, count
        );
        nextId++;
        boardList.add(newBoard);
        return newBoard;
    }

    public BoardDTO updateBoard(Long id, String title, String content) {
        int target = -1;
        for (int i = 0; i < boardList.size(); i++) {
            //id가 동일한 studentDTO를 찾았으면
            if(boardList.get(i).getId().equals(id)) {
                //인덱스 기록
                target = i;
                //반복 종료
                break;
            }
        }
        if(target != -1) {
            boardList.get(target).setTitle(title);
            boardList.get(target).setContent(content);
            return boardList.get(target);
        } else return null;

    }

    public boolean deleteBoard(Long id) {
        int target = -1;
        //게시글 리스트를 살펴보기
        for (int i = 0; i < boardList.size(); i++) {
            if (boardList.get(i).getId().equals(id)) {
                target = i;
                break;
            }
        }

        //검색 성공시
        if(target != -1) {
            boardList.remove(target);
            return true;
        } else return false;
    }


}
