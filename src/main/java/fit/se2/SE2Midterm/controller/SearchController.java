package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.Product;
import fit.se2.SE2Midterm.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        // Sử dụng phương thức searchProductsByTitle từ ProductService để tìm kiếm sản phẩm
        List<Product> searchResults = productService.searchProductsByTitle(query);
        
        model.addAttribute("products", searchResults);
        model.addAttribute("title", "Search Results for: " + query);
        model.addAttribute("searchQuery", query);
        
        // Chuyển người dùng đến trang danh sách sản phẩm
        return "product/list";
    }
} 