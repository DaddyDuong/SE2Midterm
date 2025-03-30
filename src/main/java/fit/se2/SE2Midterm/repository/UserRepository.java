package fit.se2.SE2Midterm.repository;

import fit.se2.SE2Midterm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}