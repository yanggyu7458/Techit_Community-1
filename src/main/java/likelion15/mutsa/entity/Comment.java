package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.base.BaseTimeEntity;
import likelion15.mutsa.entity.enums.DeletedStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private Long pid;

    @Column(nullable = false,
            columnDefinition = "TEXT"
    )
    private String comment;

    @Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private DeletedStatus isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user-id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board-id")
    private Board board;

    public void setUser(User user) { // 연관관계 편의 메서드
        this.user = user;
        user.getComments().add(this);
    }

    public void setBoard(Board board) { // 연관관계 편의 메서드
        this.board = board;
        board.getComments().add(this);
    }
}
