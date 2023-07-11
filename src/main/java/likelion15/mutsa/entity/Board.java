package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.embedded.Content;
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
public class Board extends BaseEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @Embedded
    private Content content;

    @Column(name = "view_count")
    private int viewCount;


    @ManyToOne(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinColumn(name = "user_id",
            foreignKey = @ForeignKey(name = "FK_BOARD_USER")
    )
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "FK_BOARD_CATEGORY")
    )
    private Category category;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Likes> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "board")
    private List<UserBoardTag> userBoardTags = new ArrayList<>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<FileCon> fileCon = new ArrayList<>();

    @OneToOne(cascade = CascadeType.PERSIST)
    private File file;

    public Long getId() {
        return id;
    }

    //좋아요
    public void addLikes(Likes like) {
        this.likes.add(like);
        like.setBoard(this);
    }
    //좋아요 취소
    public void removeLikes(Likes like) {
        this.likes.remove(like);
        like.setBoard(null);
    }

    public void removeFileCon(FileCon fileCon) {
        this.fileCon.remove(fileCon);
        fileCon.setBoard(null);
    }
    public int getLikesCount() {
        return likes != null ? likes.size() : 0;
    }

}