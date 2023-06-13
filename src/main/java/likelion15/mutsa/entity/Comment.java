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
}
