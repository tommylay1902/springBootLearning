package com.amigoscode.customer;

import com.amigoscode.jwt.JWTUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("")
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final JWTUtil jwtUtil;

    public CustomerController(CustomerService customerService, JWTUtil jwtUtil) {
        this.customerService = customerService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("")
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable("id") Long id){
       return customerService.getCustomer(id);
    }

    @PostMapping("")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerRegistrationRequest request){
        try{
            customerService.addCustomer(request);
            String jwtToken = jwtUtil.issueToken(request.email(), "ROLE_USER");
            return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwtToken).build();
        }catch (Exception e){
           e.printStackTrace();
           return ResponseEntity.badRequest().build();
        }

    }

    @DeleteMapping("/{id}")
    public void removeCustomer(@PathVariable("id") Long id){
        customerService.deleteCustomer(id);
    }

    @PutMapping("/{id}")
    public void updateCustomer(@PathVariable("id") Long id, @RequestBody CustomerUpdateRequest customerUpdateRequest){
        customerService.updateCustomerInfo(id, customerUpdateRequest);
    }


}
