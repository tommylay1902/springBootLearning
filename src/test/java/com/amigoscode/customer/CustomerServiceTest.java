package com.amigoscode.customer;

import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDao;
    private CustomerService underTest;
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        verify(customerDao).selectAllCustomer();
    }

    @Test
    void canGetCustomer() {
        //Given
        Long id = 1L;
        Customer customer = new Customer(id, "tommy", "tommy@gmail.com", 22);
        //tells the mock exactly what to do
        //so when we call the function
        //then we will return the optional of that customer
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        //When

        Customer actual = underTest.getCustomer(id);
        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //Given
        Long id = 1L;
        //tells the mock exactly what to do
        //so when we call the function
        //then we will return the optional of that customer
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer resource not found");
    }

    @Test
    void canInsertCustomer() {
        //Given
        Customer customer = new Customer(1L, "tommy", "tommylay@gmail.com", 22);

        //When

        //Then

    }

    @Test
    void deleteCustomer() {
        //Given

        //When

        //Then
    }

    @Test
    void updateCustomerInfo() {
        //Given

        //When

        //Then
    }
}