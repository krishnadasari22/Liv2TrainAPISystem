package com.televisory.capitalmarket.util;

public class BadRequestException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadRequestException(String s) { 
		// Call constructor of parent Exception 
		super(s); 
	}
}
