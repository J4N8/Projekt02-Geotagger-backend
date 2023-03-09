package me.j4n8.projekt02backend.user_actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserActionService {
	@Autowired
	private UserActionRepository userActionRepository;
	
	public UserAction newLog(UserActionDto userActionDto) {
		UserAction userAction = new UserAction(userActionDto);
		return userActionRepository.save(userAction);
	}
	
	public List<UserAction> getLast100Logs() {
		return userActionRepository.findTop100ByOrderByDateTimeDesc();
	}
}
