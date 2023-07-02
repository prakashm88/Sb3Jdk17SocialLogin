package com.itechgenie.apps.jdk11.sb3.dtos.exceptions;

import lombok.Data;

@Data
public class ItgGeneralException {

	private String method;
	private String status;
	private String timestamp;
	private String message;
	private String debugMessage; 
	
}
