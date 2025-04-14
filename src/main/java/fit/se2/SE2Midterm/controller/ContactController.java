package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.ContactForm;
import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.service.EmailService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for handling contact form functionality
 * Requires user to be logged in to access the contact page
 */
@Controller
public class ContactController {

    @Autowired
    private EmailService emailService;

    /**
     * Display the contact form page
     * @param session HttpSession for checking login status
     * @param model Model for passing data to the view
     * @return contact page view or redirect to login if not logged in
     */
    @GetMapping("/contact")
    public String showContactForm(HttpSession session, Model model) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        
        model.addAttribute("contactForm", new ContactForm());
        return "contact/contact";
    }

    /**
     * Process the contact form submission
     * @param contactForm the submitted form data
     * @param session HttpSession for checking login status
     * @param redirectAttributes for passing flash messages
     * @return redirect to success page or login page if not logged in
     */
    @PostMapping("/contact")
    public String submitContactForm(
            @ModelAttribute("contactForm") ContactForm contactForm,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
                                       
        try {
            // Add username to subject for better tracking
            contactForm.setSubject(contactForm.getSubject() + " (from: " + loggedInUser.getUsername() + ")");
            
            // Send email with user information
            emailService.sendContactFormEmail(contactForm, loggedInUser);
            
            // Add success message
            redirectAttributes.addFlashAttribute("success", 
                    "Thank you for your message! We will get back to you soon at " + 
                    (contactForm.getEmail() != null ? contactForm.getEmail() : loggedInUser.getEmail()));
            
            System.out.println("Contact form submitted successfully by user: " + loggedInUser.getUsername());
        } catch (Exception e) {
            System.err.println("Error sending contact form: " + e.getMessage());
            
            redirectAttributes.addFlashAttribute("error", 
                    "There was an error sending your message. Please try again later or contact us directly at " + 
                    emailService.getSupportEmail());
        }
        return "redirect:/contact/success";
    }

    /**
     * Display the success page after form submission
     * @param session HttpSession for checking login status
     * @return success page view or redirect to login if not logged in
     */
    @GetMapping("/contact/success")
    public String contactSuccess(HttpSession session) {
        User loggedInUser = (User) session.getAttribute("loggedInUser");
        
        if (loggedInUser == null) {
            return "redirect:/user/login";
        }
        
        return "contact/success";
    }
} 