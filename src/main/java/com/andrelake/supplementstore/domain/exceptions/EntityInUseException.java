package com.andrelake.supplementstore.domain.exceptions;

public class EntityInUseException extends GenericException{

	private static final long serialVersionUID = 1L;

	public EntityInUseException(String msg) {
		super(msg);
	}
}
