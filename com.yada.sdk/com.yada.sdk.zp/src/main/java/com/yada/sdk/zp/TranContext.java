package com.yada.sdk.zp;

import java.util.Calendar;

import com.yada.sdk.packages.transaction.IMessage;

class TranContext {
	public IMessage reqMessage;
	public long createDateTime = Calendar.getInstance().getTimeInMillis(); // 创建日期时间
}
