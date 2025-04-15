package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.Product;
import fit.se2.SE2Midterm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Arrays;
import java.util.Map;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/list";
    }

    @GetMapping("/deals")
    public String getDeals(Model model) {
        List<Product> deals = productService.getDeals();
        model.addAttribute("products", deals);
        model.addAttribute("title", "Deals of the Month");
        return "product/list";
    }

    @GetMapping("/type")
    public String getProductsByType(@RequestParam("type") String productType, Model model) {
        List<Product> products = productService.getProductsByType(productType);
        model.addAttribute("products", products);
        model.addAttribute("title", productType);
        return "product/list";
    }

    @GetMapping("/filter")
    public String filterProducts(
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "type", required = false) String productType,
            Model model) {
        
        List<Product> filteredProducts;
        if (productType != null && !productType.isEmpty()) {
            filteredProducts = productService.filterByPriceRangeAndType(minPrice, maxPrice, productType);
        } else {
            filteredProducts = productService.filterByPriceRange(minPrice, maxPrice);
        }
        
        model.addAttribute("products", filteredProducts);
        
        // Xử lý tiêu đề tốt hơn
        String title;
        if (minPrice == null && maxPrice == null) {
            title = "All Products";
        } else if (minPrice == null) {
            title = String.format("Products up to $%.2f", maxPrice);
        } else if (maxPrice == null) {
            title = String.format("Products from $%.2f", minPrice);
        } else {
            title = String.format("Products $%.2f - $%.2f", minPrice, maxPrice);
        }
        
        model.addAttribute("title", title);
        
        // Add the filter values back to the model for form persistence
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("selectedType", productType);
        
        return "product/list";
    }

    @GetMapping("/list")
    public String getProductsList(Model model) {
        List<Product> listProducts = productService.getAllProducts();
        model.addAttribute("products", listProducts);
        return "product/list";
    }
    
    @GetMapping("/fashion")
    public String getFashionProducts(Model model) {
        // Tạo danh sách các sản phẩm fashion từ 3 danh mục con
        List<Product> menClothing = productService.getProductsByType("Men's Clothing");
        List<Product> womenClothing = productService.getProductsByType("Women's Clothing");
        List<Product> accessories = productService.getProductsByType("Accessories");
        
        // Tổng hợp tất cả sản phẩm thời trang
        List<Product> allFashion = new ArrayList<>();
        allFashion.addAll(menClothing);
        allFashion.addAll(womenClothing);
        allFashion.addAll(accessories);
        
        // Loại bỏ các sản phẩm trùng lặp
        List<Product> uniqueFashion = removeDuplicatesByTitle(allFashion);
        
        model.addAttribute("products", uniqueFashion);
        model.addAttribute("title", "Fashion Products");
        return "product/list";
    }
    
    @GetMapping("/{id}")
    public String getProductDetail(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product != null) {
            model.addAttribute("product", product);
            // Fetch related products (using products of the same type as related)
            List<Product> relatedProducts = productService.getProductsByType(product.getProductType());
            // Loại bỏ sản phẩm hiện tại khỏi danh sách sản phẩm liên quan
            relatedProducts = relatedProducts.stream()
                .filter(p -> !p.getId().equals(product.getId()))
                .collect(Collectors.toList());
            // Limit to 4 related products
            if (relatedProducts.size() > 4) {
                relatedProducts = relatedProducts.subList(0, 4);
            }
            model.addAttribute("relatedProducts", relatedProducts);
            return "product/detail";
        }
        return "redirect:/products/list";
    }
    
    @GetMapping("/search/suggest")
    @ResponseBody
    public ResponseEntity<List<String>> suggestProducts(@RequestParam("query") String query) {
        // Lấy các sản phẩm phù hợp với từ khóa tìm kiếm
        List<String> suggestions = productService.getAllProducts().stream()
                .filter(product -> product.getTitle().toLowerCase().contains(query.toLowerCase()))
                .map(Product::getTitle)
                .distinct()
                .limit(10) // Giới hạn số lượng gợi ý
                .collect(Collectors.toList());
        
        return ResponseEntity.ok(suggestions);
    }

    @GetMapping("/count-by-category")
    @ResponseBody
    public ResponseEntity<Map<String, Long>> countProductsByCategory() {
        List<Product> allProducts = productService.getAllProducts();
        
        // Đếm số lượng sản phẩm trong mỗi danh mục
        Map<String, Long> categoryCounts = allProducts.stream()
            .collect(Collectors.groupingBy(
                Product::getProductType,
                Collectors.counting()
            ));
        
        return ResponseEntity.ok(categoryCounts);
    }
    
    /**
     * Helper method để loại bỏ sản phẩm trùng lặp dựa trên tiêu đề
     * @param products Danh sách sản phẩm cần lọc
     * @return Danh sách sản phẩm không trùng lặp
     */
    private List<Product> removeDuplicatesByTitle(List<Product> products) {
        Set<String> seenTitles = new HashSet<>();
        List<Product> uniqueProducts = new ArrayList<>();
        
        for (Product product : products) {
            if (!seenTitles.contains(product.getTitle())) {
                uniqueProducts.add(product);
                seenTitles.add(product.getTitle());
            }
        }
        
        return uniqueProducts;
    }
}