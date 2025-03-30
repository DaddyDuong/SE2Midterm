package fit.se2.SE2Midterm.model;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;  // Changed from 'long' to 'Long'

    private String username;
    private String password;
    private String email;

    public User() {
        // Default constructor required by JPA
    }

    public Long getId() {  // Changed return type from 'long' to 'Long'
        return id;
    }

    public void setId(Long id) {  // Changed parameter type from 'long' to 'Long'
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}