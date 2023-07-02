package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.YesOrNo;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Likes extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private YesOrNo isLike;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeletedStatus isDeleted;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_LIKES_USER"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "FK_LIKES_BOARD"))
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "comment_id", foreignKey = @ForeignKey(name = "FK_LIKES_COMMENT"))
    private Comment comment;

    // 좋아요 -> 좋아요 취소, 노 좋아요 -> 좋아요
    public Likes updateLikesYesOrNo(YesOrNo isLike) {
        this.isLike = isLike;
        return this;
    }
}
