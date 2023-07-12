package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.enums.DeletedStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
//    @Id
//    @Column(name = "comment_id")
//    private Long id; // comment_id 수동으로 설정

    private Long pid;

    @Column(nullable = false,
            columnDefinition = "TEXT"
    )
    private String comment;

    //@Column(nullable = false)
    private String username;

    @Enumerated(EnumType.STRING)
    private DeletedStatus isDeleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "FK_COMMENT_BOARD"))
    private Board board;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "comment"
    )
    private List<Likes> likes = new ArrayList<>();
}