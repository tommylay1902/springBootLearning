package com.amigoscode;

import com.amigoscode.customer.Customer;
import com.amigoscode.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

@SpringBootApplication
@EnableScheduling
public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private final RestTemplate restTemplate;

    public Main(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        Faker faker = new Faker();
        String nameOne = faker.name().fullName();
        String nameTwo = faker.name().fullName();

        Random r = new Random();

        Customer.Gender randomGender = Customer.Gender.values()[r.nextInt(Customer.Gender.values().length)];
        LOGGER.info(randomGender.getValue());

        return args -> {
            Customer customer1 = new Customer( nameOne, faker.internet().emailAddress(), r.nextInt(16, 29), randomGender.getValue());
            Customer customer2 = new Customer( nameTwo,  faker.internet().emailAddress(), r.nextInt(16, 29), randomGender.getValue());
            List<Customer> customers = List.of(customer1, customer2);
            customerRepository.saveAll(customers);
        };


    }
    
}
