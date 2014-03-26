package com.yada.sdk.zp;

public interface IZpSystemConfigService {
	public void savePinKey(String pinKey);

	public String getPinKey();
	
	public String getLmkZmk();

	public String getAcqOrgId();
}
