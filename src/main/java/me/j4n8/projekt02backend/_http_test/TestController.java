package me.j4n8.projekt02backend._http_test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping()
public class TestController {
	
	@GetMapping("/login")
	public String showLoginForm() {
		return "login";
	}
	
	@GetMapping("/register")
	public String showRegisterForm() {
		return "register";
	}
	
	@GetMapping("/home")
	public String showHomePage() {
		return "home";
	}
	
}
