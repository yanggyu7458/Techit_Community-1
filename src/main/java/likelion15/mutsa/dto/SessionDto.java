package likelion15.mutsa.dto;

import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class SessionDto {
    private String uuid;
    private String name;
    private String email;
    private String realName;
    private String phoneNumber;
    private String password;
    private UserAuth auth;
    private UserStatus status;
}
