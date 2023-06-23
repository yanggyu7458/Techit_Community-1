package likelion15.mutsa.service;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepos repository;

    // 회원가입 - 유저 객체 추가
    public User joinUser(User user) {

        repository.save(user);
        System.out.println(user.getId());
//        userList.add(user);
        return user;
    }

    //중복 이메일 검사
    public boolean isEmailExists(String email) {
        User user = repository.findByEmail(email);
        return user != null;
    }
}
