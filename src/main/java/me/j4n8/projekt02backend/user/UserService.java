package me.j4n8.projekt02backend.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return null;
        }
        if (!BCrypt.checkpw(password, user.getPassword())) {
            return null;
        }
        return user;
    }


    public User registerUser(String email, String password, String username) {
        User newUser = new User(email, passwordEncoder.encode(password), username);

        return userRepository.save(newUser);
    }
}
