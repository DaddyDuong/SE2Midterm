package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.model.Cart;
import fit.se2.SE2Midterm.model.CartItem;
import fit.se2.SE2Midterm.service.UserService;
import fit.se2.SE2Midterm.service.UserCartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import java.util.HashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserCartService userCartService;

    @GetMapping({"/login", "/user/login"})
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    @PostMapping({"/login", "/user/login"})
    public String processLogin(@ModelAttribute("user") User user, HttpSession session, Model model) {
        System.out.println("Login attempt for username: " + user.getUsername() + ", password: " + user.getPassword());
        User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());

        if (authenticatedUser != null) {
            System.out.println("Login successful for: " + authenticatedUser.getUsername());
            
            // Get the current session cart (anonymous user's cart)
            Cart sessionCart = (Cart) session.getAttribute("cart");
            
            // Load the user's saved cart from database
            Cart savedCart = userCartService.loadUserCart(authenticatedUser);
            
            // Handle cart merging
            if (sessionCart != null && !sessionCart.isEmpty() && savedCart != null) {
                // Merge items from session cart into saved cart
                for (CartItem item : sessionCart.getItems()) {
                    savedCart.addItem(item.getProduct(), item.getQuantity());
                }
                
                // Save the merged cart back to the database
                userCartService.saveUserCart(authenticatedUser, savedCart);
                
                // Set the merged cart to session
                session.setAttribute("cart", savedCart);
            } else if (sessionCart != null && !sessionCart.isEmpty()) {
                // If user has no saved cart but has items in session cart, save session cart
                userCartService.saveUserCart(authenticatedUser, sessionCart);
                // Keep using session cart
            } else if (savedCart != null) {
                // If user has no session cart but has a saved cart, use saved cart
                session.setAttribute("cart", savedCart);
            }
            
            // Set the authenticated user in session
            session.setAttribute("loggedInUser", authenticatedUser);
            
            return "redirect:/";
        } else {
            // Add error message to the model
            System.out.println("Login failed for: " + user.getUsername());
            model.addAttribute("error", "Invalid username or password");
            return "user/login";
        }
    }

    @GetMapping({"/register", "/user/register"})
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping({"/register", "/user/register"})
    public String processRegistration(@ModelAttribute("user") User user, Model model) {
        // Validate input length - at least 4 characters required
        if (user.getUsername() == null || user.getUsername().length() < 4) {
            model.addAttribute("error", "Username must be at least 4 characters long.");
            return "user/register";
        }
        
        if (user.getPassword() == null || user.getPassword().length() < 4) {
            model.addAttribute("error", "Password must be at least 4 characters long.");
            return "user/register";
        }
        
        boolean registrationSuccess = userService.registerUser(user);

        if (registrationSuccess) {
            model.addAttribute("success", "Registration successful! Please login.");
            return "user/login";
        } else {
            // Username already exists, show error message
            model.addAttribute("error", "Username already exists. Please choose a different username.");
            return "user/register";
        }
    }

    @GetMapping({"/logout", "/user/logout"})
    public String logout(HttpSession session) {
        // Save user's cart to database before logout
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        Cart cart = (Cart) session.getAttribute("cart");
        
        if (loggedInUser != null && cart != null) {
            // Save the current state of the cart to the database
            userCartService.saveUserCart(loggedInUser, cart);
            System.out.println("Saved cart for user: " + loggedInUser.getUsername() + " with " + cart.getTotalItems() + " items");
        }
        
        // Invalidate the session to log the user out
        session.invalidate();
        return "redirect:/";
    }
    
    @GetMapping("/user/account")
    public String showUserAccount(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        
        // Add user to model
        model.addAttribute("user", loggedInUser);
        
        // Get cart item count
        Cart cart = (Cart) session.getAttribute("cart");
        int cartItemCount = 0;
        if (cart != null) {
            cartItemCount = cart.getTotalItems();
        }
        model.addAttribute("cartItemCount", cartItemCount);
        
        // Get order history count - this would typically come from an OrderService
        // For now, we'll use a placeholder or get it from the database if available
        int orderHistoryCount = 0; 
        // Todo: Replace with actual call to order service when implemented
        // orderHistoryCount = orderService.getOrderCountByUser(loggedInUser.getId());
        model.addAttribute("orderHistoryCount", orderHistoryCount);
        
        // Get completed order count (could be a subset of order history)
        int completedOrderCount = 0;
        // Todo: Replace with actual call when implemented
        // completedOrderCount = orderService.getCompletedOrderCountByUser(loggedInUser.getId());
        model.addAttribute("completedOrderCount", completedOrderCount);
        
        return "user/account";
    }
    
    @GetMapping("/user/profile")
    public String showUserProfile(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        
        model.addAttribute("user", loggedInUser);
        return "user/profile";
    }
    
    @PostMapping("/user/profile/update")
    public String updateUserProfile(
            @ModelAttribute("user") User updatedUser,
            HttpSession session,
            Model model) {
        
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        
        // Update only the profile fields, not username/password
        loggedInUser.setFullName(updatedUser.getFullName());
        loggedInUser.setEmail(updatedUser.getEmail());
        loggedInUser.setPhoneNumber(updatedUser.getPhoneNumber());
        loggedInUser.setAddress(updatedUser.getAddress());
        loggedInUser.setAge(updatedUser.getAge());
        loggedInUser.setGender(updatedUser.getGender());
        loggedInUser.setPreferredPaymentMethod(updatedUser.getPreferredPaymentMethod());
        
        // If avatar URL is provided, update it
        if (updatedUser.getAvatarUrl() != null && !updatedUser.getAvatarUrl().isEmpty()) {
            loggedInUser.setAvatarUrl(updatedUser.getAvatarUrl());
        }
        
        // Save the updated user
        userService.updateUser(loggedInUser);
        
        // Update the session
        session.setAttribute("loggedInUser", loggedInUser);
        
        // Add success message
        model.addAttribute("successMessage", "Profile updated successfully!");
        model.addAttribute("user", loggedInUser);
        
        return "user/profile";
    }

    // Test endpoint to diagnose login issues
    @GetMapping("/user/test-auth")
    @ResponseBody
    public String testAuth() {
        System.out.println("Testing authentication...");
        
        // Test with hardcoded credentials
        String username = "nam";
        String password = "123";
        
        System.out.println("Testing credentials: " + username + ", " + password);
        User user = userService.authenticateUser(username, password);
        
        if (user != null) {
            return "Authentication successful for user: " + user.getUsername();
        } else {
            return "Authentication failed. Check database connection and user credentials.";
        }
    }

    // Password change handling
    @PostMapping("/user/change-password")
    public String changePassword(@RequestParam("newPassword") String newPassword,
                                @RequestParam("confirmPassword") String confirmPassword,
                                HttpSession session,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Validate password match
        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Passwords do not match!");
            return "redirect:/user/account";
        }

        // Update password
        boolean updated = userService.updatePassword(loggedInUser.getUsername(), newPassword);
        
        if (updated) {
            // Update session user
            loggedInUser = userService.findByUsername(loggedInUser.getUsername());
            session.setAttribute("loggedInUser", loggedInUser);
            
            redirectAttributes.addFlashAttribute("successMessage", "Password changed successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update password. Please try again.");
        }
        
        return "redirect:/user/account";
    }
    
    // Update gender preference
    @PostMapping("/user/update-gender")
    public String updateGenderPreference(@RequestParam("gender") String gender,
                                       HttpSession session,
                                       RedirectAttributes redirectAttributes) {
        // Check if user is logged in
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        if (loggedInUser == null) {
            return "redirect:/login";
        }

        // Update gender
        loggedInUser.setGender(gender);
        
        // Save the updated user
        User updatedUser = userService.updateUser(loggedInUser);
        
        if (updatedUser != null) {
            // Update the session
            session.setAttribute("loggedInUser", updatedUser);
            redirectAttributes.addFlashAttribute("successMessage", "Gender preference updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to update gender preference. Please try again.");
        }
        
        return "redirect:/user/account";
    }

    // API endpoint to verify current password
    @PostMapping("/user/verify-password")
    @ResponseBody
    public Map<String, Object> verifyPassword(@RequestParam("password") String password,
                                           HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        // Get logged in user
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            response.put("success", false);
            return response;
        }
        
        // Verify password
        User authenticatedUser = userService.authenticateUser(loggedInUser.getUsername(), password);
        
        if (authenticatedUser != null) {
            response.put("success", true);
        } else {
            response.put("success", false);
        }
        
        return response;
    }
    
    // Session status check endpoint
    @GetMapping("/user/session-status")
    @ResponseBody
    public Map<String, Object> checkSessionStatus(HttpSession session) {
        Map<String, Object> status = new HashMap<>();
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser != null) {
            status.put("loggedIn", true);
            status.put("username", loggedInUser.getUsername());
            status.put("sessionId", session.getId());
            status.put("sessionCreationTime", new java.util.Date(session.getCreationTime()));
            status.put("sessionLastAccessedTime", new java.util.Date(session.getLastAccessedTime()));
        } else {
            status.put("loggedIn", false);
            status.put("sessionId", session.getId());
        }
        
        return status;
    }

    @PostMapping("/user/profile/upload-avatar")
    public String handleAvatarUpload(@RequestParam("avatarFile") MultipartFile file, 
                                    HttpSession session, 
                                    RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("loggedInUser");
        
        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "You must be logged in to upload an avatar.");
            return "redirect:/user/login";
        }
        
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
            return "redirect:/user/profile";
        }
        
        // Check file type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            redirectAttributes.addFlashAttribute("error", "Only image files are allowed.");
            return "redirect:/user/profile";
        }
        
        // Check file size (max 2MB)
        if (file.getSize() > 2 * 1024 * 1024) {
            redirectAttributes.addFlashAttribute("error", "File size exceeds the 2MB limit.");
            return "redirect:/user/profile";
        }
        
        try {
            // Create directories if they don't exist
            String uploadDir = "uploads/avatars";  // Changed to be relative to application root
            String publicPathDir = "/uploads/avatars";
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
                System.out.println("Created directory: " + uploadPath.toAbsolutePath());
            }
            
            // Generate a unique filename to prevent overwriting
            String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
            String fileExtension = "";
            if (originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            
            String filename = user.getUsername() + "_" + System.currentTimeMillis() + fileExtension;
            Path filePath = uploadPath.resolve(filename);
            
            // Delete the old avatar file if it exists
            if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                try {
                    String oldAvatarUrl = user.getAvatarUrl();
                    // Remove query parameters if present
                    if (oldAvatarUrl.contains("?")) {
                        oldAvatarUrl = oldAvatarUrl.substring(0, oldAvatarUrl.indexOf("?"));
                    }
                    String oldFilename = oldAvatarUrl.substring(oldAvatarUrl.lastIndexOf("/") + 1);
                    Path oldFilePath = uploadPath.resolve(oldFilename);
                    
                    if (Files.exists(oldFilePath)) {
                        Files.delete(oldFilePath);
                        System.out.println("Deleted old avatar file: " + oldFilePath.toAbsolutePath());
                    }
                } catch (Exception e) {
                    System.err.println("Error deleting old avatar file: " + e.getMessage());
                    // Continue with the upload even if deletion fails
                }
            }
            
            // Save the file
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Saved new avatar file: " + filePath.toAbsolutePath());
            
            // Verify file exists after saving
            if (Files.exists(filePath)) {
                System.out.println("Verify: File exists at: " + filePath);
                System.out.println("File size: " + Files.size(filePath) + " bytes");
            } else {
                System.out.println("WARNING: File does not exist after saving!");
            }
            
            // Update user's avatar URL (with timestamp to prevent caching)
            String avatarUrl = publicPathDir + "/" + filename + "?t=" + System.currentTimeMillis();
            user.setAvatarUrl(avatarUrl);
            userService.updateUser(user);
            
            // Debug information
            System.out.println("Avatar URL set to: " + avatarUrl);
            System.out.println("User object after update: " + user.toString());
            
            // Update the session
            session.setAttribute("loggedInUser", user);
            
            redirectAttributes.addFlashAttribute("successMessage", "Avatar uploaded successfully!");
        } catch (IOException e) {
            System.err.println("Error uploading avatar: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Failed to upload avatar: " + e.getMessage());
        }
        
        return "redirect:/user/profile";
    }
}