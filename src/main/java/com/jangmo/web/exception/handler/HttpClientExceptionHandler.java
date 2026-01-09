package com.jangmo.web.exception.handler;

import com.jangmo.web.api.annotation.HttpClientExceptionController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice(annotations = HttpClientExceptionController.class)
public class HttpClientExceptionHandler {

	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<Void> handleException(HttpClientErrorException ex){
		return ResponseEntity.notFound().build();
	}
}
