package com.apiupload.restapi.advice;

import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.apiupload.restapi.exception.BaseException;

@ControllerAdvice
public class ApiUploadAdvice {
	
	private static final Logger LOG = Logger.getLogger(ApiUploadAdvice.class);
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<BaseException> errorHandler(Exception e) {
		
		LOG.error(e);
		
		BaseException ex = new BaseException();
		
		ex.setCode("500");
		ex.setMessage(e.getMessage());
		ResponseEntity<BaseException> entity = new ResponseEntity<BaseException>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
		
		return entity;
	}

}
