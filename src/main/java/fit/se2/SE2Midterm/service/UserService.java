package fit.se2.SE2Midterm.service;

import fit.se2.SE2Midterm.model.User;
import fit.se2.SE2Midterm.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        User user = userRepository.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
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
    public boolean registerUser(User user) {
        if (isUsernameExists(user.getUsername())) {
            return false;
        }

        userRepository.save(user);
        return true;
    }
}