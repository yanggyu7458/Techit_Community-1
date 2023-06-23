package likelion15.mutsa.service;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


//    private final List<User> userList = new ArrayList<>();
    private final UserRepository repository;

    // 회원가입 - 유저 객체 추가
    public User joinUser(User user) {

        repository.save(user);
        System.out.println(user.getId());
//        userList.add(user);
        return user;
    }

    public Long login(String email, String password) {
        User user = repository.findByEmail(email);
        if (user== null){
            System.out.println("존재하지 않는 아이디입니다.");
        }
        else if(user.getPassword().equals(password)){
            System.out.println("로그인 성공");
            return user.getId();
        }else{
            System.out.println("비밀번호를 다시 확인해주세요.");
        }
        return null;
    }
    //중복 이메일 검사
    public boolean isEmailExists(String email) {
        User user = repository.findByEmail(email);
        return user != null;
    }
}
