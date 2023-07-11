package likelion15.mutsa.service;

import likelion15.mutsa.dto.JoinDto;
import likelion15.mutsa.entity.Profile;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import likelion15.mutsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JoinService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;


    public void createUser(){ // for test
        User user = User.builder()
                .realName("김다미")
                .name("미미")
                .email("mimi@gmail.com")
                .password(passwordEncoder.encode("1234"))
                .phoneNumber("01012341234")
                .auth(UserAuth.USER)
                .profile(Profile.builder().build())
                .status(UserStatus.U)
                .build();

        repository.save(user);
    }
    // 회원가입 - 유저 객체 추가
    public User joinUser(JoinDto joinDto) {

        log.info(joinDto.toString());

        User user = User.builder()
                .name(joinDto.getName())
                .realName(joinDto.getRealName())
                .email(joinDto.getEmail())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .phoneNumber(joinDto.getPhoneNumber())
                .auth(UserAuth.USER)
                .profile(Profile.builder().build())
                .status(UserStatus.U)
                .build();

        repository.save(user);
        log.info("{}님 유저정보 저장 완료", joinDto.getRealName());
        return user;
    }

    // 회원가입-중복 이메일 검사, 로그인-이메일 존재 여부 검사
    public boolean IsExistEmail(String email) {
        return repository.existsByEmail(email);
    }
    // 중복 닉네임 검사
    public boolean checkUsernameDuplicate(String name) {
        return repository.existsByName(name);
    }


}
