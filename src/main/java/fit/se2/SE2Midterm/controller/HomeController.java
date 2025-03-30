package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.Product;
import fit.se2.SE2Midterm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

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

        return "Home/index";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return "redirect:/";
        }

        model.addAttribute("product", product);
        return "product/detail";
    }
}