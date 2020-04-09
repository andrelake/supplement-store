package com.andrelake.supplementstore.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.andrelake.supplementstore.domain.exceptions.EntityInUseException;
import com.andrelake.supplementstore.domain.exceptions.EntityNotFoundException;
import com.andrelake.supplementstore.domain.model.Payment;
import com.andrelake.supplementstore.domain.repository.PaymentRepository;
import com.andrelake.supplementstore.domain.services.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/stores/payments")
public class PaymentController {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private PaymentService paymentService;
	
	@GetMapping
	public List<Payment> findAll(){
		
		return paymentRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		
		Optional<Payment> payment = paymentRepository.findById(id);
		
		if(payment.isPresent()) {
			return ResponseEntity.ok(payment);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Payment> add(@RequestBody Payment payment) {
		
		payment = paymentService.save(payment);
		
		return ResponseEntity.ok(payment);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Payment payment) {
		
		Optional<Payment> actualPayment = paymentRepository.findById(id);
		
		if(actualPayment.isPresent()) {
			BeanUtils.copyProperties(payment, actualPayment.get(), "id");
			
			Payment savedPayment = paymentService.save(actualPayment.get());
			
			return ResponseEntity.ok(savedPayment);
		}
		return ResponseEntity.notFound().build();
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> columns) {
		
		Optional<Payment> actualPayment = paymentRepository.findById(id);
		
		if(actualPayment.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		Payment updatedPayment = paymentService.save(actualPayment.get());
		
		merge(columns, updatedPayment);
		
		return update(id, updatedPayment);
	}
	
	private void merge(Map<String, Object> originData, Payment targetPayment) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		Payment originPayment = objectMapper.convertValue(originData, Payment.class);
		
		originData.forEach((propertyName, propertyValue) -> {
			
			Field field = ReflectionUtils.findField(Payment.class, propertyName);
			field.setAccessible(true);
			
			Object newValue = ReflectionUtils.getField(field, originPayment);
			
			ReflectionUtils.setField(field, targetPayment, newValue);
		});
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Payment> delete(@PathVariable Long id) {
		
		try {
			paymentService.remove(id);
			return ResponseEntity.noContent().build();
		}
		catch(EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		catch(EntityInUseException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}
}
