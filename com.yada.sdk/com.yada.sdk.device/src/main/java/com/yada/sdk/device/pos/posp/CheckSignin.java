package com.yada.sdk.device.pos.posp;

import com.yada.sdk.packages.transaction.IMessage;

class CheckSignin {
	private VirtualPos vp;

	public CheckSignin(VirtualPos vp) {
		this.vp = vp;
	}

	void checkMessage(IMessage responseMessage) {
		String processCode = responseMessage.getFieldString(3);
		char c = processCode.charAt(5);
		String respCode = responseMessage.getFieldString(39);
		boolean needParamDownload = false;
		boolean needSingin = false;

		switch (c) {
		case '1':
			needParamDownload = true;
			break;
		case '4':
		case '5':
		case '6':
			needSingin = true;
			break;
		}

		switch (respCode) {
		case "Z1":
		case "88":
		case "89":
			needSingin = true;
			break;
		}

		if(needSingin)
			vp.resetSingin();
		else if(needParamDownload)
			vp.resetParamDownload();
	}
}
