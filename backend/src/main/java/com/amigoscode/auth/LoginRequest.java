package com.amigoscode.auth;

public record LoginRequest(
        String email,
        String password
) {}
