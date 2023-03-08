package me.j4n8.projekt02backend.user_actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-action")
public class UserActionController {
	
	@Autowired
	private UserActionService userActionService;
	
	@PostMapping("new")
	public UserAction newLog(@RequestBody UserActionDto userActionDto) {
		return userActionService.newLog(userActionDto);
	}
	
	@GetMapping("get")
	public List<UserAction> getLast100Logs() {
		return userActionService.getLast100Logs();
	}
}
