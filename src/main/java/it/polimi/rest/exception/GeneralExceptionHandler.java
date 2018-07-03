package it.polimi.rest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//controller delle eccezioni
@ControllerAdvice
class GeneralExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler({ ResourceNotFoundException.class })
	@ResponseBody
	protected ResponseEntity<Object> handleNotFound(ResourceNotFoundException ex, WebRequest request) {

		return handleExceptionInternal(ex, ex.getCustomMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({ InvalidDateException.class })
	@ResponseBody
	protected ResponseEntity<Object> handleNotFound(InvalidDateException ex, WebRequest request) {

		return handleExceptionInternal(ex, ex.getCustomMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	

	@ExceptionHandler({ EmptyResultSetException.class })
	@ResponseBody
	protected ResponseEntity<Object> handleNotFound(EmptyResultSetException ex, WebRequest request) {

		return handleExceptionInternal(ex, ex.getCustomMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
}
