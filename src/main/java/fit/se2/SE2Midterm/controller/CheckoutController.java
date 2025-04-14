package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.Cart;
import fit.se2.SE2Midterm.model.Order;
import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
    @Autowired
    private OrderService orderService;

    @GetMapping
    public String showCheckoutPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to proceed with checkout");
            return "redirect:/login";
        }

        // Check if cart is empty
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty");
            return "redirect:/cart";
        }

        // Add cart details to the model
        model.addAttribute("cart", cart);
        model.addAttribute("user", user);

        return "checkout/checkout";
    }

    @PostMapping("/process")
    public String processCheckout(
            @RequestParam("shippingName") String shippingName,
            @RequestParam("shippingAddress") String shippingAddress,
            @RequestParam("shippingCity") String shippingCity,
            @RequestParam("shippingPostalCode") String shippingPostalCode,
            @RequestParam("shippingCountry") String shippingCountry,
            @RequestParam("paymentMethod") String paymentMethod,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        // Check if user is logged in
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to proceed with checkout");
            return "redirect:/login";
        }

        // Get cart from session
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart == null || cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty");
            return "redirect:/cart";
        }

        try {
            // Create order
            Order order = orderService.createOrder(
                    user, cart,
                    shippingName, shippingAddress,
                    shippingCity, shippingPostalCode,
                    shippingCountry, paymentMethod
            );

            // Clear the cart after successful order
            cart.clear();
            session.setAttribute("cart", cart);

            // Add success message
            redirectAttributes.addFlashAttribute("orderSuccess", "Order placed successfully!");
            redirectAttributes.addFlashAttribute("orderId", order.getId());

            return "redirect:/checkout/confirmation";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to process order: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/confirmation")
    public String showConfirmationPage(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        // Check if order success message exists
        if (!model.containsAttribute("orderSuccess")) {
            return "redirect:/";
        }

        // Get the actual order ID from flash attributes
        Long orderId = (Long) model.asMap().get("orderId");
        
        try {
            // Lấy đơn hàng hiện tại
            Order currentOrder = orderService.getOrderById(orderId);
            
            if (currentOrder != null) {
                // Lấy danh sách tất cả đơn hàng đã xác nhận của người dùng
                List<Order> userOrders = orderService.getConfirmedOrdersByUser(user);
                
                // Đếm số đơn hàng có thời gian tạo trước hoặc bằng đơn hàng hiện tại
                int orderIndex = 0;
                for (Order order : userOrders) {
                    if (order.getCreatedAt().compareTo(currentOrder.getCreatedAt()) <= 0) {
                        orderIndex++;
                    }
                }
                
                // Thêm số thứ tự vào model
                model.addAttribute("orderIndex", orderIndex);
            } else {
                // Nếu không tìm thấy đơn hàng, sử dụng số thứ tự mặc định
                model.addAttribute("orderIndex", 1);
            }
        } catch (Exception e) {
            // Nếu có lỗi, sử dụng 1 làm số thứ tự mặc định
            model.addAttribute("orderIndex", 1);
        }

        return "checkout/confirmation";
    }
}