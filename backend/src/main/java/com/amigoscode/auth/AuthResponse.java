package com.amigoscode.auth;

import com.amigoscode.customer.CustomerDTO;

public record AuthResponse(String token, CustomerDTO customerDTO) {
}
