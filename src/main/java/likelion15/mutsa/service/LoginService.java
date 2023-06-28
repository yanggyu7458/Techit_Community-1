package likelion15.mutsa.service;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserRepos repository;

    // 로그인 검사
    public Long login(String email, String password) {
        User user = repository.findByEmail(email);
        return null;
    }
}
