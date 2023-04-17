package me.j4n8.projekt02backend.auth;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import me.j4n8.projekt02backend.auth.jwt.JwtAuthenticationResponse;
import me.j4n8.projekt02backend.auth.jwt.JwtTokenUtil;
import me.j4n8.projekt02backend.auth.password_reset.PasswordResetDto;
import me.j4n8.projekt02backend.auth.password_reset.PasswordResetToken;
import me.j4n8.projekt02backend.auth.password_reset.PasswordResetTokenService;
import me.j4n8.projekt02backend.user.User;
import me.j4n8.projekt02backend.user.UserDto;
import me.j4n8.projekt02backend.user.UserRepository;
import me.j4n8.projekt02backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;

@Log4j2
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
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private PasswordResetTokenService passwordResetTokenService;
	
	@PostMapping("/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody UserLoginDto authenticationRequest) {
		try {
			// Authenticate user with username and password from request body and set the authentication in the security context
			authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
			final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
			final String token = jwtTokenUtil.generateToken(userDetails);
			
			// Set token in auth header and cookies
			return setAuthHeaderAndCookies(token);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body("Invalid credentials " + e.getMessage());
		}
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDto) {
		// Check if user with that email already exists
		if (userService.usernameExists(userRegisterDto.getUsername())) {
			return ResponseEntity.unprocessableEntity().body("User with that username already exists");
		}
		// Create new user in database and return it
		User user = userService.registerUser(userRegisterDto.getEmail(), userRegisterDto.getPassword(), userRegisterDto.getUsername());
		return ResponseEntity.ok(user);
	}
	
	private void authenticate(String username, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new DisabledException("USER IS DISABLED");
		} catch (BadCredentialsException e) {
			throw new BadCredentialsException("INVALID CREDENTIALS");
		}
	}
	
	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		try {
			User user = userService.getUserFromRequest(request);
			// Invalidate token and remove it from the database and from the security context
			ResponseEntity<String> response = userService.invalidateToken(user, user.getJwtToken());
			response.getHeaders().set(HttpHeaders.AUTHORIZATION, null);
			SecurityContextHolder.getContext().setAuthentication(null);
			return response;
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}
	}
	
	private ResponseEntity<?> setAuthHeaderAndCookies(String token) {
		// Set token in auth header and cookies
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
		ResponseCookie jwtCookie = ResponseCookie.from("jwtToken", token)
				.httpOnly(true)
				.secure(false) // set this to true if you are using HTTPS
				.maxAge(Duration.ofHours(1))
				.build();
		
		headers.add(HttpHeaders.SET_COOKIE, jwtCookie.toString());
		JwtAuthenticationResponse response = new JwtAuthenticationResponse(token);
		
		return ResponseEntity.ok().headers(headers).body(response);
	}
	
	@PostMapping("/refresh")
	public ResponseEntity<?> refreshTokens(HttpServletRequest request) {
		User user = userService.getUserFromRequest(request);
		if (user == null) {
			return ResponseEntity.badRequest().build();
		}
		
		// Check if token is valid
		String oldToken = jwtTokenUtil.getTokenFromRequestHeader(request);
		if (!jwtTokenUtil.validateToken(oldToken, (UserDetails) user)) {
			return ResponseEntity.badRequest().build();
		}
		// Generate new token and set it in the database and in the security context
		String token = jwtTokenUtil.generateToken(user.getUsername());
		user.setJwtToken(token);
		userRepository.save(user);
		
		return setAuthHeaderAndCookies(token);
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserDto> getCurrentUser() {
		try {
			// Get current user from the security context
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = authentication.getName();
			User user = userService.findByUsername(username);
			UserDto userDto = new UserDto(user);
			return ResponseEntity.ok(userDto);
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping("/forgot-password")
	public ResponseEntity<?> forgotPassword(@RequestBody PasswordResetDto passwordResetDto) {
		User user = userService.findByEmail(passwordResetDto.getEmail());
		if (user == null) {
			return ResponseEntity.badRequest().body("No user with that email.");
		}
		// Create password reset token
		PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetToken(user);
		
		// Send email with reset link
		String resetPasswordLink = "http://localhost:8080/reset-password?token=" + passwordResetToken.getToken();
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(user.getEmail());
		message.setSubject("Password Reset Request");
		message.setText("To reset your password, click the link below:\n\n" + resetPasswordLink);
		javaMailSender.send(message);
		
		return ResponseEntity.ok().body("Password reset email sent successfully.");
	}
	
	@PostMapping("/reset-password")
	public ResponseEntity<?> changePassword(@RequestBody PasswordResetDto passwordResetDto) {
		// Check if token is valid
		PasswordResetToken resetToken = passwordResetTokenService.findByToken(passwordResetDto.getToken());
		if (resetToken == null) {
			return ResponseEntity.badRequest().body("Invalid token.");
		}
		try {
			// Change password and delete reset token from the database
			User user = resetToken.getUser();
			passwordResetTokenService.deletePasswordResetToken(resetToken);
			userService.changePassword(user, passwordResetDto.getNewPassword());
			return ResponseEntity.ok().body("Password changed.");
		} catch (Exception e) {
			return ResponseEntity.internalServerError().body("Something went wrong while changing password.");
		}
	}
}
