package fit.se2.SE2Midterm.controller;

import fit.se2.SE2Midterm.model.ContactForm;
import fit.se2.SE2Midterm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/contact")
    public String showContactForm(Model model) {
        model.addAttribute("contactForm", new ContactForm());
        return "contact/contact";
    }

    @PostMapping("/contact")
    public String submitContactForm(@ModelAttribute("contactForm") ContactForm contactForm,
                                   RedirectAttributes redirectAttributes) {
        try {
            emailService.sendContactFormEmail(contactForm);
            redirectAttributes.addFlashAttribute("success", 
                    "Thank you for your message! We will get back to you soon.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", 
                    "There was an error sending your message. Please try again later.");
        }
        return "redirect:/contact/success";
    }

    @GetMapping("/contact/success")
    public String contactSuccess() {
        return "contact/success";
    }
} 