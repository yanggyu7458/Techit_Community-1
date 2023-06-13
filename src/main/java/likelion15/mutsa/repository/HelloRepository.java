package likelion15.mutsa.repository;

import likelion15.mutsa.entity.Hello;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HelloRepository extends JpaRepository<Hello, Long>, HelloRepositoryCustom{
}
