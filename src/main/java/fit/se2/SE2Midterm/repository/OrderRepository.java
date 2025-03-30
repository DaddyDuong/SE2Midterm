package fit.se2.SE2Midterm.repository;

import fit.se2.SE2Midterm.model.Order;
import fit.se2.SE2Midterm.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    List<Order> findByUserOrderByCreatedAtDesc(User user);
}