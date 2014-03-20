package com.yada.sdk.zp;

/**
 * 获取系统跟踪号接口
 * @author ct
 *
 */
public interface ITraceNoService {
	/**
	 * 获取系统跟踪号
	 * @param terminalId 终端号
	 * @return 系统跟踪号
	 */
	public String getTraceNo(String terminalId);
}
