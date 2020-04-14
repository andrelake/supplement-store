package com.andrelake.supplementstore.domain.exceptions;

public class CustomerNotFoundException extends EntityNotFoundException{

	private static final long serialVersionUID = 1L;

	public CustomerNotFoundException(String msg) {
		super(msg);
	}
	
	public CustomerNotFoundException(Long id) {
		super(String.format("Can't find a customer with id %d", id));
	}
}
