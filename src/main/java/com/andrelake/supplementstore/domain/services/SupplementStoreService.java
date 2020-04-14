package com.andrelake.supplementstore.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.andrelake.supplementstore.domain.exceptions.SupStoreInUseException;
import com.andrelake.supplementstore.domain.exceptions.SupStoreNotFoundException;
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
			throw new SupStoreNotFoundException(id);
		}
		catch(DataIntegrityViolationException e) {
			throw new SupStoreInUseException(id);
		}
	}
	
	public SupplementStore findOrFail(Long id) {
		
		return supplementRepository.findById(id)
				.orElseThrow(() -> new SupStoreNotFoundException(id));
	}
}
