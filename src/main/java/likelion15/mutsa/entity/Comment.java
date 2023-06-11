package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user-id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board-id")
    private Board board;

    private String content;

    public void setUser(User user) { // 연관관계 편의 메서드
        this.user = user;
        user.getComments().add(this);
    }

    public void setBoard(Board board) { // 연관관계 편의 메서드
        this.board = board;
        board.getComments().add(this);
    }





}
