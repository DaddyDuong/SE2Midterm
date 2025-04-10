package fit.se2.SE2Midterm.service;

import fit.se2.SE2Midterm.model.ContactForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final String SUPPORT_EMAIL = "nekko1309@gmail.com";

    @Autowired
    private JavaMailSender mailSender;

    public void sendContactFormEmail(ContactForm contactForm) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(SUPPORT_EMAIL);
        message.setSubject("New Contact Form Submission: " + contactForm.getSubject());
        
        String emailBody = "Name: " + contactForm.getName() + "\n"
                + "Email: " + contactForm.getEmail() + "\n"
                + "Phone: " + contactForm.getPhone() + "\n\n"
                + "Message: \n" + contactForm.getMessage();
        
        message.setText(emailBody);
        mailSender.send(message);
    }
} 