package likelion15.mutsa.service;

import likelion15.mutsa.dto.SessionDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserRepository repository;

    // 로그인 검사
    public Long login(String email, String password) {
        // 우선 null값으로 설정하고 에러에 대한 것은 spring security적용 후
        // 그것으로 처리하면 될듯
        User user = repository.findByEmail(email).orElse(null);

        if (user== null){ // 아이디가 존재하지 않는 경우
            return null;
        }
        else if(user.getPassword().equals(password)){ // 로그인 성공
            return user.getId();
        }else{ // 비밀번호가 일치하지 않는 경우
            return null;
        }
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
