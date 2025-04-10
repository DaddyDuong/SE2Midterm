package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.Product;
import fit.se2.SE2Midterm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        // Get deal products (products with discount)
        List<Product> deals = productService.getDeals();
        model.addAttribute("deals", deals);

        // Get regular products (without discount)
        List<Product> regularProducts = productService.getRegularProducts();
        model.addAttribute("regularProducts", regularProducts);
        
        // Get all products to display on homepage
        List<Product> allProducts = productService.getAllProducts();
        model.addAttribute("products", allProducts);
        model.addAttribute("featuredProducts", allProducts);
        
        // Get fashion products (Men's Clothing, Women's Clothing, Accessories)
        List<String> fashionCategories = Arrays.asList("Men's Clothing", "Women's Clothing", "Accessories");
        List<Product> fashionProducts = allProducts.stream()
                .filter(product -> fashionCategories.contains(product.getProductType()))
                .collect(Collectors.toList());
        model.addAttribute("fashionProducts", fashionProducts);

        return "Home/index";
    }

    @GetMapping("/product/{id}")
    public String redirectProductDetail(@PathVariable Long id) {
        return "redirect:/products/" + id;
    }
}