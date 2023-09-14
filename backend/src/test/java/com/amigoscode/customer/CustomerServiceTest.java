package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerDAO customerDao;
    @Mock
    private PasswordEncoder passwordEncoder;

    private final CustomerDTOMapper customerDTOMapper = new CustomerDTOMapper();
    private CustomerService underTest;
    AutoCloseable autoCloseable;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao, customerDTOMapper, passwordEncoder);
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
    void canGetCustomer() {
        //Given
        Long id = 10L;
        Customer customer = new Customer(id, "alex", "alex@gmail.com", "password", 19, Customer.Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerDTO expected = customerDTOMapper.apply(customer);
        //When
        CustomerDTO actual = underTest.getCustomer(id);
        //Then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addCustomer() {
        //Given
        String email = "tommy@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("tommy", "tommy@gmail.com", "password", 26, Customer.Gender.MALE);
        String passwordHash = "2349043209";
        when(passwordEncoder.encode("password")).thenReturn(passwordHash);
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
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());
        assertThat(capturedCustomer.getPassword()).isEqualTo(passwordHash);
    }

    @Test
    void willThrowWhenAddCustomerHasDuplicateEmail() {
        //Given
        String email = "tommy@gmail.com";

        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest("tommy", "tommy@gmail.com", "password", 26, Customer.Gender.MALE);

        //When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already exists");

        //Then
        verify(customerDao, never()).insertCustomer(any());

    }


    @Test
    void canDeleteCustomer() {
        //Given
        Long id = 1L;
        when(customerDao.existsPersonWithId(id)).thenReturn(true);
        //When
        underTest.deleteCustomer(id);
        //Then
        verify(customerDao).deleteCustomerWithId(id);
    }

    @Test
    void willThrowWhenIdNotUniqueDeleteCustomer() {
        //Given
        Long id = 1L;
        when(customerDao.existsPersonWithId(id)).thenReturn(false);

        //When
        assertThatThrownBy(()-> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Customer not found");
        //Then
        verify(customerDao, never()).deleteCustomerWithId(any());
    }

    @Test
    void canUpdateAllCustomerInfo() {

        //Given
        Long id = 2L;
        Customer customer = new Customer(id, "tommy", "tommy@gmail.com", "password", 22, Customer.Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "tommylay@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest("Tommy Lay", "tommylay@gmail.com", 20, Customer.Gender.OTHER);
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        //When
        underTest.updateCustomerInfo(id, request);


        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());
    }
    @Test
    void canUpdateNameCustomerInfo() {

        //Given
        Long id = 2L;
        Customer customer = new Customer(id, "tommy", "tommy@gmail.com", "password", 22, Customer.Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String name = "Tommy Lay";
        CustomerUpdateRequest request = new CustomerUpdateRequest(name, null, null, Customer.Gender.MALE);

        //When
        underTest.updateCustomerInfo(id, request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());


    }
    @Test
    void canUpdateEmailCustomerInfo() {

        //Given
        Long id = 2L;
        Customer customer = new Customer(id, "tommy", "tommy@gmail.com", "password", 22, Customer.Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail = "tommylay@gmail.com";
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, "tommylay@gmail.com", null, Customer.Gender.MALE);
        //When
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        underTest.updateCustomerInfo(id, request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getGender()).isEqualTo(customer.getGender());
    }

    @Test
    void canUpdateAgeCustomerInfo() {

        //Given
        Long id = 2L;
        Customer.Gender gender = Customer.Gender.MALE;
        Customer customer = new Customer(id, "tommy", "tommy@gmail.com", "password", 22, gender);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String name = "Tommy Lay";
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, null, 300, Customer.Gender.MALE);

        //When
        underTest.updateCustomerInfo(id, request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getGender()).isEqualTo(gender);


    }

    @Test
    void canUpdateGenderCustomerInfo() {

        //Given
        Long id = 2L;
        Customer customer = new Customer(id, "tommy", "tommy@gmail.com", "password", 22, Customer.Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String name = "Tommy Lay";
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, null, null, Customer.Gender.FEMALE);

        //When
        underTest.updateCustomerInfo(id, request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();
        assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(capturedCustomer.getGender()).isEqualTo(request.gender());

    }
    @Test
    void willThrowWhenNoChangesUpdateCustomerInfo() {

        //Given
        Long id = 2L;
        Customer customer = new Customer(id, "tommy", "tommy@gmail.com", "password", 22, Customer.Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerUpdateRequest request = new CustomerUpdateRequest("tommy", "tommy@gmail.com", 22, Customer.Gender.MALE);
        //When
        assertThatThrownBy(() -> underTest.updateCustomerInfo(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No changes found");

        //Then
    }

    @Test
    void willThrowWhenEmailExistsCustomerInfo() {

        //Given
        Long id = 2L;
        Customer customer = new Customer(id, "tommy", "tommy@gmail.com", "password", 22, Customer.Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerUpdateRequest request = new CustomerUpdateRequest(null, "tommylay@gmail.com", null, null);

        //When
        when(customerDao.existsPersonWithEmail(request.email())).thenReturn(true);

        //Then
        assertThatThrownBy(() -> underTest.updateCustomerInfo(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email is already taken");
    }

}