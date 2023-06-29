package likelion15.mutsa.service;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserRepos repository;

    // 로그인 검사
    public Long login(String email, String password) {
        User user = repository.findByEmail(email);
        // 테스트를 위해 임시로 다시 부활
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
    // userId를 통해 User반환
    public User getLoginUser(Long userId) {
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }else{
            return null;
        }

    }
}
