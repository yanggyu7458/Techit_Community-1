package likelion15.mutsa.service;

import likelion15.mutsa.repository.LikesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final LikesRepository likesRepository;

    // comment 좋아요 갯수 반환
    public int getCntCommentLikes(Long commentId) {
        return likesRepository.countLikesByComment_Id(commentId);
    }

    // board 좋아요 갯수 반환
    public int getCntBoardLikes(Long boardId) {
        return likesRepository.countLikesByBoard_Id(boardId);
    }
}
