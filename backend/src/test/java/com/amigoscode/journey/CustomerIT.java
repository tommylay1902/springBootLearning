package com.amigoscode.journey;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerDTO;
import com.amigoscode.customer.CustomerRegistrationRequest;
import com.amigoscode.customer.CustomerUpdateRequest;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, "password", age, randomGender);
        //send a post request
        //    NEVER DO THIS!!! We never want to invoke the method directly from the controller
        //    @Autowired
        //    private CustomerController controller

        String jwtToken = webTestClient.post().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION)
                .body(Mono.just(request), CustomerRegistrationRequest.class)                //.exchange is sending the request
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDTO> customers = webTestClient.get()
                .uri("/api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s",jwtToken))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

//        CustomerDTO expectedCustomer = new CustomerDTO( null, name, email, randomGender, age, email);
//        //make sure that customer is present
//        assertThat(customers)
//                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
//                .contains(expectedCustomer);

        //get customer by id
        assert customers != null;
        Long id = customers.stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();


        CustomerDTO expectedCustomer = new CustomerDTO( id, name, email, randomGender, age, email);
        //make sure that customer is present
        assertThat(customers)
                .contains(expectedCustomer);

        webTestClient.get()
                .uri(baseURI + "/{id}", id)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
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

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, "password", age, Customer.Gender.MALE);
        //send a post request
        String jwtToken = webTestClient.post().uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION)
                .body(Mono.just(request), CustomerRegistrationRequest.class)                //.exchange is sending the request
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDTO> customers = webTestClient.get()
                .uri(baseURI)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        //get customer by id
        assert customers != null;
        Long id = customers.stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        //delete customer
        webTestClient.delete()
                .uri(baseURI + "/{id}", id)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        //get customers list after delete
        webTestClient.get()
                .uri(baseURI + "/{id}", id)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .is4xxClientError();
    }

    @Test
    void canUpdateCustomer(){
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String name = fakerName.fullName();
        String email = fakerName.lastName() + "-" + UUID.randomUUID() + "@amigoscode.com";
        int age = RANDOM.nextInt(1, 100);

        Customer.Gender gender = age % 2 == 0 ? Customer.Gender.MALE : Customer.Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                name, email, "password", age, gender
        );

        // send a post request
        String jwtToken = webTestClient.post()
                .uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(AUTHORIZATION)
                .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(baseURI)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {
                })
                .returnResult()
                .getResponseBody();


        Long id = allCustomers.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();

        // update customer

        String newName = "Ali";

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                newName, null, null, gender
        );

        webTestClient.put()
                .uri(baseURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id
        CustomerDTO updatedCustomer = webTestClient.get()
                .uri(baseURI + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        CustomerDTO expected = new CustomerDTO(
                id,
                newName,
                email,
                gender,
                age,
                email);
        assertThat(updatedCustomer).isEqualTo(expected);
    }
}
