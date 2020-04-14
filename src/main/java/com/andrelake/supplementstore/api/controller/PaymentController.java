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

import com.andrelake.supplementstore.domain.model.Payment;
import com.andrelake.supplementstore.domain.model.SupplementStore;
import com.andrelake.supplementstore.domain.repository.PaymentRepository;
import com.andrelake.supplementstore.domain.services.PaymentService;
import com.andrelake.supplementstore.domain.services.SupplementStoreService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/stores/payments")
public class PaymentController {

	@Autowired
	private PaymentRepository paymentRepository;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	private SupplementStoreService supService;
	
	@GetMapping
	public List<Payment> findAll(){
		
		return paymentRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public Payment findById(@PathVariable Long id) {
		
		return paymentService.findOrFail(id);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Payment add(@RequestBody Payment payment) {
		
		SupplementStore sup = supService.findOrFail(1L);
		
		payment.setSupStore(sup);
		
		return paymentService.save(payment);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Payment payment) {
		
		Payment actualPayment = paymentService.findOrFail(id);
		
		BeanUtils.copyProperties(payment, actualPayment, "id", "supStore");
			
		Payment savedPayment = paymentService.save(actualPayment);
			
		return ResponseEntity.ok(savedPayment);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> columns) {
		
		Payment updatedPayment = paymentService.findOrFail(id);
		
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
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		paymentService.remove(id);
	}
}
