package com.yada.sdk.packages;

public class PackagingException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5907453971237579415L;

	public PackagingException() {
		super();
	}

	public PackagingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PackagingException(String message, Throwable cause) {
		super(message, cause);
	}

	public PackagingException(String message) {
		super(message);
	}

	public PackagingException(Throwable cause) {
		super(cause);
	}

}
