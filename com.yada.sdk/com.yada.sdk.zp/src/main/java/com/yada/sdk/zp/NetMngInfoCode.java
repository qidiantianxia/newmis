package com.yada.sdk.zp;

public enum NetMngInfoCode {
	SignOn("001"),
	SignIn("002"),
	KeyExchangeReq("162"),
	EchoTest("301");
	
	private String value;
	
	public String getValue()
	{
		return value;
	}
	
	private NetMngInfoCode(String value) {
		this.value = value;
	}
	
}
