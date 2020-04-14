package com.andrelake.supplementstore.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.andrelake.supplementstore.domain.exceptions.CustomerInUseException;
import com.andrelake.supplementstore.domain.exceptions.CustomerNotFoundException;
import com.andrelake.supplementstore.domain.model.Customer;
import com.andrelake.supplementstore.domain.model.SupplementStore;
import com.andrelake.supplementstore.domain.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private SupplementStoreService supService;
	
	public Customer save(Customer customer) {
		
		Long supStoreId = customer.getSupStore().getId();
		
		SupplementStore supStore = supService.findOrFail(supStoreId);
		
		customer.setSupStore(supStore);
		
		return customerRepository.save(customer);
	}
	
	public void remove(Long id) {
		
		try {
			customerRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new CustomerNotFoundException(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new CustomerInUseException(id);
		}
	}
	
	public Customer findOrFail(Long id) {
		
		return customerRepository.findById(id)
				.orElseThrow(() -> new CustomerNotFoundException(id));
	}
}
