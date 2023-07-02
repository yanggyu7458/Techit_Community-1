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
    public User join(User user) {
        return userRepository.save(user);
    }

    public User readOne(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional
    public Long updateRealName(Long userId, String realName) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.updateRealName(realName);
        return userRepository.save(oldUser).getId();
    }

    @Transactional
    public Long updateName(Long userId, String name) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.updateName(name);
        return userRepository.save(oldUser).getId();
    }

    @Transactional
    public Long updateEmail(Long userId, String email) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.updateEmail(email);
        return userRepository.save(oldUser).getId();
    }

    @Transactional
    public Long updatePhoneNumber(Long userId, String phoneNumber) {
        User oldUser = userRepository.findById(userId).get();
        oldUser.updatePhoneNumber(phoneNumber);
        return userRepository.save(oldUser).getId();
    }

    @Transactional
    public Long updatePassword(Long userId, PasswordDto passwordDto) {
        User oldUser = userRepository.findById(userId).get();
        if (
                oldUser.getPassword().equals(passwordDto.getPassword()) &&
                        !passwordDto.getPassword().equals(passwordDto.getNewPassword()) &&
                        passwordDto.getNewPassword().equals(passwordDto.getNewPasswordCheck())
        ) {
            oldUser.updatePassword(passwordDto.getNewPassword());
            return userRepository.save(oldUser).getId();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
