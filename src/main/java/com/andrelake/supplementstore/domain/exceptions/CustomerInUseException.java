package com.andrelake.supplementstore.domain.exceptions;

public class CustomerInUseException extends EntityNotFoundException{

	private static final long serialVersionUID = 1L;

	public CustomerInUseException(String msg) {
		super(msg);
	}
	
	public CustomerInUseException(Long id) {
		super(String.format("Customer with id %d can't be deleted because has being used", id));
	}
}
