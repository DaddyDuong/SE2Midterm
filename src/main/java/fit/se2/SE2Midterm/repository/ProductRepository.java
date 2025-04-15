package fit.se2.SE2Midterm.repository;

import fit.se2.SE2Midterm.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Find products with discount (for deals section)
    List<Product> findByDiscountPercentageIsNotNull();

    // Find products without discount (for shop now section)
    List<Product> findByDiscountPercentageIsNull();

    // Find products by type
    List<Product> findByProductType(String productType);
    
    // Find products by title containing the search query (case insensitive)
    @Query("SELECT p FROM Product p WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Product> findByTitleContainingIgnoreCase(@Param("query") String query);
    
    // Find products by price range
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
    
    // Find products by price range and type
    @Query("SELECT p FROM Product p WHERE p.price BETWEEN :minPrice AND :maxPrice AND p.productType = :productType")
    List<Product> findByPriceRangeAndType(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("productType") String productType);
}