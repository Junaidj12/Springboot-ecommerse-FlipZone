package com.Ecommerse.Shopping.exception;

public class NotLoggedInException extends RuntimeException{
	
	String message = "Invalid Session, Login Again";

	
	public String getMessage() {
		return message;
	}
	
}
