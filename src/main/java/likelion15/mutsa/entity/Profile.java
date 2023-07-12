package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
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
public class Profile extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    private String content;

    @OneToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_PROFILE_USER"))
    private User user;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "profile"
    )
    private List<FileCon> fileCon = new ArrayList<>();

    public Profile updateProfileContent(String content) {
        this.content = content;
        return this;
    }

}
