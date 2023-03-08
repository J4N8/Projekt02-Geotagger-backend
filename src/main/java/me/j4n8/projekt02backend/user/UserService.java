package me.j4n8.projekt02backend.user;

import jakarta.servlet.http.HttpServletRequest;
import me.j4n8.projekt02backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
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
	    User user = userRepository.findByUsername(username)
			    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
	    return user;
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
			String token = jwtTokenUtil.getTokenFromRequest(request);
			String username = jwtTokenUtil.getUsernameFromToken(token);
			User currentUser = findByUsername(username);
			return currentUser;
		} catch (Exception e) {
			return null;
		}
	}
}
