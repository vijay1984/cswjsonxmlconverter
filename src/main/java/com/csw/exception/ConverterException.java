package com.csw.exception;

public class ConverterException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConverterException(String errorMessage) {
		super(errorMessage);
	}
	
	public ConverterException(String errorMessage, Throwable cause) {
		super(errorMessage, cause);
	}
}
