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

    @GetMapping("/list")
    public String getProductsList(Model model) {
        List<Product> listProducts = productService.getAllProducts();
        model.addAttribute("products", listProducts);
        return "product/list";
    }
    
    @GetMapping("/fashion")
    public String getFashionProducts(Model model) {
        List<String> fashionCategories = Arrays.asList("Men's Clothing", "Women's Clothing", "Accessories");
        List<Product> allProducts = productService.getAllProducts();
        
        System.out.println("Total products: " + allProducts.size());
        System.out.println("Product types: " + allProducts.stream().map(Product::getProductType).distinct().collect(Collectors.toList()));
        
        List<Product> fashionProducts = allProducts.stream()
                .filter(product -> fashionCategories.contains(product.getProductType()))
                .collect(Collectors.toList());
        
        System.out.println("Fashion products: " + fashionProducts.size());
        System.out.println("Fashion product names: " + fashionProducts.stream().map(Product::getTitle).collect(Collectors.toList()));
        
        model.addAttribute("products", fashionProducts);
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
}