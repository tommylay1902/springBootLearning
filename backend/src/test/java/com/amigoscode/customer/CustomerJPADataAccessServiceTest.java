package com.amigoscode.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {


    private CustomerJPADataAccessService underTest;
    @Mock
    private CustomerRepository customerRepository;

    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomer() {
        //When
        underTest.selectAllCustomer();
        //Then
        Mockito.verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        Long id = 1L;
        //When
        underTest.selectCustomerById(id);
        //Then
        Mockito.verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        //Given
        Customer customer = new Customer(1L, "tommy", "t@gmail.com", 2);

        //When
        underTest.insertCustomer(customer);
        //Then
        Mockito.verify(customerRepository).save(customer);
    }

//    @Test
//    void existsPersonWithEmail() {
//        //Given
//        String email = "t@gmail.com";
//        //When
//        underTest.existsPersonWithEmail(email);
//        //Then
//        Mockito.verify(customerRepository).existsCustomerByEmail(email);
//    }

    @Test
    void existsPersonWithId() {
        //Given
        Long id = 2L;
        //When
        underTest.existsPersonWithId(id);
        //Then

        Mockito.verify(customerRepository).existsById(id);
    }

    @Test
    void deleteCustomerWithId() {
        //Given
        Long id = 2L;
        //When
        underTest.deleteCustomerWithId(id);
        //Then

        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer customer = new Customer(1L, "tommy", "t@gmail.com", 2);

        //When
        underTest.updateCustomer(customer);
        //Then

        Mockito.verify(customerRepository).save(customer);
    }
}