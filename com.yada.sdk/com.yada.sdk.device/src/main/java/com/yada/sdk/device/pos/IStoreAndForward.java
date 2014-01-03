package com.yada.sdk.device.pos;

public interface IStoreAndForward<T> {
	/**
	 * 转发
	 * 
	 * @param t
	 */
	public void forward(T t);

	/**
	 * 存储
	 */
	public void store();
}
