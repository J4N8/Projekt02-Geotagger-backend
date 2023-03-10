package me.j4n8.projekt02backend.auth.password_reset;

public class PasswordResetDto {
	private String email;
	private String token;
	private String newPassword;
	
	public PasswordResetDto() {
	}
	
	public PasswordResetDto(String email, String token, String newPassword) {
		this.email = email;
		this.token = token;
		this.newPassword = newPassword;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
	
	public String getNewPassword() {
		return newPassword;
	}
	
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
