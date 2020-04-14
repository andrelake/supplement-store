package com.andrelake.supplementstore.domain.exceptions;

public class SupStoreInUseException extends EntityInUseException{

	private static final long serialVersionUID = 1L;

	public SupStoreInUseException(String msg) {
		super(msg);
	}
	
	public SupStoreInUseException(Long id) {
		super(String.format("Supplement store with id %d can't be deleted because has being used", id));
	}
}
