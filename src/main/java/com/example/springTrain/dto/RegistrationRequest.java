package com.example.springTrain.dto;


//Nested DTO (wrapper dto)  
//wraps UserDto and CustomerDto
public class RegistrationRequest {

	private UserDto userDto;
	private CustomerDto customerDto;
	
	public UserDto getUserDto() {
		return userDto;
	}
	public void setUserDto(UserDto userDto) {
		this.userDto = userDto;
	}
	public CustomerDto getCustomerDto() {
		return customerDto;
	}
	public void setCustomerDto(CustomerDto customerDto) {
		this.customerDto = customerDto;
	}
	
	
}