package likelion15.mutsa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDto {
    private String password;
    private String newPassword;
    private String newPasswordCheck;
}
