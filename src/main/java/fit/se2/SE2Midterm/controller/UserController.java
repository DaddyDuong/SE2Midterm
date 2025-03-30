package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("user", new User());
        return "user/login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute("user") User user, HttpSession session, Model model) {
        User authenticatedUser = userService.authenticateUser(user.getUsername(), user.getPassword());

        if (authenticatedUser != null) {
            session.setAttribute("loggedInUser", authenticatedUser);
            return "redirect:/";
        } else {
            // Add error message to the model
            model.addAttribute("error", "Invalid username or password");
            return "user/login";
        }
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
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

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}