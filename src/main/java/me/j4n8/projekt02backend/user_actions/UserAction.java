package me.j4n8.projekt02backend.user_actions;

import jakarta.persistence.*;
import me.j4n8.projekt02backend.user.User;

import java.sql.Date;

@Entity
@Table(indexes = @Index(columnList = "date"), name = "user_action")
public class UserAction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition = "DATE DEFAULT CURRENT_DATE")
	private Date date;
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false, table = "user_action")
	private User user;
	
	@Column(nullable = true, name = "new_value")
	private String newValue;
	@Column(nullable = false)
	private String url;
	@Enumerated(EnumType.STRING)
	private UserActionEnum action;
	@Enumerated(EnumType.STRING)
	@Column(name = "component_type")
	private ComponentTypes componentType;
	
	public UserAction() {
	}
	
	public UserAction(User user, String newValue, String url, UserActionEnum action, ComponentTypes componentType) {
		this.user = user;
		this.newValue = newValue;
		this.url = url;
		this.action = action;
		this.componentType = componentType;
	}
	
	public UserAction(UserActionDto userActionDto) {
		this.user = userActionDto.getUser();
		this.newValue = userActionDto.getNewValue();
		this.url = userActionDto.getUrl();
		this.action = userActionDto.getAction();
		this.componentType = userActionDto.getComponentType();
	}
}
