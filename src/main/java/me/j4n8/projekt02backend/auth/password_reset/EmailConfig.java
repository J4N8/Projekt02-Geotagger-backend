package me.j4n8.projekt02backend.auth.password_reset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
	@Value("${spring.mail.host}")
	private String mailHost;
	
	@Value("${spring.mail.port}")
	private int mailPort;
	
	@Value("${spring.mail.username}")
	private String mailUsername;
	
	@Value("${spring.mail.password}")
	private String mailPassword;
	
	@Bean
	public JavaMailSender javaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(mailHost);
		mailSender.setPort(mailPort);
		mailSender.setUsername(mailUsername);
		mailSender.setPassword(mailPassword);
		
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		
		mailSender.setJavaMailProperties(properties);
		
		return mailSender;
	}
}
