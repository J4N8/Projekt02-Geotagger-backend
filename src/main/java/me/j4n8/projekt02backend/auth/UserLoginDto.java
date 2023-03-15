package me.j4n8.projekt02backend.auth;

public class UserLoginDto {
	
	private String username;
	private String password;
	
	public UserLoginDto() {
	}
	
	public UserLoginDto(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
}

