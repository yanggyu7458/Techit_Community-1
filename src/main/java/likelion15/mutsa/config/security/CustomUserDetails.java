package likelion15.mutsa.config.security;

import likelion15.mutsa.entity.Profile;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.entity.enums.UserAuth;
import likelion15.mutsa.entity.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
@Slf4j
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomUserDetails implements UserDetails {
    // 로그인했을 때 정보들
// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면 UserDetails타입의 오브젝트를
// 스프링 시큐리티의 고유한 세션저장소에 저장을 해준다. CustomUserDetails
    private String email;
    private String realName;
    private String name;
    private String phoneNumber;
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Collection<GrantedAuthority> authorities = new ArrayList<>();
//
//        // 사용자의 권한 정보를 추가합니다.
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER")); // 예시로 "ROLE_USER" 권한을 추가합니다.

//        return authorities;
        return null;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        // username으로 아이디가 되서 일단 email로 설정
        return email;
    }

    @Override
    public boolean isAccountNonExpired() { //계정이 만료되지 않았는지 리턴 (true: 만료안됨)
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { //계정이 잠겨있는지 않았는지 리턴. (true:잠기지 않음)
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {//비밀번호가 마료되지 않았는지 리턴한다. (true:만료안됨)
        return true;
    }

    @Override
    public boolean isEnabled() {//계정이 활성화(사용가능)인지 리턴 (true:활성화)
        return true;
    }

    public String getRealName() {
        return realName;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public static CustomUserDetails fromEntity(User entity) {
        log.info(entity.getEmail());

        CustomUserDetails details = CustomUserDetails.builder()
                .email(entity.getEmail())
                .realName(entity.getRealName())
                .name(entity.getName())
                .phoneNumber(entity.getPhoneNumber())
                .password(entity.getPassword())
                .build();
        return details;
    }
    public User newEntity(){
        User entity = User.builder()
                .email(email)
                .realName(realName)
                .name(name)
                .phoneNumber(phoneNumber)
                .password(password)
                .auth(UserAuth.USER)
                .profile(Profile.builder().build())
                .status(UserStatus.U)
                .build();

        return entity;

    }
}

