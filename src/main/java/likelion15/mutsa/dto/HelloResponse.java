package likelion15.mutsa.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class HelloResponse {
    private String name;

    @QueryProjection
    public HelloResponse(String name) {
        this.name = name;
    }
}
