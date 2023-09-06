package com.amigoscode.customer;

import com.amigoscode.Main;
import com.amigoscode.exception.DuplicateResourceException;
import com.amigoscode.exception.RequestValidationException;
import com.amigoscode.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class CustomerService {

    private final CustomerDAO customerDAO;
    private static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    public CustomerService(@Qualifier("jdbc") CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }

    public List<Customer> getAllCustomers(){
        return customerDAO.selectAllCustomer();
    }

    public Customer getCustomer(Long id){
        return customerDAO.selectCustomerById(id).orElseThrow(() -> new ResourceNotFoundException("Customer resource not found"));
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        if(customerDAO.existsPersonWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("Email already exists");
        }
        if(customerRegistrationRequest.gender().isEmpty()){
            throw new RequestValidationException("Please specify 'Male', 'Female' or 'Other' for your gender");
        }

        Customer customerToInsert = new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age(),
                customerRegistrationRequest.gender()
        );

        LOGGER.info(customerToInsert.toString());


        customerDAO.insertCustomer(customerToInsert);
    }

    public void deleteCustomer(Long id) {
        if(!customerDAO.existsPersonWithId(id)) throw new ResourceNotFoundException("Customer not found");
        customerDAO.deleteCustomerWithId(id);
    }

    public void updateCustomerInfo(Long id, CustomerUpdateRequest customerUpdateRequest){
        Customer customerToUpdate = getCustomer(id);

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

            customerToUpdate.setGender(Customer.Gender.fromValue(customerUpdateRequest.gender()));
            changes = true;
        }
        if(!changes){
            throw new RequestValidationException("No changes found");
        }
        customerDAO.updateCustomer(customerToUpdate);
    }
}
