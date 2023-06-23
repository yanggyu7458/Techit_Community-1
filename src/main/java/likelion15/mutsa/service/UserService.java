package likelion15.mutsa.service;

import likelion15.mutsa.entity.User;
import likelion15.mutsa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
