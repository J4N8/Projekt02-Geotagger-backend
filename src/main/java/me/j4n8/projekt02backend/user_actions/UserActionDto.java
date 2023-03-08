package me.j4n8.projekt02backend.user_actions;

import lombok.NonNull;
import me.j4n8.projekt02backend.user.User;

public class UserActionDto {
	@NonNull
	private final User user;
	private final String newValue;
	@NonNull
	private final String url;
	@NonNull
	private final UserActionEnum action;
	private final ComponentTypes componentType;
	
	public UserActionDto(User user, String newValue, String url, String action, String componentType) {
		this.user = user;
		this.newValue = newValue;
		this.url = url;
		this.action = UserActionEnum.valueOf(action);
		this.componentType = ComponentTypes.valueOf(componentType);
	}
	
	public User getUser() {
		return user;
	}
	
	public String getNewValue() {
		return newValue;
	}
	
	public String getUrl() {
		return url;
	}
	
	public UserActionEnum getAction() {
		return action;
	}
	
	public ComponentTypes getComponentType() {
		return componentType;
	}
}
