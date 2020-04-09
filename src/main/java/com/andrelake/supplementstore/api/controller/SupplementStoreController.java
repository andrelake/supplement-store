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
import org.springframework.web.bind.annotation.RestController;

import com.andrelake.supplementstore.domain.exceptions.EntityInUseException;
import com.andrelake.supplementstore.domain.exceptions.EntityNotFoundException;
import com.andrelake.supplementstore.domain.model.SupplementStore;
import com.andrelake.supplementstore.domain.repository.SupplementStoreRepository;
import com.andrelake.supplementstore.domain.services.SupplementStoreService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/stores")
public class SupplementStoreController {

	@Autowired
	private SupplementStoreRepository supRepository;
	
	@Autowired
	private SupplementStoreService supService;
	
	@GetMapping
	public List<SupplementStore> findAll(){
		
		return supRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SupplementStore> findById(@PathVariable Long id) {
		
		Optional<SupplementStore> sup = supRepository.findById(id);
		
		if(sup.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(sup.get());
	}
	
	@PostMapping
	public ResponseEntity<?> add(@RequestBody SupplementStore supStore) {
		try {
			
			supStore = supService.salvar(supStore);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(supStore);
		}
		catch(EntityNotFoundException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody SupplementStore supStore) {
		
		try {
			
			Optional<SupplementStore> actualSupStore = supRepository.findById(id);
			
			if(actualSupStore.isPresent()) {
				BeanUtils.copyProperties(supStore, actualSupStore.get(), "id");
				
				SupplementStore savedSupStore = supService.salvar(actualSupStore.get());
				
				return ResponseEntity.ok(savedSupStore);
			}
			return ResponseEntity.notFound().build();
		}
		catch(EntityNotFoundException e) {
			
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> columns) {
		
		Optional<SupplementStore> actualSupStore = supRepository.findById(id);
		
		if(actualSupStore.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		SupplementStore updatedSupStore = supService.salvar(actualSupStore.get());
		
		merge(columns, updatedSupStore);
		
		return update(id, updatedSupStore);
	}
	
	private void merge(Map<String, Object> originData, SupplementStore targetSupStore) {
		
		ObjectMapper objectMapper = new ObjectMapper();
		SupplementStore originSupStore = objectMapper.convertValue(originData, SupplementStore.class);
		
		originData.forEach((propertyName, propertyValue) -> {
			
			Field field = ReflectionUtils.findField(SupplementStore.class, propertyName);
			field.setAccessible(true);
			
			Object newValue = ReflectionUtils.getField(field, originSupStore);
			
			ReflectionUtils.setField(field, targetSupStore, newValue);
		});
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<SupplementStore> delete(@PathVariable Long id) {
		
		try {
			supService.excluir(id);
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
