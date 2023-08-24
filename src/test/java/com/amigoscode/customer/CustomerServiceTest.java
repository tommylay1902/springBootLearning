package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void willThrowWhenGetCustomerReturnsEmptyOptional() {
        //Given
        Long id = 1L;
        //tells the mock exactly what to do
        //so when we call the function
        //then we will return the optional of that customer
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer resource not found");
    }

    @Test
    void addCustomer() {
        //Given
        String email = "tommy@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("tommy", "tommy@gmail.com", 26);
        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        //these tests will make sure that the inputted data is what is actually saved to the database
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
    }

    @Test
    void willThrowWhenAddCustomerHasDuplicateEmail() {
        //Given
        String email = "tommy@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("tommy", "tommy@gmail.com", 26);

        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email not found");

        //Then
        verify(customerDao, never()).insertCustomer(any());

    }


    @Test
    void canDeleteCustomer() {
        //Given
        Long id = 1L;
        //When
        when(customerDao.existsPersonWithId(id)).thenReturn(true);
        underTest.deleteCustomer(id);
        //Then
        verify(customerDao).deleteCustomerWithId(id);
    }

    @Test
    void willThrowWhenIdNotUniqueDeleteCustomer() {
        //Given
        Long id = 1L;
        //When
        when(customerDao.existsPersonWithId(id)).thenReturn(false);
        //Then
        assertThatThrownBy(()-> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer not found");
    }

    @Test
    void updateCustomerInfo() {
        //Given

        //When

        //Then
    }
}