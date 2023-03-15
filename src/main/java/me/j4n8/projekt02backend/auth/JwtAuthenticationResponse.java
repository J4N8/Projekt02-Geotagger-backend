package me.j4n8.projekt02backend.auth;

public class JwtAuthenticationResponse {
	private String jwtToken;
	
	public JwtAuthenticationResponse(String jwtToken) {
		this.jwtToken = jwtToken;
	}
	
	public String getJwtToken() {
		return jwtToken;
	}
	
	public void setJwtToken(String jwtToken) {
		this.jwtToken = jwtToken;
	}
}

