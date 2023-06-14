package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    private String name;

    private String path;

    @Enumerated(EnumType.STRING)
    private VisibleStatus isVisible;

    @Enumerated(EnumType.STRING)
    private UserStatus auth;

    @OneToOne(fetch = FetchType.LAZY,
            mappedBy = "category"
    )
    private Board board;
}
