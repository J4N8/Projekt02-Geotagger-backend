package me.j4n8.projekt02backend.user_actions;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {
	List<UserAction> findTop100ByOrderByDateDesc();
}