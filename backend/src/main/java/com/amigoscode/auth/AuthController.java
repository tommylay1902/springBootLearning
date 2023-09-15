package com.amigoscode.auth;

import com.amigoscode.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class AuthController {
    private final AuthService authService;
    private final JWTUtil jwtUtil;

    public AuthController(AuthService authService, JWTUtil jwtUtil) {
        this.authService = authService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        AuthResponse res = authService.login(loginRequest);
        // IN FUTURE REMOVE THE PAYLOAD RETURN FROM THE BODY, JUST NEED TO RETURN THE TOKEN
        return ResponseEntity
                .ok()
                .header(HttpHeaders.AUTHORIZATION, res.token())
                .body(res);
    }
}
