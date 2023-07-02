package com.itechgenie.apps.jdk11.sb3.configs;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.itechgenie.apps.jdk11.sb3.dtos.exceptions.ItgGeneralException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandlerConfig {

	private ResponseEntity<ItgGeneralException> buildResponseEntity(ItgGeneralException exp) {
		String status = exp.getStatus();
		HttpStatusCode statusCode = HttpStatusCode.valueOf(500);
		if(status != null)
			HttpStatusCode.valueOf(Integer.valueOf(status));
		return ResponseEntity.status(statusCode).contentType(MediaType.APPLICATION_JSON).body(exp);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ItgGeneralException> generalException(Exception ex, Model model) {
		log.debug("handling exception::" + ex);
		ItgGeneralException exp = new ItgGeneralException();
		exp.setMethod("Some method here !");
		model.addAttribute(exp);
		return this.buildResponseEntity(exp);
	}

}
