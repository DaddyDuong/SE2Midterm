package fit.se2.SE2Midterm.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Cart {
    private List<CartItem> items;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    public void addItem(Product product, int quantity) {
        Optional<CartItem> existingItem = items.stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItem.isPresent()) {
            // Update quantity if product already in cart
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            // Add new item to cart
            items.add(new CartItem(product, quantity));
        }
    }

    public void updateItemQuantity(Long productId, int quantity) {
        items.stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    if (quantity > 0) {
                        item.setQuantity(quantity);
                    } else {
                        removeItem(productId);
                    }
                });
    }

    public void removeItem(Long productId) {
        items.removeIf(item -> item.getProduct().getId().equals(productId));
    }

    public void clear() {
        items.clear();
    }

    public int getTotalItems() {
        return items.stream().mapToInt(CartItem::getQuantity).sum();
    }

    public double getTotalPrice() {
        return items.stream().mapToDouble(CartItem::getSubtotal).sum();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}