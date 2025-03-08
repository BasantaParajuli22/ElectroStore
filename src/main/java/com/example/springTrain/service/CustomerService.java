package com.example.springTrain.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.CustomerDto;
import com.example.springTrain.model.Customer;
import com.example.springTrain.model.Order;
import com.example.springTrain.model.User;
import com.example.springTrain.repository.CustomerRepository;
import com.example.springTrain.repository.OrderRepository;
import com.example.springTrain.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class CustomerService {
	
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerService(CustomerRepository customerRepository,
    		UserRepository userRepository,
    		OrderRepository orderRepository) {
    	this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }
    
    
    public void authenticateCustomer(Long id, Authentication authentication) throws Exception {

//        String authenticatedEmail = authentication.getName();
//        System.out.println("Authenticated Email: " + authenticatedEmail);
//
//        Long authenticatedId = customerRepository.findIdByUser_Email(authenticatedEmail);
//        if (authenticatedId == null) {
//            System.err.println("Customer not found by email: " + authenticatedEmail);
//            throw new Exception("Customer not found by email: " + authenticatedEmail);
//        }

    	Long authenticatedId = getAuthentcatedId(authentication);
        System.out.println("Authenticated ID from DB: " + authenticatedId);
        System.out.println("ID from URL: " + id);

        // Check if the authenticated ID matches the ID from the URL
        if (!authenticatedId.equals(id)) {
            System.err.println("Unauthorized access: Customer ID does not match authenticated ID");
            throw new Exception("Unauthorized access: Customer ID does not match authenticated ID");
        }
    }
    
    public Long getAuthentcatedId( Authentication authentication) throws Exception {

        String authenticatedEmail = authentication.getName();

        Long authenticatedId = customerRepository.findIdByUser_Email(authenticatedEmail);
        if (authenticatedId == null) {
            System.err.println("Customer not found by email: " + authenticatedEmail);
            throw new Exception("Customer not found by email: " + authenticatedEmail);
        }
		return authenticatedId;
        
    }
    
    public List<Customer> getAllCustomers() {
    	return customerRepository.findAll();
    }
    public Customer getCustomerById(Long id) throws Exception {
        return customerRepository.findById(id)
        		.orElseThrow(() -> new Exception("Customer not found"));
    }

    @Transactional
    public Customer createCustomer(CustomerDto customerDto, User user) throws Exception {
    	
    	if(customerDto == null || user == null) {
    		throw new Exception("Customer cannot be created received null values");
    	}
    	Customer customer = new Customer();
    	customer.setUser(user);//setting user to customer
    	customer.setFirstName(customerDto.getFirstName());
    	customer.setAddress(customerDto.getAddress());
    	customer.setLastName(customerDto.getLastName());
    	customer.setPhoneNumber(customerDto.getPhoneNumber());
        return customerRepository.save(customer);
    }

    @Transactional
    public Customer updateCustomer(Long id, CustomerDto customerDto) throws Exception {
    	customerRepository.findById(id)
    		.orElseThrow(() -> new RuntimeException("Customer not found"));
    	
        Customer customer = getCustomerById(id);
        customer.setFirstName(customerDto.getFirstName());
        customer.setLastName(customerDto.getLastName());
        customer.setPhoneNumber(customerDto.getPhoneNumber());
        customer.setAddress(customerDto.getAddress());
        return customerRepository.save(customer);
    }

    @Transactional
    public void deleteCustomer(Long id) throws Exception {
    	Customer customer = customerRepository.findById(id)
		.orElseThrow(() -> new RuntimeException("Customer not found"));
    	
    	orderRepository.deleteAll(customer.getOrders());

    	userRepository.findById(id)
		.orElseThrow(() -> new Exception("User not found"));
    	    	
    	customerRepository.deleteById(id);
    	userRepository.deleteById(id);
    }
	
	public List<Order> getOrdersByCustomerId(Long id) throws Exception {
		Customer customer = customerRepository.findById(id)
				.orElseThrow(() -> new Exception("Customer not found by "+ id));
		
		List<Order> orders = orderRepository.findAllByCustomer_Id(id);
		if (orders == null) {
            System.err.println("Orders not found by customerId: " + id);
            throw new Exception("Orders not found by customerId: \" + id");
		}
		return orders;
	}
}
