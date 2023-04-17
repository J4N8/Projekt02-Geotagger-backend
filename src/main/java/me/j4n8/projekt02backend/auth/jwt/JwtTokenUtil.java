package me.j4n8.projekt02backend.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {
	
	private final Key key;
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private long expiration;
	
	public JwtTokenUtil() {
		key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
	}
	
	/***
	 * Generates a new JWT token for the given user details
	 * @param userDetails The user details
	 * @return The JWT token
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, userDetails.getUsername());
	}
	
	/***
	 * Generates a new JWT token for the given username
	 * @param username The username
	 * @return The JWT token
	 */
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		return createToken(claims, username);
	}
	
	/***
	 * Extracts the username from the given JWT token
	 * @param token The JWT token
	 * @return The username
	 */
	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	/***
	 * Extracts the expiration date from the given JWT token
	 * @param token The JWT token
	 * @return The expiration date
	 */
	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}
	
	/***
	 * Validates the given JWT token
	 * @param token The JWT token
	 * @param userDetails The user details
	 * @return True if the token is valid, false otherwise
	 */
	public boolean validateToken(String token, UserDetails userDetails) {
		String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
	
	/***
	 * Checks if the given JWT token is expired
	 * @param token The JWT token
	 * @return True if the token is expired, false otherwise
	 */
	private boolean isTokenExpired(String token) {
		Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	/***
	 * Extracts a claim from the given JWT token
	 * @param token The JWT token
	 * @param claimsResolver The claim resolver
	 * @param <T> The type of the claim
	 * @return The claim
	 */
	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}
	
	/***
	 * Extracts all claims from the given JWT token
	 * @param token The JWT token
	 * @return The claims
	 */
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
	}
	
	/***
	 * Creates a new JWT token
	 * @param claims The claims
	 * @param subject The subject
	 * @return The JWT token
	 */
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
				.signWith(key, SignatureAlgorithm.HS256)
				.compact();
	}
	
	/***
	 * Extracts the JWT token from the given request
	 * @param request The request
	 * @return The JWT token
	 */
	public String getTokenFromRequestHeader(HttpServletRequest request) {
		String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		String token = null;
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
		}
		return token;
	}
}