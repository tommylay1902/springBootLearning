package com.amigoscode;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Random;


@SpringBootApplication

public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);

    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        Faker faker = new Faker();
        String nameOne = faker.name().fullName();
        String nameTwo = faker.name().fullName();

        Random r = new Random();

        return args -> {
            Customer customer1 = new Customer( nameOne, faker.internet().emailAddress(), r.nextInt(16, 29));
            Customer customer2 = new Customer( nameTwo,  faker.internet().emailAddress(), r.nextInt(16, 29));
            List<Customer> customers = List.of(customer1, customer2);

            customerRepository.saveAll(customers);

        };


    }
    
}
