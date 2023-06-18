package likelion15.mutsa.service;

import likelion15.mutsa.dto.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private Long nextId = 1L;
    private final List<User> userList = new ArrayList<>();

    // 회원가입 - 유저 객체 추가
    public User joinUser(String username, String email, String password, String phoneNumber) {
        User user = new User(nextId,username,email,password,phoneNumber);
        nextId++;
        userList.add(user);
        return user;
    }

    // 해당하는 id의 객체를 찾아 반환
    public User readUser(Long id) {
        for (User user : userList) {
            if(user.getId().equals(id))
                return user;
        }
        return null;
    }

    // email,password정보에 해당하는 id를 반환
    public Long login(String email, String password) {
        for (User user : userList) {
            if(user.getEmail().equals(email)){
                if(user.getPassword().equals(password))
                    return user.getId();
            }
        }
        return null;
    }

}
