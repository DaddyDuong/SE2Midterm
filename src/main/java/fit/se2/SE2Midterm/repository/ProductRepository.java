package fit.se2.SE2Midterm.repository;

import fit.se2.SE2Midterm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products with discount (for deals section)
    List<Product> findByDiscountPercentageIsNotNull();

    // Find products without discount (for shop now section)
    List<Product> findByDiscountPercentageIsNull();

    // Find products by type
    List<Product> findByProductType(String productType);
}