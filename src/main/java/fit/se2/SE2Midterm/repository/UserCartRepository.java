package fit.se2.SE2Midterm.repository;

import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.model.UserCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCartRepository extends JpaRepository<UserCart, Long> {
    Optional<UserCart> findByUser(User user);
} 