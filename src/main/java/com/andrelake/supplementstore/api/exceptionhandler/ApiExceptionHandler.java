package com.andrelake.supplementstore.api.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.andrelake.supplementstore.domain.exceptions.EntityInUseException;
import com.andrelake.supplementstore.domain.exceptions.EntityNotFoundException;
import com.andrelake.supplementstore.domain.exceptions.GenericException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler{

	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> treatEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
		
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(EntityInUseException.class)
	public ResponseEntity<?> treatEntityInUseException(EntityInUseException ex, WebRequest request) {
		
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@ExceptionHandler(GenericException.class)
	public ResponseEntity<?> treatGenericException(GenericException ex, WebRequest request) {
		
		return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
	
		if(body == null) {
			
			body = Problem.builder()
					.dateHour(LocalDateTime.now())
					.message(status.getReasonPhrase())
					.build();
		}
		else if (body instanceof String) {
			
			body = Problem.builder()
					.dateHour(LocalDateTime.now())
					.message((String) body)
					.build();
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
}
