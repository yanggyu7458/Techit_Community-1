package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUser_Id(Long id);
    Optional<Profile> findByUser_Name(String userName);
}
