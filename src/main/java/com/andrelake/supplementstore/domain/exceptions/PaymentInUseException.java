package com.andrelake.supplementstore.domain.exceptions;

public class PaymentInUseException extends EntityNotFoundException{

	private static final long serialVersionUID = 1L;

	public PaymentInUseException(String msg) {
		super(msg);
	}
	
	public PaymentInUseException(Long id) {
		super(String.format("Payment with id %d can't be deleted because has being used", id));
	}
}
