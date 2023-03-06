package me.j4n8.projekt02backend.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(@NonNull String email);
	
	User findByUsername(@NonNull String username);
//    @Query("SELECT u FROM User u WHERE u.id = ?1 AND u.password = ?2")
//    User checkPassword(@NonNull Long id, @NonNull String password);
}