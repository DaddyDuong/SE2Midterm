package fit.se2.SE2Midterm.service;

import fit.se2.SE2Midterm.model.*;
import fit.se2.SE2Midterm.repository.OrderRepository;
import fit.se2.SE2Midterm.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Order createOrder(User user, Cart cart, String shippingName, String shippingAddress,
                             String shippingCity, String shippingPostalCode,
                             String shippingCountry, String paymentMethod) {
        // Validate cart is not empty
        if (cart.isEmpty()) {
            throw new IllegalStateException("Cannot create order from an empty cart");
        }

        // Create new order
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(cart.getTotalPrice());
        order.setShippingName(shippingName);
        order.setShippingAddress(shippingAddress);
        order.setShippingCity(shippingCity);
        order.setShippingPostalCode(shippingPostalCode);
        order.setShippingCountry(shippingCountry);
        order.setPaymentMethod(paymentMethod);

        // Create order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : cart.getItems()) {
            OrderItem orderItem = new OrderItem(
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()
            );
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        // Mark order as paid
        order.setStatus("PAID");

        // Save order
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepository.findByUserOrderByCreatedAtDesc(user);
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }

    /**
     * Lấy danh sách đơn hàng đã xác nhận của người dùng
     * (các đơn hàng có trạng thái PAID, SHIPPED hoặc DELIVERED)
     */
    public List<Order> getConfirmedOrdersByUser(User user) {
        List<Order> allOrders = orderRepository.findByUserOrderByCreatedAtDesc(user);
        return allOrders.stream()
                .filter(order -> order.getStatus().equals("PAID") 
                        || order.getStatus().equals("SHIPPED") 
                        || order.getStatus().equals("DELIVERED"))
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Xóa đơn hàng theo ID
     * Chỉ người dùng sở hữu đơn hàng mới có thể xóa
     */
    @Transactional
    public boolean deleteOrder(Long orderId, User currentUser) {
        Order order = getOrderById(orderId);
        
        // Kiểm tra xem người dùng hiện tại có phải là chủ sở hữu của đơn hàng không
        if (!order.getUser().getId().equals(currentUser.getId())) {
            return false;
        }
        
        // Xóa đơn hàng
        orderRepository.delete(order);
        return true;
    }
}