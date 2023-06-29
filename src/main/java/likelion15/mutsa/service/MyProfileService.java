package likelion15.mutsa.service;

import likelion15.mutsa.dto.PasswordDto;
import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MyProfileService {
    private final UserRepository userRepository;
    @Transactional
    public User join(User user) {return userRepository.save(user);}

    public User readOne(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }



    @Transactional
    public Long updateName(Long userId, String userName) {

        User oldUser = userRepository.findById(userId).get();

        userRepository.save(
                User.builder()
                        .id(oldUser.getId())
                        .realName(oldUser.getRealName())
                        .name(userName)
                        .email(oldUser.getEmail())
                        .password(oldUser.getPassword())
                        .phoneNumber(oldUser.getPhoneNumber())
                        .status(oldUser.getStatus())
                        .auth(oldUser.getAuth())
                        .build()
        );
        return userRepository.findById(userId).get().getId();
    }

    @Transactional
    public Long updateEmail(Long userId, String email) {
        User oldUser = userRepository.findById(userId).get();

        userRepository.save(
                User.builder()
                        .id(oldUser.getId())
                        .realName(oldUser.getRealName())
                        .name(oldUser.getName())
                        .email(email)
                        .password(oldUser.getPassword())
                        .phoneNumber(oldUser.getPhoneNumber())
                        .status(oldUser.getStatus())
                        .auth(oldUser.getAuth())
                        .build()
        );
        return userRepository.findById(userId).get().getId();
    }

    @Transactional
    public Long updatePhoneNumber(Long userId, String phoneNumber) {
        User oldUser = userRepository.findById(userId).get();

        userRepository.save(
                User.builder()
                        .id(oldUser.getId())
                        .realName(oldUser.getRealName())
                        .name(oldUser.getName())
                        .email(oldUser.getEmail())
                        .password(oldUser.getPassword())
                        .phoneNumber(phoneNumber)
                        .status(oldUser.getStatus())
                        .auth(oldUser.getAuth())
                        .build()
        );
        return userRepository.findById(userId).get().getId();
    }

    @Transactional
    public Long updatePassword(Long userId, PasswordDto passwordDto) {
        User oldUser = userRepository.findById(userId).get();
        if (
                oldUser.getPassword().equals(passwordDto.getPassword()) &&
                !passwordDto.getPassword().equals(passwordDto.getNewPassword()) &&
                passwordDto.getNewPassword().equals(passwordDto.getNewPasswordCheck())
        ) {
            userRepository.save(
                    User.builder()
                            .id(oldUser.getId())
                            .realName(oldUser.getRealName())
                            .name(oldUser.getName())
                            .email(oldUser.getEmail())
                            .password(passwordDto.getNewPassword())
                            .phoneNumber(oldUser.getPhoneNumber())
                            .status(oldUser.getStatus())
                            .auth(oldUser.getAuth())
                            .build()
            );
            return userRepository.findById(userId).get().getId();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

    }

}
