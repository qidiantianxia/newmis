package com.yada.sdk.packages.transaction;

import java.nio.ByteBuffer;

import com.yada.sdk.packages.PackagingException;

/**
 * 标准8583包的接口
 * 
 * @author jiangfengming
 * 
 */
public interface IFinancialTransactionCardOriginatedMessages {
	/**
	 * 判断域是否存在
	 * 
	 * @param fieldId
	 * @return
	 */
	public boolean hasField(int fieldId);

	/**
	 * 获取域值
	 * 
	 * @param fieldId
	 * @return
	 */
	public ByteBuffer getField(int fieldId);

	/**
	 * 
	 * @param fieldId
	 * @return
	 */
	public String getFieldString(int fieldId);

	/**
	 * 设置域值
	 * 
	 * @param fieldId
	 * @param fieldValue
	 */
	public void setField(int fieldId, ByteBuffer fieldValue) throws PackagingException;

	/**
	 * 
	 * @param fieldId
	 * @param fieldString
	 */
	public void setFieldString(int fieldId, String fieldString) throws PackagingException;

	/**
	 * 获取域最大长度
	 * 
	 * @param fieldId
	 *            域ID
	 * @return 长度
	 */
	public int getFieldMaxLen(int fieldId);

	/**
	 * 获取TPDU源地址
	 * 
	 * @return
	 * @throws PackagingException
	 */
	public ByteBuffer getTpduFromAddress() throws PackagingException;

	/**
	 * 获取TPDU目标地址
	 * 
	 * @return
	 * @throws PackagingException
	 */
	public ByteBuffer getTpduToAddress() throws PackagingException;

	/**
	 * 设置TPDU源地址
	 * 
	 * @param tpduFromAddress
	 * @throws PackagingException
	 */
	public void setTpduFromAddress(ByteBuffer tpduFromAddress) throws PackagingException;

	/**
	 * 设置TPDU目标地址
	 * 
	 * @param tpduFromAddress
	 * @throws PackagingException
	 */
	public void setTpduToAddress(ByteBuffer tpduFromAddress) throws PackagingException;
}
