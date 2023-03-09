package me.j4n8.projekt02backend.user_actions;

import jakarta.persistence.*;
import me.j4n8.projekt02backend.user.User;

import java.time.LocalDateTime;

@Entity
@Table(indexes = @Index(columnList = "date_time"), name = "user_action")
public class UserAction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP", nullable = false, name = "date_time")
	private LocalDateTime dateTime;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserActionEnum action;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false, table = "user_action")
	private User user;
	
	@Column(nullable = true, name = "new_value")
	private String newValue;
	@Column(nullable = false)
	private String url;
	public UserAction(User user, String newValue, String url, UserActionEnum action, ComponentTypes componentType) {
		this.user = user;
		this.newValue = newValue;
		this.url = url;
		this.action = action;
		this.componentType = componentType;
	}
	@Enumerated(EnumType.STRING)
	@Column(name = "component_type")
	private ComponentTypes componentType;
	
	public UserAction() {
	}
	
	public UserAction(UserActionDto userActionDto) {
		this.user = userActionDto.getUser();
		this.newValue = userActionDto.getNewValue();
		this.url = userActionDto.getUrl();
		this.action = userActionDto.getAction();
		this.componentType = userActionDto.getComponentType();
	}
	
	@PrePersist
	public void prePersist() {
		dateTime = LocalDateTime.now();
	}
}
