package me.j4n8.projekt02backend.auth.password_reset;

import jakarta.persistence.*;
import me.j4n8.projekt02backend.user.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_token")
public class PasswordResetToken {
	private static final int EXPIRATION_MINUTES = 60;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(nullable = false)
	private String token;
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	@Column(nullable = false)
	private LocalDateTime expiryDate;
	
	public PasswordResetToken() {
	}
	
	public PasswordResetToken(String token, User user) {
		this.token = token;
		this.user = user;
		this.expiryDate = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);
	}
	
	public Long getId() {
		return id;
	}
	
	public String getToken() {
		return token;
	}
	
	public User getUser() {
		return user;
	}
	
	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}
	
	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expiryDate);
	}
}

