package com.hcl.ownermicroservice.exceptions;

public class RideAlreadyExistOrNotException extends Exception{
	
	public RideAlreadyExistOrNotException(String msg)
	{
		super(msg);
	}
}
