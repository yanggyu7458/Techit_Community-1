package likelion15.mutsa.service;

import likelion15.mutsa.dto.CommentDTO;
import likelion15.mutsa.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final BoardRepository boardRepository;
    private final List<CommentDTO> commentList = new ArrayList<>();
    private Long commentId = 1L;
    public CommentDTO createComment(Long boardId, String comment) {
        //Optional<Board> optionalBoardDTO = boardRepository.findById(boardId);
        //optionalBoardDTO.orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id= " + boardId));
        CommentDTO commentDTO = new CommentDTO(
                commentId, comment
        );
        commentId++;
        commentList.add(commentDTO);
        log.info(commentDTO.toString());

        return commentDTO;
    }
    public List<CommentDTO> readCommentAll() {
        return commentList;
    }
    public CommentDTO readComment(Long id) {
        for (CommentDTO commentDTO: commentList) {
            if(commentDTO.getId().equals(id)) {
                return commentDTO;
            }
        }
        return null;
    }
    public CommentDTO updateComment(Long id, String comment) {
        int target = -1;
        for (int i = 0; i < commentList.size(); i++) {
            //id가 동일한 studentDTO를 찾았으면
            if(commentList.get(i).getId().equals(id)) {
                //인덱스 기록
                target = i;
                //반복 종료
                break;
            }
        }
        if(target != -1) {
            commentList.get(target).setComment(comment);
            return commentList.get(target);
        } else return null;
    }


}
