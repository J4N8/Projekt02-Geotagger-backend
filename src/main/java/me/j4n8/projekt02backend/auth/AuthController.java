package me.j4n8.projekt02backend.auth;

import jakarta.servlet.http.HttpServletRequest;
import me.j4n8.projekt02backend.auth.password_reset.PasswordResetToken;
import me.j4n8.projekt02backend.auth.password_reset.PasswordResetTokenService;
import me.j4n8.projekt02backend.user.User;
import me.j4n8.projekt02backend.user.UserDto;
import me.j4n8.projekt02backend.user.UserRepository;
import me.j4n8.projekt02backend.user.UserService;
import me.j4n8.projekt02backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserDetailsService userDetailsService;
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDto) {
		User user = userService.registerUser(userRegisterDto.getEmail(), userRegisterDto.getPassword(), userRegisterDto.getUsername());
		JwtAuthenticationResponse response = new JwtAuthenticationResponse(user.getJwtToken());
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {
		// authenticate user
		User user = userService.authenticateUser(userLoginDto.getEmail(), userLoginDto.getPassword());
		
		if (user == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		
		// generate token
		String token = jwtTokenUtil.generateToken(user.getUsername());
		user.setJwtToken(token);
		userRepository.save(user);
		
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		JwtAuthenticationResponse response = new JwtAuthenticationResponse(token);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return ResponseEntity.ok().headers(headers).body(response);
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		try {
			User user = userService.getUserFromRequest(request);
			ResponseEntity<String> response = userService.invalidateToken(user, user.getJwtToken());
			response.getHeaders().set(HttpHeaders.AUTHORIZATION, null);
			SecurityContextHolder.getContext().setAuthentication(null);
			return response;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshTokens(HttpServletRequest request) {
		User user = userService.getUserFromRequest(request);
		if (user == null) {
			return ResponseEntity.badRequest().build();
		}
		UserDto userDto = new UserDto(user);
		
		//Validate token
		String oldToken = jwtTokenUtil.getTokenFromRequest(request);
		if (!jwtTokenUtil.validateToken(oldToken, (UserDetails) user)) {
			return ResponseEntity.badRequest().build();
		}
		//Generate new token
		String token = jwtTokenUtil.generateToken(user.getUsername());
		user.setJwtToken(token);
		userRepository.save(user);
		
		//Set tokens in header
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		JwtAuthenticationResponse response = new JwtAuthenticationResponse(token);
		return ResponseEntity.ok().headers(headers).body(response);
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserDto> getCurrentUser(HttpServletRequest request) {
		try {
			User user = userService.getUserFromRequest(request);
			UserDto userDto = new UserDto(user);
			return ResponseEntity.ok(userDto);
		} catch (Exception e) {
			return ResponseEntity.badRequest().build();
		}
	}
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private PasswordResetTokenService passwordResetTokenService;
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestParam("email") String email) {
		User user = userService.findByEmail(email);
		if (user == null) {
			return ResponseEntity.badRequest().body("No user with that email.");
		}
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);
		
		String resetPasswordLink = "http://localhost:8080/reset-password?token=" + passwordResetToken.getToken();
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("Password Reset Request");
		message.setText("To reset your password, click the link below:\n\n" + resetPasswordLink);
		javaMailSender.send(message);
		
		return ResponseEntity.ok().body("Password reset email sent successfully.");
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> changePassword(@RequestParam("token") String token, @RequestParam("new_password") String newPassword) {
		PasswordResetToken resetToken = passwordResetTokenService.findByToken(token);
		if (resetToken == null) {
			return ResponseEntity.badRequest().body("Invalid token.");
		}
		try {
			User user = resetToken.getUser();
			passwordResetTokenService.deletePasswordResetToken(resetToken);
			userService.changePassword(user, newPassword);
			return ResponseEntity.ok().body("Password changed.");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Something went wrong while changing password.");
		}
	}
}
