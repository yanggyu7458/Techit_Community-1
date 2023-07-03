package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
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
public class File extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @Column(nullable = false)
    private String path;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private DeletedStatus isDeleted;

    @Column(nullable = false)
    private Long size;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "file")
    private List<FileCon> fileCon = new ArrayList<>();

    public Long getId() {
        return id;
    }
}
