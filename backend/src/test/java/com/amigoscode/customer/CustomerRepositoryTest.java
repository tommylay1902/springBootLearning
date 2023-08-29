package com.amigoscode.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.UUID;

import static com.amigoscode.AbstractTestcontainers.FAKER;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {


    private  Faker FAKER;
    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        FAKER = new Faker();
    }

//    @Test
//    void existsCustomerByEmail() {
//        // Given
//        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
//        Customer customer = new Customer(
//                FAKER.name().fullName(),
//                email,
//                20
//        );
//
//        underTest.save(customer);
//
//        // When
//        var actual = underTest.existsCustomerByEmail(email);
//
//        // Then
//        assertThat(actual).isTrue();
//    }
}