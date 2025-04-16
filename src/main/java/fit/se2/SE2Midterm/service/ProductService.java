package fit.se2.SE2Midterm.service;

import fit.se2.SE2Midterm.model.Product;
import fit.se2.SE2Midterm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

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
        List<Product> products = productRepository.findAll();
        return removeDuplicates(products);
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
        List<Product> deals = productRepository.findByDiscountPercentageIsNotNull();
        return removeDuplicates(deals);
    }

    /**
     * Get regular products (no discount)
     * @return List of products with no discount
     */
    public List<Product> getRegularProducts() {
        List<Product> products = productRepository.findByDiscountPercentageIsNull();
        return removeDuplicates(products);
    }

    /**
     * Get products by type
     * @param productType Type of product
     * @return List of products of the specified type
     */
    public List<Product> getProductsByType(String productType) {
        List<Product> products = productRepository.findByProductType(productType);
        return removeDuplicates(products);
    }
    
    /**
     * Search products by title
     * @param query Search query
     * @return List of products matching the search query
     */
    public List<Product> searchProductsByTitle(String query) {
        List<Product> products = productRepository.findByTitleContainingIgnoreCase(query);
        return removeDuplicates(products);
    }
    
    /**
     * Get search suggestions based on product titles
     * @param query Search query
     * @return List of suggested search terms
     */
    public List<String> getSearchSuggestions(String query) {
        List<Product> products = productRepository.findByTitleContainingIgnoreCase(query);
        List<String> suggestions = new ArrayList<>();
        
        for (Product product : products) {
            if (!suggestions.contains(product.getTitle()) && suggestions.size() < 10) {
                suggestions.add(product.getTitle());
            }
            
            // Extract product type as suggestion if it contains the query
            String type = product.getProductType();
            if (type != null && type.toLowerCase().contains(query.toLowerCase()) 
                    && !suggestions.contains(type) && suggestions.size() < 10) {
                suggestions.add(type);
            }
        }
        
        return suggestions;
    }
    
    /**
     * Helper method to remove duplicate products
     * @param products List of products to deduplicate
     * @return List of unique products
     */
    private List<Product> removeDuplicates(List<Product> products) {
        // Sử dụng LinkedHashMap để duy trì thứ tự và loại bỏ các phần tử trùng lặp dựa trên title
        // Giữ lại phần tử đầu tiên gặp phải với mỗi title
        Map<String, Product> uniqueProducts = new LinkedHashMap<>();
        
        for (Product product : products) {
            String key = product.getTitle();
            if (!uniqueProducts.containsKey(key)) {
                uniqueProducts.put(key, product);
            }
        }
        
        return new ArrayList<>(uniqueProducts.values());
    }

    /**
     * Filter products by price range
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @return List of products within the price range
     */
    public List<Product> filterByPriceRange(Double minPrice, Double maxPrice) {
        if (minPrice == null) minPrice = 0.0;
        if (maxPrice == null) maxPrice = Double.MAX_VALUE;
        List<Product> products = productRepository.findByPriceRange(minPrice, maxPrice);
        return removeDuplicates(products);
    }

    /**
     * Filter products by price range and type
     * @param minPrice Minimum price
     * @param maxPrice Maximum price
     * @param productType Product type
     * @return List of products within the price range and of the specified type
     */
    public List<Product> filterByPriceRangeAndType(Double minPrice, Double maxPrice, String productType) {
        if (minPrice == null) minPrice = 0.0;
        if (maxPrice == null) maxPrice = Double.MAX_VALUE;
        List<Product> products = productRepository.findByPriceRangeAndType(minPrice, maxPrice, productType);
        return removeDuplicates(products);
    }
}