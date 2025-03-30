package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.Product;
import fit.se2.SE2Midterm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
}