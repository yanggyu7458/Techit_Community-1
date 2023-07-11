package likelion15.mutsa.config.security;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
@Slf4j
@Service
public class JpaUserDetailsManager implements UserDetailsManager {

    private final UserRepository userRepository;

    public JpaUserDetailsManager(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("이메일"+ email);

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if(optionalUser.isEmpty()) {
            log.info("해당 사용자를 못 찾지 못함");
            throw new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. " + email);
        }
        User user = optionalUser.get();

        return CustomUserDetails.fromEntity(user);
    }

    @Override
    public void createUser(UserDetails user) {

        if(this.userExists(user.getUsername())){
            log.info("이미 유저 정보 존재함",user.getUsername());
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        log.info("manager에서 유저 정보 저장 시도 ");
        try {
            this.userRepository.save(((CustomUserDetails) user).newEntity());
            log.info("저장 완료 ");
        } catch (ClassCastException e) {
            log.error("failed to cast to {}",CustomUserDetails.class);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public void updateUser(UserDetails user) {

    }

    @Override
    public void deleteUser(String username) {

    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    @Override
    public boolean userExists(String username) {
        return false;
    }

}
