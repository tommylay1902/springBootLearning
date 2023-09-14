package com.amigoscode.customer;

import java.util.Objects;

public record CustomerRegistrationRequest(String name, String email, String password, Integer age, Customer.Gender gender) {
}
