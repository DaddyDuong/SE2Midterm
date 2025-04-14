package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.Order;
import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.service.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/history")
    public String viewOrderHistory(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to view your order history");
            return "redirect:/user/login";
        }
        
        // Lấy danh sách đơn hàng đã xác nhận của người dùng
        List<Order> orders = orderService.getConfirmedOrdersByUser(loggedInUser);
        
        // Tạo map để lưu trữ số thứ tự đơn hàng của người dùng (bắt đầu từ 1)
        Map<Long, Integer> orderIndexMap = new HashMap<>();
        int index = 1;
        for (Order order : orders) {
            orderIndexMap.put(order.getId(), index++);
        }
        
        // Thêm danh sách đơn hàng và map số thứ tự vào model
        model.addAttribute("orders", orders);
        model.addAttribute("orderIndexMap", orderIndexMap);
        
        return "order/history";
    }
    
    @GetMapping("/detail/{orderId}")
    public String viewOrderDetail(@PathVariable("orderId") Long orderId, HttpSession session, Model model, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to view order details");
            return "redirect:/user/login";
        }
        
        try {
            // Lấy thông tin đơn hàng
            Order order = orderService.getOrderById(orderId);
            
            // Kiểm tra xem đơn hàng có thuộc về người dùng hiện tại không
            if (!order.getUser().getId().equals(loggedInUser.getId())) {
                redirectAttributes.addFlashAttribute("error", "You don't have permission to view this order");
                return "redirect:/orders/history";
            }
            
            // Lấy danh sách đơn hàng để tính toán số thứ tự
            List<Order> userOrders = orderService.getConfirmedOrdersByUser(loggedInUser);
            int orderIndex = 1;
            for (Order userOrder : userOrders) {
                if (userOrder.getId().equals(orderId)) {
                    break;
                }
                orderIndex++;
            }
            
            // Thêm thông tin đơn hàng và số thứ tự vào model
            model.addAttribute("order", order);
            model.addAttribute("orderIndex", orderIndex);
            
            return "order/detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Order not found");
            return "redirect:/orders/history";
        }
    }

    @GetMapping("/delete/{orderId}")
    public String deleteOrder(@PathVariable("orderId") Long orderId, HttpSession session, RedirectAttributes redirectAttributes) {
        // Kiểm tra xem người dùng đã đăng nhập chưa
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            redirectAttributes.addFlashAttribute("error", "Please log in to delete your order");
            return "redirect:/user/login";
        }
        
        try {
            // Thử xóa đơn hàng
            boolean deleted = orderService.deleteOrder(orderId, loggedInUser);
            
            if (deleted) {
                redirectAttributes.addFlashAttribute("success", "Order has been deleted successfully");
            } else {
                redirectAttributes.addFlashAttribute("error", "You don't have permission to delete this order");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Order not found or could not be deleted");
        }
        
        return "redirect:/orders/history";
    }
} 