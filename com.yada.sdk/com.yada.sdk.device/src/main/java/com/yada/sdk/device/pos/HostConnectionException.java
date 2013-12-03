package com.yada.sdk.device.pos;

public class HostConnectionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public HostConnectionException(Exception e)
	{
		super(e);
	}
	
	public HostConnectionException(String message, Exception e)
	{
		super(message, e);
	}
}
