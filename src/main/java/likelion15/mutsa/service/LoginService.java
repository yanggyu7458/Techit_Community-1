package likelion15.mutsa.service;

import likelion15.mutsa.dto.SessionDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.repository.UserRepos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserRepos repository;

    // 로그인 검사
    public Long login(String email, String password) {
        User user = repository.findByEmail(email);
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

    public SessionDto createSessionDto(User user) {

        UUID uuid = UUID.randomUUID();

        SessionDto sessionDto = SessionDto.builder()
                .uuid(uuid.toString())
                .name(user.getName())
                .realName(user.getRealName())
                .email(user.getEmail())
                .password(user.getPassword())
                .phoneNumber(user.getPhoneNumber())
                .auth(UserAuth.USER)
                .status(UserStatus.U)
                .build();
        // 프로필은 제외했음.
        return sessionDto;
    }
}
