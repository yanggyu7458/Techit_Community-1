package likelion15.mutsa.service;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepos repository;

    // 로그인 검사
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
}
