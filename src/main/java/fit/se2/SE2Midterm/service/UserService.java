package fit.se2.SE2Midterm.service;

import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Authenticate a user with username and password
     * @param username Username to check
     * @param password Password to verify
     * @return User object if authentication successful, null otherwise
     */
    public User authenticateUser(String username, String password) {
        System.out.println("Authenticating user: " + username);
        User user = userRepository.findByUsername(username);
        
        System.out.println("User found in database: " + (user != null));
        if (user != null) {
            System.out.println("Stored password: " + user.getPassword());
            System.out.println("Input password: " + password);
            System.out.println("Password match: " + user.getPassword().equals(password));
        }

        if (user != null && user.getPassword().equals(password)) {
            System.out.println("Authentication successful for user: " + username);
            return user;
        }

        System.out.println("Authentication failed for user: " + username);
        return null;
    }

    /**
     * Get all users from the database
     * @return List of all users
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Find a user by username
     * @param username Username to find
     * @return User object if found, null otherwise
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Check if a username already exists in the database
     * @param username Username to check
     * @return true if username exists, false otherwise
     */
    public boolean isUsernameExists(String username) {
        User user = userRepository.findByUsername(username);
        return user != null;
    }

    /**
     * Register a new user
     * @param user User to register
     * @return true if registration successful, false if username already exists
     */
    @Transactional
    public boolean registerUser(User user) {
        if (isUsernameExists(user.getUsername())) {
            System.out.println("Registration failed: Username already exists: " + user.getUsername());
            return false;
        }

        System.out.println("Registering new user: " + user.getUsername());
        userRepository.save(user);
        System.out.println("User registered successfully: " + user.getUsername());
        return true;
    }

    /**
     * Update an existing user's information
     * @param user User to update
     * @return Updated user object
     */
    @Transactional
    public User updateUser(User user) {
        System.out.println("Updating user: " + user.getUsername());
        User updatedUser = userRepository.save(user);
        System.out.println("User updated successfully: " + user.getUsername());
        return updatedUser;
    }
    
    /**
     * Update user's password
     * @param username Username of the user
     * @param newPassword New password to set
     * @return true if password updated successfully, false otherwise
     */
    @Transactional
    public boolean updatePassword(String username, String newPassword) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            System.out.println("Password update failed: User not found: " + username);
            return false;
        }
        
        System.out.println("Updating password for user: " + username);
        user.setPassword(newPassword);
        userRepository.save(user);
        System.out.println("Password updated successfully for user: " + username);
        return true;
    }
}