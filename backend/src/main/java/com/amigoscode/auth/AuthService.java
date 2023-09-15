package com.amigoscode.auth;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerDTO;
import com.amigoscode.customer.CustomerDTOMapper;
import com.amigoscode.jwt.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDTOMapper customerDTOMapper;
    private final JWTUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, CustomerDTOMapper customerDTOMapper, JWTUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDTOMapper = customerDTOMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(LoginRequest login){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.email(), login.password()));
        Customer principal = (Customer) authentication.getPrincipal();
        CustomerDTO customer = customerDTOMapper.apply(principal);
        String token = jwtUtil.issueToken(customer.email(), List.of("ROLE_USER"));

        return new AuthResponse(token, customer);
    }
}
