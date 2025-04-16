package fit.se2.SE2Midterm.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_cart_items")
public class UserCartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_cart_id", nullable = false)
    private UserCart userCart;
    
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    
    @Column(nullable = false)
    private int quantity;
    
    public UserCartItem() {
    }
    
    public UserCartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public UserCart getUserCart() {
        return userCart;
    }
    
    public void setUserCart(UserCart userCart) {
        this.userCart = userCart;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
} 