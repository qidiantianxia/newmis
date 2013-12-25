package com.yada.sdk.device.pos.posp;

import com.yada.sdk.packages.transaction.IMessage;

class CheckSingin {
	private VirtualPos vp;
	public CheckSingin(VirtualPos vp)
	{
		this.vp = vp;
	}
	
	void checkMessage(IMessage responseMessage)
	{
		String processCode = responseMessage.getFieldString(3);
		char c = processCode.charAt(5);
		
		switch(c)
		{
		case '1':
			vp.resetParamDownload();
			break;
		case '4':
		case '5':
		case '6':
			vp.resetSingin();
			break;
		}
	}
}
