package fit.se2.SE2Midterm.service;

import fit.se2.SE2Midterm.model.ContactForm;
import fit.se2.SE2Midterm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class EmailService {

    private static final String SUPPORT_EMAIL = "nekko1309@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Get the support email address
     * @return the support email address
     */
    public String getSupportEmail() {
        return SUPPORT_EMAIL;
    }
    
    /**
     * Send contact form email with user information
     * @param contactForm The contact form data
     * @param user The user who submitted the form
     */
    public void sendContactFormEmail(ContactForm contactForm, User user) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(SUPPORT_EMAIL);
            message.setSubject("New Contact Form Submission: " + contactForm.getSubject());
            
            // Format current date and time
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = now.format(formatter);
            
            String emailBody = "===== CONTACT FORM SUBMISSION =====\n\n" +
                    "Submission Date: " + formattedDateTime + "\n\n" +
                    "USER INFORMATION:\n" +
                    "Username: " + user.getUsername() + "\n" +
                    "User Email: " + user.getEmail() + "\n\n" +
                    "CONTACT INFORMATION:\n" +
                    "Name: " + contactForm.getName() + "\n" +
                    "Email: " + contactForm.getEmail() + "\n" +
                    "Phone: " + (contactForm.getPhone() != null ? contactForm.getPhone() : "Not provided") + "\n\n" +
                    "REQUEST DETAILS:\n" +
                    "Subject: " + contactForm.getSubject() + "\n\n" +
                    "Message:\n" + contactForm.getMessage() + "\n\n" +
                    "===================================\n" +
                    "This message was sent from the E-Shop contact form.";
            
            message.setText(emailBody);
            mailSender.send(message);
            System.out.println("Email sent successfully to " + SUPPORT_EMAIL);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
            e.printStackTrace();
            throw e; // Re-throw to be handled by the controller
        }
    }
} 