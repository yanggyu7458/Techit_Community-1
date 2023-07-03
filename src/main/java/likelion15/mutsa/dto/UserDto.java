package likelion15.mutsa.dto;

import likelion15.mutsa.entity.User;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String realName;
    private String name;
    private String email;
    private String phoneNumber;


    public static UserDto fromEntity(User entity) {
        if (entity == null) {
            return null;  // or throw an exception, depending on your requirement
        }
        UserDto userDto= new UserDto();
        userDto.setId(entity.getId());
        userDto.setRealName(entity.getRealName());
        userDto.setName(entity.getName());
        userDto.setEmail(entity.getEmail());
        userDto.setPhoneNumber(entity.getPhoneNumber());


        return userDto;
    }
}
