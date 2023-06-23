package likelion15.mutsa.service;

import likelion15.mutsa.dto.CommentDTO;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.repository.BoardRepository;
import likelion15.mutsa.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final List<CommentDTO> commentList = new ArrayList<>();
    public Comment createComment(CommentDTO commentDTO) {
        Board board = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + commentDTO.getBoardId()));

        Comment comment = Comment.builder()
                .pid(commentDTO.getPid())
                .comment(commentDTO.getComment())
                .username(commentDTO.getUsername())
                .isDeleted(DeletedStatus.NOT_DELETED)
                .board(board)
                .build();

        commentList.add(commentDTO); // CommentDTO를 commentList에 추가합니다.

        return commentRepository.save(comment);
    }
    public List<CommentDTO> readCommentAll() {
        return commentList;
    }
//    public CommentDTO readComment(Long id) {
//        for (CommentDTO commentDTO: commentList) {
//            if(commentDTO.getId().equals(id)) {
//                return commentDTO;
//            }
//        }
//        return null;
//    }
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
