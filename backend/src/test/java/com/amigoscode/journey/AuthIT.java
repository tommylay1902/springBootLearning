package com.amigoscode.journey;

import com.amigoscode.auth.AuthResponse;
import com.amigoscode.auth.LoginRequest;
import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerDTO;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.amigoscode.exception.AuthenticationException;
import com.amigoscode.jwt.JWTUtil;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthIT {
    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private final String authURI = "/api/v1";
    private final String customerURI = "/api/v1/customers";
    private final JWTUtil jwtUtil = new JWTUtil();

    @Test
    void canLogin() {
        //Create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.firstName() + UUID.randomUUID() + "@gmail.com";
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];
        String password = "password";

        int age = RANDOM.nextInt(1,100);

        LoginRequest authRequest = new LoginRequest(email, password);
        webTestClient.post()
                .uri(authURI + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest), LoginRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        CustomerRegistrationRequest customerRequest = new CustomerRegistrationRequest(name, email, password, age, randomGender);

        webTestClient.post().uri(customerURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        Mono.just(customerRequest),
                        CustomerRegistrationRequest.class
                )                //.exchange is sending the request
                .exchange()
                .expectStatus()
                .is2xxSuccessful();


        EntityExchangeResult<AuthResponse> result = webTestClient.post()
                .uri(authURI + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest), LoginRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthResponse>() {
                })
                .returnResult();

        String jwtToken = result.getResponseHeaders().get(AUTHORIZATION).get(0);
        AuthResponse authResponse = result.getResponseBody();
        assertThat(jwtUtil.isTokenValid(
                jwtToken,
                authResponse.customerDTO().username())).isTrue();

        assertThat(authResponse.customerDTO().email()).isEqualTo(email);
        assertThat(authResponse.customerDTO().age()).isEqualTo(age);
        assertThat(authResponse.customerDTO().name()).isEqualTo(name);
        assertThat(authResponse.customerDTO().gender()).isEqualTo(randomGender);



    }


}
