package likelion15.mutsa.entity.Board;

import likelion15.mutsa.entity.base.BaseTimeEntity;

public class BoardEntity {
    private Long id;
    private String title;
    private String content;
    private String status;
    private boolean isDeleted;
    public BoardEntity(Long id, String title, String content, String status, boolean isDeleted) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.status = status;
        this.isDeleted = isDeleted;
    }
}
