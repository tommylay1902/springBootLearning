package com.amigoscode.customer;

import com.amigoscode.AbstractTestcontainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainers {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp(){
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomer() {
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];

        //given
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID(),
                20,
                randomGender.getValue()
        );
        underTest.insertCustomer(customer);

        //when
        List<Customer> customers = underTest.selectAllCustomer();
        //then

        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];

        //given
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                randomGender.getValue()
        );

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //when
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
            assertThat(c.getGender()).isEqualTo(customer.getGender());
        });
    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        //given
        Long id = -1L;

        //when
        var actual = underTest.selectCustomerById(id);

        //then

        assertThat(actual).isEmpty();
    }


    @Test
    void existsPersonWithEmail() {
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];

        //given
        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                randomGender.getValue()
        );

        underTest.insertCustomer(customer);

        //when
        boolean exists = underTest.existsPersonWithEmail(email);
        //then

        assertThat(exists).isTrue();
    }

    @Test
    void willReturnFalseExistsPersonWithEmail(){
        //given
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];

        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                randomGender.getValue()
        );

        underTest.insertCustomer(customer);

        String wrongEmail = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        // when
        boolean exists = underTest.existsPersonWithEmail(wrongEmail);

        //then

        assertThat(exists).isFalse();

    }

    @Test
    void existsPersonWithId() {
        //given
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];

        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                randomGender.getValue()
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //when
        boolean exists = underTest.existsPersonWithId(id);

        //then
        assertThat(exists).isTrue();

    }

    @Test
    void willReturnFalseExistsPersonWithId() {
        //given
        Long id = -1L;
        //when
        boolean exists = underTest.existsPersonWithId(id);

        //then
        assertThat(exists).isFalse();

    }

    @Test
    void deleteCustomerWithId() {
        //given
        Random random = new Random();
        Customer.Gender randomGender = Customer.Gender.values()[random.nextInt(Customer.Gender.values().length)];

        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                randomGender.getValue()
        );
        underTest.insertCustomer(customer);
        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        //when
        underTest.deleteCustomerWithId(id);
        Optional<Customer> actual = underTest.selectCustomerById(id);
        //then
        assertThat(actual).isNotPresent();
    }


    @Test
    void updateCustomer() {
        //given
        Customer.Gender gender = Customer.Gender.MALE;

        String email = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20,
                gender.getValue()
        );

        underTest.insertCustomer(customer);

        Long id = underTest.selectAllCustomer()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        //when
        String emailUpdate = FAKER.internet().safeEmailAddress() + "-"+ UUID.randomUUID();
        String nameUpdate = "Tommy Tommerton";
        int age = 10;
        Customer.Gender genderUpdate = Customer.Gender.FEMALE;

        Customer update = new Customer(id, emailUpdate, nameUpdate, age, genderUpdate.getValue());

        underTest.updateCustomer(update);

        Optional<Customer> actual = underTest.selectCustomerById(id);

        //then

        assertThat(actual).isPresent().hasValue(update);
    }
}