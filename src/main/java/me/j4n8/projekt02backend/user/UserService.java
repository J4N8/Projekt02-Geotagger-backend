package me.j4n8.projekt02backend.user;

import jakarta.servlet.http.HttpServletRequest;
import me.j4n8.projekt02backend.auth.jwt.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
    public User findByUsername(String username) {
	    return userRepository.findByUsername(username);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
	
	public User registerUser(String email, String password, String username) {
		// Generate token and save user to database
		String token = jwtTokenUtil.generateToken(email);
		User newUser = new User(email, passwordEncoder.encode(password), username, token);
		return userRepository.save(newUser);
	}
	
	public ResponseEntity<String> invalidateToken(User user, String token) {
		if (token != null && jwtTokenUtil.validateToken(token, (UserDetails) user)) {
			String username = jwtTokenUtil.getUsernameFromToken(token);
			//Invalidate the token
			user.setJwtToken(null);
			userRepository.save(user);
			return ResponseEntity.ok("Logout successful for user " + username);
		} else {
			return ResponseEntity.badRequest().body("Invalid or expired token");
		}
	}
	
	public User getUserFromRequest(HttpServletRequest request) {
		try {
			String token = jwtTokenUtil.getTokenFromRequestHeader(request);
			String username = jwtTokenUtil.getUsernameFromToken(token);
			User currentUser = findByUsername(username);
			return currentUser;
		} catch (Exception e) {
			return null;
		}
	}
	
	public User changePassword(User user, String newPassword) {
		user.setPassword(passwordEncoder.encode(newPassword));
		return userRepository.save(user);
	}
	
	public boolean usernameExists(String username) {
		return userRepository.existsByUsername(username);
	}
}
