package likelion15.mutsa.service;

import jakarta.transaction.Transactional;
import likelion15.mutsa.dto.CommentDTO;
import likelion15.mutsa.entity.Board;
import likelion15.mutsa.entity.Comment;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.repository.BoardRepository;
import likelion15.mutsa.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final List<CommentDTO> commentList = new ArrayList<>();
    @Transactional
    public Comment createComment(CommentDTO commentDTO, User loginUser) {
        Board board = boardRepository.findById(commentDTO.getBoardId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + commentDTO.getBoardId()));

        Comment comment = Comment.builder()
                .id(commentDTO.getId())
                .comment(commentDTO.getComment())
                .username(commentDTO.getUsername())
                .createdBy(loginUser.getRealName())
                .isDeleted(DeletedStatus.NOT_DELETED)
                .board(board)
                .build();
        board.getComments().add(comment); // 댓글과 게시글의 관계 설정
        //boardRepository.save(board);

        commentList.add(commentDTO); // CommentDTO를 commentList에 추가합니다.

        return commentRepository.save(comment);
    }
    public List<CommentDTO> readAllComments(Long boardId) {
        List<Comment> comments = commentRepository.findByBoardId(boardId);
        List<CommentDTO> commentList = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO commentDTO = CommentDTO.fromEntity(comment);
            commentList.add(commentDTO);
        }

        return commentList;
    }
    public CommentDTO updateComment(Long commentId, CommentDTO dto, User loginUser) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        Comment comment;
        if(optionalComment.isPresent()) {
            comment = optionalComment.get();
            if(!loginUser.getName().equals(comment.getUsername())) {
                return null;
            }
            comment.setComment(dto.getComment());
            return CommentDTO.fromEntity(commentRepository.save(comment));
        } else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    public void deleteComment(Long commentId, User loginUser) {
        Optional<Comment> optionalComment = commentRepository.findById(commentId);
        if(optionalComment.isPresent()) {
            Comment comment = optionalComment.get();
            if (comment.getUsername().equals(loginUser.getName())) {
                commentRepository.deleteById(commentId);
            } else throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        else throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

}
