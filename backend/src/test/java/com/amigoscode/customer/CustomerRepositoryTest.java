package com.amigoscode.customer;

import com.amigoscode.TestConfig;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class CustomerRepositoryTest {


    private  Faker FAKER;
    @Autowired
    private CustomerRepository underTest;

    @BeforeEach
    void setUp() {
        FAKER = new Faker();
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];

        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                "password",
                20,
                randomGender.getValue()
        );

        underTest.save(customer);

        // When
        var actual = underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isTrue();
    }
}