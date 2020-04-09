package com.andrelake.supplementstore.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.andrelake.supplementstore.domain.exceptions.EntityInUseException;
import com.andrelake.supplementstore.domain.exceptions.EntityNotFoundException;
import com.andrelake.supplementstore.domain.model.Payment;
import com.andrelake.supplementstore.domain.repository.PaymentRepository;

@Service
public class PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;
	
	public Payment save(Payment payment) {
		
		return paymentRepository.save(payment);
	}
	
	public void remove(Long id) {
		
		try {
			paymentRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(String.format("Cannot find payment with id %d", id));
		}
		catch(DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format("Cannot delete because payment with id %d is being used", id));
		}
	}
}
