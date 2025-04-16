package fit.se2.SE2Midterm.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_carts")
public class UserCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @OneToMany(mappedBy = "userCart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserCartItem> cartItems = new ArrayList<>();
    
    public UserCart() {
    }
    
    public UserCart(User user) {
        this.user = user;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public List<UserCartItem> getCartItems() {
        return cartItems;
    }
    
    public void setCartItems(List<UserCartItem> cartItems) {
        this.cartItems = cartItems;
    }
    
    public void addCartItem(UserCartItem item) {
        cartItems.add(item);
        item.setUserCart(this);
    }
    
    public void removeCartItem(UserCartItem item) {
        cartItems.remove(item);
        item.setUserCart(null);
    }
    
    public void clearItems() {
        cartItems.clear();
    }
} 