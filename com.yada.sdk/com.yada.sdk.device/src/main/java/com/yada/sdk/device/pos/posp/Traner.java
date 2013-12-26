package com.yada.sdk.device.pos.posp;

import java.io.IOException;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.pos.AbsTraner;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.packages.transaction.jpos.PospPacker;

public class Traner extends AbsTraner {
	private CheckSingin cs;
	public Traner(String merchantId, String terminalId, String serverIp,
			int serverPort, int timeout, CheckSingin cs) throws IOException, ISOException {
		super(merchantId, terminalId,
				new FixLenPackageSplitterFactory(2, false), new PospPacker(7),
				serverIp, serverPort, timeout);
		this.cs = cs;
	}

}
