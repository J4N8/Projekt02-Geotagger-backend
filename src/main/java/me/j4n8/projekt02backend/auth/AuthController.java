package me.j4n8.projekt02backend.auth;

import me.j4n8.projekt02backend.user.User;
import me.j4n8.projekt02backend.user.UserService;
import me.j4n8.projekt02backend.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {
        // authenticate user
        User user = userService.authenticateUser(userLoginDto.getEmail(), userLoginDto.getPassword());

        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // generate token
        String token = jwtTokenUtil.generateToken((UserDetails) user);

        // return token
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @GetMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto userRegisterDto) {
        User user = userService.registerUser(userRegisterDto.getEmail(), userRegisterDto.getPassword(), userRegisterDto.getUsername());

        String token = jwtTokenUtil.generateToken(userRegisterDto.getEmail());

        JwtAuthenticationResponse response = new JwtAuthenticationResponse(token);

        return ResponseEntity.ok(response);
    }
}
