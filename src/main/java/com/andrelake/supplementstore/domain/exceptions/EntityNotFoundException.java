package com.andrelake.supplementstore.domain.exceptions;

public class EntityNotFoundException extends GenericException{

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String msg) {
		super(msg);
	}
}
