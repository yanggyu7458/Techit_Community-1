package likelion15.mutsa.repository;


import likelion15.mutsa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email); // 이메일 중복 가입 방지
    boolean existsByName(String name); // 닉네임 중복확인을 위해 존재여부 확인
}
