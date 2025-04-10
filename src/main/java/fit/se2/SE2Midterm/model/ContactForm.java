package fit.se2.SE2Midterm.model;

public class ContactForm {
    private String name;
    private String phone;
    private String email;
    private String message;
    private String subject;

    public ContactForm() {
    }

    public ContactForm(String name, String phone, String email, String message, String subject) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.message = message;
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
} 