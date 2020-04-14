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
			
			SupplementStore actualSupStore = supService.findOrFail(id);
			
			BeanUtils.copyProperties(supStore, actualSupStore, "id");
				
			SupplementStore savedSupStore = supService.salvar(actualSupStore);
				
			return ResponseEntity.ok(savedSupStore);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> columns) {
		
		SupplementStore supStore = supService.findOrFail(id);
		
		merge(columns, supStore);
		
		return update(id, supStore);
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
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		supService.excluir(id);
	}
}
