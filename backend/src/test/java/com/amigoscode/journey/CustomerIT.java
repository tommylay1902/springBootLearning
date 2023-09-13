package com.amigoscode.journey;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.amigoscode.customer.CustomerUpdateRequest;
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
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {
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
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];


        int age = RANDOM.nextInt(1,100);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, "password", age, randomGender.getValue());
        //send a post request
        //    NEVER DO THIS!!! We never want to invoke the method directly from the controller
        //    @Autowired
        //    private CustomerController controller

        webTestClient.post().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)                //.exchange is sending the request
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

        Customer expectedCustomer = new Customer( name, email, "password", age, randomGender.getValue());
        //make sure that customer is present
        assertThat(customers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        //get customer by id
        assert customers != null;
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

        int age = RANDOM.nextInt(1,100);
        String gender = "Male";

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, "password", age, gender);
        //send a post request
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

    @Test
    void canUpdateCustomer(){
        //CREATE A CUSTOMER
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.firstName() + UUID.randomUUID() + "@gmail.com";

        int age = RANDOM.nextInt(1,100);

        Customer.Gender gender = Customer.Gender.MALE;



        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, "password", age, "Male");
        //send a post updateRequest
        webTestClient.post().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                //.exchange is sending the updateRequest
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

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

        //create update updateRequest
        Name fakerNameUpdate = faker.name();

        String nameUpdate = fakerNameUpdate.fullName();
        String emailUpdate = fakerNameUpdate.firstName() + UUID.randomUUID() + "@gmail.com";
        int ageUpdate = RANDOM.nextInt(1,100);
        String genderUpdate = "Female";


        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(nameUpdate, emailUpdate,  ageUpdate, genderUpdate);

        webTestClient.put().uri(baseURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        //get the updated customer
        Customer actual = webTestClient.get()
                .uri(baseURI + "/{id}", id)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();


        Customer expected = new Customer(id, nameUpdate, emailUpdate, "password", ageUpdate, genderUpdate);

        //test updateRequest to actual
        assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
    }
}
