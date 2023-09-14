package com.amigoscode.customer;

public record CustomerDTO(
        Long id,
        String name,
        String email,
        Customer.Gender gender,
        Integer age,
        String username
) {}
