package com.andrelake.supplementstore.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.andrelake.supplementstore.domain.exceptions.EntityInUseException;
import com.andrelake.supplementstore.domain.exceptions.EntityNotFoundException;
import com.andrelake.supplementstore.domain.model.SupplementStore;
import com.andrelake.supplementstore.domain.repository.SupplementStoreRepository;

@Service
public class SupplementStoreService {

	@Autowired
	private SupplementStoreRepository supplementRepository;
	
	public SupplementStore salvar(SupplementStore supplementStore) {
		
		return supplementRepository.save(supplementStore);
	}
	
	public void excluir(Long id) {
		
		try {
			supplementRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new EntityNotFoundException(String.format("Can't find any store with id %d", id));
		}
		catch(DataIntegrityViolationException e) {
			throw new EntityInUseException(String.format("Store with id %d is being used", id));
		}
	}
}
