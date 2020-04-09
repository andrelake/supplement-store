package com.andrelake.supplementstore.domain.exceptions;

public class EntityInUseException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EntityInUseException(String msg) {
		super(msg);
	}
}
