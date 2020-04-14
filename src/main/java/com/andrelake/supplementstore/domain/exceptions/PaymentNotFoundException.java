package com.andrelake.supplementstore.domain.exceptions;

public class PaymentNotFoundException extends EntityNotFoundException{

	private static final long serialVersionUID = 1L;

	public PaymentNotFoundException(String msg) {
		super(msg);
	}

	public PaymentNotFoundException(Long id) {
		super(String.format("Payment with id %d cannot be found", id));
	}
}
