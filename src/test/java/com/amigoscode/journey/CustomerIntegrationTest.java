package com.amigoscode.journey;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerController;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;
    private static final Random RANDOM = new Random();
    private final String baseURI = "/api/v1/customers";
    @Test
    void canRegisterACustomer() {
        //Create a registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.firstName() + UUID.randomUUID() + "@gmail.com";

        Integer age = RANDOM.nextInt(1,100);


        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        //send a post request
        //    NEVER DO THIS!!! We never want to invoke the method directly from the controller
        //    @Autowired
        //    private CustomerController customerController

        webTestClient.post().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                //.exchange is sending the request
                .exchange()
                .expectStatus()
                .is2xxSuccessful();
        //get all customers
        List<Customer> customers = webTestClient.get()
                .uri("/api/v1/customers")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        Customer expectedCustomer = new Customer( name, email, age);
        //make sure that customer is present
        assertThat(customers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);


        //get customer by id
        Long id = customers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        expectedCustomer.setId(id);
        webTestClient.get()
                .uri(baseURI + "/{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);
    }
    @Test
    void canDeleteCustomer() {

        //CREATE A CUSTOMER
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.firstName() + UUID.randomUUID() + "@gmail.com";

        Integer age = RANDOM.nextInt(1,100);


        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, age);
        //send a post request
        webTestClient.post().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                //.exchange is sending the request
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        //create the expected customer
        Customer expectedCustomer = new Customer( name, email, age);

        //get all customers
        List<Customer> customers = webTestClient.get()
                .uri(baseURI)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        //get customer by id
        assert customers != null;
        Long id = customers.stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        //delete customer
        webTestClient.delete().uri(baseURI + "/{id}", id).exchange().expectStatus().is2xxSuccessful();

        //get customers list after delete
        webTestClient.get()
                .uri(baseURI + "/{id}", id)
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }
}
