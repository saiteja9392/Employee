package com.employee.exception;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandling extends ResponseEntityExceptionHandler{

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<Object> handleAllExceptions(Exception e, WebRequest web){
		
		ExceptionMessageFormat exceptionMessage = new ExceptionMessageFormat(e.getMessage(), new Date(),web.getDescription(false));
		
		return new ResponseEntity<Object>(exceptionMessage, HttpStatus.BAD_REQUEST);
	}
}
