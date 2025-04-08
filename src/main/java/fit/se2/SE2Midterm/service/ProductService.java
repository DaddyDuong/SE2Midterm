package fit.se2.SE2Midterm.service;

import fit.se2.SE2Midterm.model.Product;
import fit.se2.SE2Midterm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    /**
     * Initialize product image paths
     */
    @PostConstruct
    public void initializeProductImages() {
        List<Product> products = productRepository.findAll();
        boolean needsUpdate = false;

        for (Product product : products) {
            if (product.getImagePath() == null || product.getImagePath().isEmpty()) {
                // Sử dụng trực tiếp tên sản phẩm làm tên file
                String imagePath = "/images/products/" + product.getTitle() + ".jpg";
                product.setImagePath(imagePath);
                needsUpdate = true;
            }
        }

        if (needsUpdate) {
            productRepository.saveAll(products);
        }
    }

    /**
     * Get all products
     * @return List of all products
     */
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Get product by ID
     * @param id Product ID
     * @return Product if found, otherwise null
     */
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    /**
     * Get products with discounts (deals)
     * @return List of products with discount
     */
    public List<Product> getDeals() {
        return productRepository.findByDiscountPercentageIsNotNull();
    }

    /**
     * Get regular products (no discount)
     * @return List of products with no discount
     */
    public List<Product> getRegularProducts() {
        return productRepository.findByDiscountPercentageIsNull();
    }

    /**
     * Get products by type
     * @param productType Type of product
     * @return List of products of the specified type
     */
    public List<Product> getProductsByType(String productType) {
        return productRepository.findByProductType(productType);
    }
}