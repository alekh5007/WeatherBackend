package com.exam.helper;

public class UserFoundException extends Exception{



	public UserFoundException() {
		super("user with this username are already in databases!!!");
	}
	
	public UserFoundException(String msg) {super(msg);}
	
}
