package me.j4n8.projekt02backend.auth.password_reset;

import me.j4n8.projekt02backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetTokenService {
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	/***
	 * Creates a new password reset token in database for the given user
	 * @param user The user
	 * @return The password reset token
	 */
	public PasswordResetToken createPasswordResetToken(User user) {
		String token = UUID.randomUUID().toString();
		PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
		return passwordResetTokenRepository.save(passwordResetToken);
	}
	
	/***
	 * Finds the password reset token in the database by the given token
	 * @param token The token
	 * @return The password reset token
	 */
	public PasswordResetToken findByToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}
	
	/***
	 * Deletes the given password reset token from the database
	 * @param passwordResetToken The password reset token
	 */
	public void deletePasswordResetToken(PasswordResetToken passwordResetToken) {
		passwordResetTokenRepository.delete(passwordResetToken);
	}
	
}

