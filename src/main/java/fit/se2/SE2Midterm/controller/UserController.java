package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.model.Cart;
import fit.se2.SE2Midterm.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

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
}