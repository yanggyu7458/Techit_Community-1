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
}
