//package com.amigoscode.customer;
//
//import org.springframework.stereotype.Repository;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Repository("list")
//public class CustomerListDataAccessService implements CustomerDAO {
//    private static List<Customer> customers;
//
//    static {
//        customers = new ArrayList<>();
//
//        customers.add(new Customer(1L, "alex", "a@gmail.com", 20));
//        customers.add(new Customer(2L, "tommy", "t@gmail.com", 26));
//    }
//
//    @Override
//    public List<Customer> selectAllCustomer() {
//        return customers;
//    }
//
//    @Override
//    public Optional<Customer> selectCustomerById(Long id) {
//        return customers.stream().filter(c -> c.getId().equals(id)).findFirst();
//    }
//
//    @Override
//    public void insertCustomer(Customer customer) {
//        customers.add(customer);
//    }
//
//    @Override
//    public boolean existsPersonWithEmail(String email) {
//        return customers.stream()
//                .anyMatch(c -> c.getEmail().equals(email));
//    }
//
//    @Override
//    public boolean existsPersonWithId(Long id) {
//        return customers.stream().anyMatch(c -> c.getId().equals(id));
//    }
//
//    @Override
//    public void deleteCustomerWithId(Long id) {
//        customers = customers.stream().filter(c -> !(c.getId().equals(id))).toList();
//    }
//
//    @Override
//    public void updateCustomer(Customer customer) {
//
//
//    }
//}