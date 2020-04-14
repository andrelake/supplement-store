package com.andrelake.supplementstore.domain.exceptions;

public class SupStoreNotFoundException extends EntityNotFoundException{

	private static final long serialVersionUID = 1L;

	public SupStoreNotFoundException(String msg) {
		super(msg);
	}
	
	public SupStoreNotFoundException(Long id) {
		super(String.format("Can't find a supplement store with id %d", id));
	}
}
