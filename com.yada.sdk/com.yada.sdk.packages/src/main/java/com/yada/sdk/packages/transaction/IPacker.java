package com.yada.sdk.packages.transaction;

import java.nio.ByteBuffer;

import com.yada.sdk.packages.PackagingException;

/**
 * 标准的包装者接口
 * 
 * @author jiangfengming
 * 
 */
public interface IPacker {

	/**
	 * 打包
	 * 
	 * @param pkgs
	 * @return
	 */
	public ByteBuffer pack(IFinancialTransactionCardOriginatedMessages message) throws PackagingException;

	/**
	 * 解包
	 * 
	 * @param byteBuffer
	 * @return
	 */
	public IFinancialTransactionCardOriginatedMessages unpack(ByteBuffer byteBuffer) throws PackagingException;

}
