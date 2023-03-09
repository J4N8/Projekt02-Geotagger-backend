package me.j4n8.projekt02backend.auth;

import me.j4n8.projekt02backend.util.JwtTokenFilter;
import me.j4n8.projekt02backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenUtil, userDetailsService());
		return http
				.authorizeHttpRequests()
				// Allow access to login and register page without authentication
				.requestMatchers("/auth/login").permitAll()
				.requestMatchers("/login").permitAll()
				.requestMatchers("/register").permitAll()
				.requestMatchers("/auth/register").permitAll()
				.anyRequest().permitAll()
//				.anyRequest().authenticated() // All other requests need authentication
				.and()
				.formLogin()
				.loginPage("/login")
				.defaultSuccessUrl("/home")
				.permitAll()
				.and()
				.logout()
				.permitAll()
				.and()
				.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
				.csrf()
				.disable() // disable CSRF protection for simplicity in this example
				.build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager();
		userDetailsManager.setDataSource(dataSource);
		userDetailsManager.setUsersByUsernameQuery("select username,password,'true' as enabled from users where username = ?");
		return userDetailsManager;
	}
}

