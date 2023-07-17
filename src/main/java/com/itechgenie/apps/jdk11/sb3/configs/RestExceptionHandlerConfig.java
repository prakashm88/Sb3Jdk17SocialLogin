package com.itechgenie.apps.jdk11.sb3.configs;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

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

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	private ResponseEntity<ItgGeneralException> buildResponseEntity(ItgGeneralException exp) {
		log.error("handling exception::" + exp.getMessage(), exp);
		String status = exp.getStatus();
		HttpStatusCode statusCode = HttpStatusCode.valueOf(500);
		if (status != null)
			HttpStatusCode.valueOf(Integer.valueOf(status));
		return ResponseEntity.status(statusCode).contentType(MediaType.APPLICATION_JSON).body(exp);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ItgGeneralException> generalException(Exception ex, Model model) {
		log.error("handling exception::" + ex.getMessage(), ex);
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		ItgGeneralException exp = new ItgGeneralException();
		exp.setMethod("Some method here !");
		exp.setTimestamp(sdf.format(timestamp));
		exp.setMessage(ex.getMessage());
		exp.setStatus("fail");
		if(ex.getCause() != null)
			exp.setDebugMessage(ex.getCause().getMessage());
		model.addAttribute(exp);
		return this.buildResponseEntity(exp);
	}

}
