package com.hcl.ownermicroservice.exceptions;

public class UserAlreadyExistOrNotException extends Exception{
	

	public UserAlreadyExistOrNotException(String msg)
	{
		super(msg);
	}

}
