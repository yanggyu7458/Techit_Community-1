package likelion15.mutsa.repository;


import likelion15.mutsa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;



public interface UserRepository extends JpaRepository<User, Long> {}
