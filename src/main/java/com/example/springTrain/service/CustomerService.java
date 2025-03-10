package com.example.springTrain.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.springTrain.dto.CustomerDto;
import com.example.springTrain.exceptions.CreationFailedException;
import com.example.springTrain.exceptions.DeleteFailedException;
import com.example.springTrain.exceptions.ResourceNotFoundException;
import com.example.springTrain.exceptions.UnauthorizedAccessException;
import com.example.springTrain.exceptions.UpdateFailedException;
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
    
  
//      String authenticatedEmail = authentication.getName();
//        System.out.println("Authenticated Email: " + authenticatedEmail);
//
//        Long authenticatedId = customerRepository.findIdByUser_Email(authenticatedEmail);
//        if (authenticatedId == null) {
//            System.err.println("Customer not found by email: " + authenticatedEmail);
//            throw new Exception("Customer not found by email: " + authenticatedEmail);
//        }

    public void authenticateCustomer(Long id, Authentication authentication) {
    	
		Long authenticatedId;
		authenticatedId = getAuthentcatedId(authentication);
		if (!authenticatedId.equals(id)) {
			throw new UnauthorizedAccessException("Unauthorized access: Customer ID does not match authenticated ID");
		}
		
    }
    
    public Long getAuthentcatedId( Authentication authentication) {

		String authenticatedEmail = authentication.getName();
        Long authenticatedId = customerRepository.findIdByUser_Email(authenticatedEmail);
        if (authenticatedId == null) {
            throw new ResourceNotFoundException("Customer not found by email: " + authenticatedEmail);
        }
		return authenticatedId;
    }
    
    public List<Customer> getAllCustomers() {
    	return customerRepository.findAll();
    }
    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
        		.orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    @Transactional
    public Customer createCustomer(CustomerDto customerDto, User user) throws Exception {
    	
    	if(customerDto == null || user == null) {
    		throw new Exception("CustomerDto and user are null cannot create customer");
    	}
	    try {
	    	Customer customer = new Customer();
	    	customer.setUser(user);//setting user to customer
	    	customer.setFirstName(customerDto.getFirstName());
	    	customer.setAddress(customerDto.getAddress());
	    	customer.setLastName(customerDto.getLastName());
	    	customer.setPhoneNumber(customerDto.getPhoneNumber());
	        return customerRepository.save(customer);
		} catch (Exception e) {
            throw new CreationFailedException("Customer Creation Failed " + e.getMessage());
		}
    	
    }

    @Transactional
    public Customer updateCustomer(Long id, CustomerDto customerDto)  {
    	try {
    		customerRepository.findById(id)
    		.orElseThrow(() -> new RuntimeException("Customer not found"));
    	
	        Customer customer = getCustomerById(id);
	        customer.setFirstName(customerDto.getFirstName());
	        customer.setLastName(customerDto.getLastName());
	        customer.setPhoneNumber(customerDto.getPhoneNumber());
	        customer.setAddress(customerDto.getAddress());
	        return customerRepository.save(customer);
		} catch (Exception e) {
            throw new UpdateFailedException("Customer update Failed " + e.getMessage());
		}
    	
    }

    @Transactional
    public void deleteCustomer(Long id) {
			
    	try {
    		Customer customer = customerRepository.findById(id)
    				.orElseThrow(() -> new RuntimeException("Customer not found"));
    		
    		userRepository.findById(id)
    		.orElseThrow(() -> new ResourceNotFoundException("User not found"));
    		
    		orderRepository.deleteAll(customer.getOrders());
    		customerRepository.deleteById(id);
    		userRepository.deleteById(id);
		} catch (Exception e) {
            throw new DeleteFailedException("Customer deletion Failed " + e.getMessage());
		}
		
    }
	
	public List<Order> getOrdersByCustomerId(Long id) {
		
		try {
			Customer customer = customerRepository.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Customer not found by "+ id));
			List<Order> orders = orderRepository.findByCustomer_Id(id);	
			if (orders == null) {
				throw new ResourceNotFoundException("Orders not found by customerId: \" + id");
			}
			return orders;
		} catch (Exception e) {
            throw new ResourceNotFoundException("Customer orders couldnot be Found " + e.getMessage());
		}
	}
	
	
}
