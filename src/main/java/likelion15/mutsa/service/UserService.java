package likelion15.mutsa.service;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long join(User user) {
        userRepository.save(user);
        return user.getId();
    }

    public User findOne(Long id) {
        return userRepository.findOne(id);
    }

    @Transactional
    public Long updateName(Long id, String userName) {
        User user = userRepository.findOne(id);
        user.setName(userName);
        userRepository.update(user);
        return userRepository.findOne(id).getId();
    }

    @Transactional
    public Long updateEmail(Long id, String email) {
        User user = userRepository.findOne(id);
        user.setEmail(email);
        userRepository.update(user);
        return userRepository.findOne(id).getId();
    }

    @Transactional
    public Long updatePhoneNumber(Long id, String phoneNumber) {
        User user = userRepository.findOne(id);
        user.setPhoneNumber(phoneNumber);
        userRepository.update(user);
        return userRepository.findOne(id).getId();
    }

    @Transactional
    public Long updatePassword(Long id, String password, String newPassword, String newPasswordCheck) {
        User user = userRepository.findOne(id);
        if (user.getPassword().equals(password) && newPassword.equals(newPasswordCheck)) {
            user.setPassword(newPassword);
            userRepository.update(user);
            return userRepository.findOne(id).getId();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
