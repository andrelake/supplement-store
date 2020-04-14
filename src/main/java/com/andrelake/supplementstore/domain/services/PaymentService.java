package com.andrelake.supplementstore.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.andrelake.supplementstore.domain.exceptions.PaymentInUseException;
import com.andrelake.supplementstore.domain.exceptions.PaymentNotFoundException;
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
			throw new PaymentNotFoundException(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new PaymentInUseException(id);
		}
	}
	
	public Payment findOrFail(Long id) {
		
		return paymentRepository.findById(id)
				.orElseThrow(() -> new PaymentNotFoundException(id));
	}
}
