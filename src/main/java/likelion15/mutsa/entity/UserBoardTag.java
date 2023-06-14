package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.enums.DeletedStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class UserBoardTag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_board_tag_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private DeletedStatus isDeleted;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "tag_id", foreignKey = @ForeignKey(name = "FK_USER_BOARD_TAG_TAG"))
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_USER_BOARD_TAG_USER"))
    private User user;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "FK_USER_BOARD_TAG_BOARD"))
    private Board board;

}
