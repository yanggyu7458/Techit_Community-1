package likelion15.mutsa.entity;

import jakarta.persistence.*;
import likelion15.mutsa.entity.base.BaseEntity;
import likelion15.mutsa.entity.embedded.Content;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class Notice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long id;

    @Embedded
    private Content content;
}
