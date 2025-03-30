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
}