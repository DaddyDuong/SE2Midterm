package fit.se2.SE2Midterm.service;

import fit.se2.SE2Midterm.model.*;
import fit.se2.SE2Midterm.repository.UserCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserCartService {
    
    @Autowired
    private UserCartRepository userCartRepository;
    
    /**
     * Save user's cart to database
     */
    @Transactional
    public void saveUserCart(User user, Cart sessionCart) {
        if (user == null || sessionCart == null) {
            return;
        }
        
        // Find existing user cart or create new one
        UserCart userCart = userCartRepository.findByUser(user)
                .orElse(new UserCart(user));
        
        // Clear existing items
        userCart.clearItems();
        
        // Add items from session cart
        for (CartItem item : sessionCart.getItems()) {
            UserCartItem userCartItem = new UserCartItem(item.getProduct(), item.getQuantity());
            userCart.addCartItem(userCartItem);
        }
        
        // Save to database
        userCartRepository.save(userCart);
    }
    
    /**
     * Load user's cart from database to session
     */
    @Transactional(readOnly = true)
    public Cart loadUserCart(User user) {
        if (user == null) {
            return new Cart();
        }
        
        Optional<UserCart> userCartOpt = userCartRepository.findByUser(user);
        
        if (userCartOpt.isPresent()) {
            UserCart userCart = userCartOpt.get();
            Cart sessionCart = new Cart();
            
            // Transfer items from database to session cart
            for (UserCartItem item : userCart.getCartItems()) {
                sessionCart.addItem(item.getProduct(), item.getQuantity());
            }
            
            return sessionCart;
        }
        
        return new Cart();
    }
    
    /**
     * Clear user's cart from database
     */
    @Transactional
    public void clearUserCart(User user) {
        if (user == null) {
            return;
        }
        
        Optional<UserCart> userCartOpt = userCartRepository.findByUser(user);
        
        if (userCartOpt.isPresent()) {
            UserCart userCart = userCartOpt.get();
            userCart.clearItems();
            userCartRepository.save(userCart);
        }
    }
} 