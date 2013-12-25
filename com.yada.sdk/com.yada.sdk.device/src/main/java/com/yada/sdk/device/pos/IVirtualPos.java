package com.yada.sdk.device.pos;

public interface IVirtualPos<T extends AbsTraner> {
	public T createTraner() throws Exception;
}
