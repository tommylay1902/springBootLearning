package com.amigoscode.customer;

import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;
    private final CustomerDTOMapper customerDTOMapper;
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());
    private final PasswordEncoder passwordEncoder;
    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO, CustomerDTOMapper customerDTOMapper, PasswordEncoder passwordEncoder) {
        this.customerDAO = customerDAO;
        this.customerDTOMapper = customerDTOMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getAllCustomers(){
        return customerDAO.selectAllCustomer().stream().map(customerDTOMapper).collect(Collectors.toList());
    }

    public CustomerDTO getCustomer(Long id){
        return customerDAO.selectCustomerById(id)
                .map(customerDTOMapper)
                .orElseThrow(() -> new ResourceNotFoundException("Customer resource not found"));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        if(customerDAO.existsPersonWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("Email already exists");
        }

        Customer customerToInsert = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                passwordEncoder.encode(customerRegistrationRequest.password()),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );

        customerDAO.insertCustomer(customerToInsert);
    }

    public void deleteCustomer(Long id) {
        if(!customerDAO.existsPersonWithId(id)) throw new ResourceNotFoundException("Customer not found");
        customerDAO.deleteCustomerWithId(id);
    }

    public void updateCustomerInfo(Long id, CustomerUpdateRequest customerUpdateRequest){
        Customer customerToUpdate = customerDAO.selectCustomerById(id).orElseThrow(() ->  new ResourceNotFoundException("customer not found"));

        boolean changes = false;

        if(
                customerUpdateRequest.name() != null
                && !customerUpdateRequest.name().equals(customerToUpdate.getName())
        ){
            customerToUpdate.setName(customerUpdateRequest.name());
            changes = true;
        }

        if(
                customerUpdateRequest.email() != null
                && !customerUpdateRequest.email().equals(customerToUpdate.getEmail())
        ){
            if(customerDAO.existsPersonWithEmail(customerUpdateRequest.email())){
                throw new DuplicateResourceException("Email is already taken");
            }
            customerToUpdate.setEmail(customerUpdateRequest.email());
            changes = true;
        }
        if(
                customerUpdateRequest.age() !=  null
                && !customerUpdateRequest.age().equals(customerToUpdate.getAge())
        ){
            customerToUpdate.setAge(customerUpdateRequest.age());
            changes=true;
        }

        if(
                customerUpdateRequest.gender() != null
                && !customerUpdateRequest.gender().equals(customerToUpdate.getGender())
        ){

            customerToUpdate.setGender(customerUpdateRequest.gender());
            changes = true;
        }
        if(!changes){
            throw new RequestValidationException("No changes found");
        }
        customerDAO.updateCustomer(customerToUpdate);
    }
}
