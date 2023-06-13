package likelion15.mutsa.entity.embedded;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import likelion15.mutsa.entity.enums.DeletedStatus;
import likelion15.mutsa.entity.enums.VisibleStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Embeddable
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Content {

    @Column(nullable = false)
    private String title;

    @Column(nullable = false,
            columnDefinition = "TEXT"
    )
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private VisibleStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DeletedStatus isDeleted;
}
