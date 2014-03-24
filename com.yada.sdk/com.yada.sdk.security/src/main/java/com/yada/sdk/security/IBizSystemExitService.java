package com.yada.sdk.security;

import java.util.List;

public interface IBizSystemExitService {
	public void beginClose();
	public List<Object> getBizData();
	public String getSystemName();
}
