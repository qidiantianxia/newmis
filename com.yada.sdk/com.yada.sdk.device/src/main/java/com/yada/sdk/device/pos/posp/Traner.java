package com.yada.sdk.device.pos.posp;

import java.io.IOException;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.encryption.IEncryption;
import com.yada.sdk.device.encryption.TerminalAuth;
import com.yada.sdk.device.pos.AbsTraner;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.packages.transaction.jpos.PospPacker;

public class Traner extends AbsTraner {
	private CheckSignin cs;
	public Traner(String merchantId, String terminalId, String serverIp,
			String tellerNo, int serverPort, int timeout, CheckSignin cs, TerminalAuth terminalAuth) throws IOException, ISOException {
		super(merchantId, terminalId, tellerNo,
				new FixLenPackageSplitterFactory(2, false), new PospPacker(7),
				serverIp, serverPort, timeout, terminalAuth);
		this.cs = cs;
	}
	
	SigninInfo singin()
	{
		return null;
	}

	void paramDownload()
	{
		
	}
}
