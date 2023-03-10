package me.j4n8.projekt02backend.auth.password_reset;

import me.j4n8.projekt02backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PasswordResetTokenService {
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	public PasswordResetToken createPasswordResetToken(User user) {
		String token = UUID.randomUUID().toString();
		PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
		return passwordResetTokenRepository.save(passwordResetToken);
	}
	
	public PasswordResetToken findByToken(String token) {
		return passwordResetTokenRepository.findByToken(token);
	}
	
	public void deletePasswordResetToken(PasswordResetToken passwordResetToken) {
		passwordResetTokenRepository.delete(passwordResetToken);
	}
	
}

