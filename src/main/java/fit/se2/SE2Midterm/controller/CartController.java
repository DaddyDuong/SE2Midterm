package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.Cart;
import fit.se2.SE2Midterm.model.Product;
import fit.se2.SE2Midterm.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private ProductService productService;

    // Initialize cart in session if it doesn't exist
    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            session.setAttribute("cart", cart);
        }
        return cart;
    }

    // View cart
    @GetMapping
    public String viewCart(Model model, HttpSession session) {
        Cart cart = getCart(session);
        model.addAttribute("cart", cart);
        return "cart/view";
    }

    // Add item to cart
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "quantity", defaultValue = "1") int quantity,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        Product product = productService.getProductById(productId);
        if (product == null) {
            response.put("success", false);
            response.put("message", "Product not found");
            return ResponseEntity.badRequest().body(response);
        }

        Cart cart = getCart(session);
        cart.addItem(product, quantity);

        response.put("success", true);
        response.put("message", product.getTitle() + " added to cart");
        response.put("totalItems", cart.getTotalItems());

        return ResponseEntity.ok(response);
    }

    // Update item quantity
    @PostMapping("/update")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @RequestParam("productId") Long productId,
            @RequestParam("quantity") int quantity,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        Cart cart = getCart(session);
        cart.updateItemQuantity(productId, quantity);

        response.put("success", true);
        response.put("totalItems", cart.getTotalItems());
        response.put("totalPrice", cart.getTotalPrice());

        return ResponseEntity.ok(response);
    }

    // Remove item from cart
    @PostMapping("/remove")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @RequestParam("productId") Long productId,
            HttpSession session) {

        Map<String, Object> response = new HashMap<>();

        Cart cart = getCart(session);
        cart.removeItem(productId);

        response.put("success", true);
        response.put("totalItems", cart.getTotalItems());

        return ResponseEntity.ok(response);
    }

    // Clear cart
    @PostMapping("/clear")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> clearCart(HttpSession session) {
        Cart cart = getCart(session);
        cart.clear();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);

        return ResponseEntity.ok(response);
    }
}