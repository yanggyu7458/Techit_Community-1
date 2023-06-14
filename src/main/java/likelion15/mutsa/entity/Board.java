package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.embedded.Content;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Embedded
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "FK_BOARD_USER")
    )
    private User user;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "FK_BOARD_CATEGORY")
    )
    private Category category;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "comment")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "board"
    )
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "board"
    )
    private List<UserBoardTag> userBoardTags = new ArrayList<>();
}
