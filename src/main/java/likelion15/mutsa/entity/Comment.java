package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.base.BaseTimeEntity;
import likelion15.mutsa.entity.enums.DeletedStatus;
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

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "FK_COMMENT_BOARD"))
    private Board board;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "comment"
    )
    private List<Likes> likes = new ArrayList<>();
}
