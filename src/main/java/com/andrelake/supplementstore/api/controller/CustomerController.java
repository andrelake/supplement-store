package com.andrelake.supplementstore.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.andrelake.supplementstore.domain.exceptions.EntityNotFoundException;
import com.andrelake.supplementstore.domain.exceptions.SupStoreNotFoundException;
import com.andrelake.supplementstore.domain.model.Customer;
import com.andrelake.supplementstore.domain.repository.CustomerRepository;
import com.andrelake.supplementstore.domain.services.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/customers")
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@GetMapping
	public List<Customer> findAll(){
		
		return customerRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Customer> findById(@PathVariable Long id) {
		
		Customer customer = customerService.findOrFail(id);
		
		return ResponseEntity.ok(customer);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Customer add(@RequestBody Customer customer) {
		
		try {
			return customerService.save(customer);
		}
		catch(EntityNotFoundException e) {
			throw new SupStoreNotFoundException(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Customer customer) {
		
		Customer oldCustomer = customerService.findOrFail(id);
		
		BeanUtils.copyProperties(customer, oldCustomer, "id", "supStore");
		
		customerService.save(oldCustomer);
		
		return ResponseEntity.ok(oldCustomer);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> columns) {
		
		Customer customer = customerService.findOrFail(id);
		
		Customer updatedCustomer = customerService.save(customer);
		
		merge(columns, updatedCustomer);
		
		return update(id, updatedCustomer);
	}

	private void merge(Map<String, Object> originData, Customer targetCustomer) {
		
		ObjectMapper objMapper = new ObjectMapper();
		Customer originCustomer = objMapper.convertValue(originData, Customer.class);
		
		originData.forEach((propertyName, propertyValue) -> {
			
			Field field = ReflectionUtils.findField(Customer.class, propertyName);
			field.setAccessible(true);
			
			Object newValue = ReflectionUtils.getField(field, originCustomer);
			
			ReflectionUtils.setField(field, targetCustomer, newValue);
		});
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		customerService.remove(id);
	}
}
