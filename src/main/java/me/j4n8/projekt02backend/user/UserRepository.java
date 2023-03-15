package me.j4n8.projekt02backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(@NonNull String email);
	
	User findByUsername(@NonNull String username);
	
	boolean existsByUsername(@NonNull String username);
}