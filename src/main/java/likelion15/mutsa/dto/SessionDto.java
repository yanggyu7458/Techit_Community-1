package likelion15.mutsa.dto;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

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

    public SessionDto(User user) {
        UUID uuid = UUID.randomUUID();

        this.uuid = uuid.toString();
        this.name = user.getName();
        this.email = user.getEmail();
        this.realName = user.getRealName();
        this.phoneNumber = user.getPhoneNumber();
        this.password = user.getPassword();
        this.auth = user.getAuth();
        this.status = user.getStatus();
    }
}
