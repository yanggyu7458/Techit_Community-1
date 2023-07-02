package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@SuperBuilder
public class FileCon extends BaseEntity {
//    public FileCon(Board board) {
//        this.board = board;
//        if (board != null) {
//            board.getFileCon().add(this);
//        }
//    }
    public FileCon(File file) {
        this.file = file;
        if (file != null) {
            file.getFileCon().add(this);
        }
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_con_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "file_id", foreignKey = @ForeignKey(name = "FK_FILE_CON_FILE"))
    private File file;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "profile_id", foreignKey = @ForeignKey(name = "FK_FILE_CON_PROFILE"))
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "board_id", foreignKey = @ForeignKey(name = "FK_FILE_CON_BOARD"))
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "notice_id", foreignKey = @ForeignKey(name = "FK_FILE_CON_NOTICE"))
    private Notice notice;

    public void setFile(File file) {
        this.file = file;
        if (file != null) {
            file.getFileCon().add(this);
        }
    }
}
