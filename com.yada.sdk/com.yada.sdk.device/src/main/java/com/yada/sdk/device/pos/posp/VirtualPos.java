package com.yada.sdk.device.pos.posp;

import java.io.IOException;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.pos.IVirtualPos;

public class VirtualPos implements IVirtualPos<Traner> {
	private static final String DEFAULT_TELLER_NO = "000";
	private String merchantId;
	private String terminalId;
	private String serverIp;
	private int serverPort;
	private int timeout;
	private byte[] pinKey;
	private byte[] macKey;
	private byte[] masterKey;
	private volatile boolean needSingin = true;
	private volatile boolean needParamDownload = true;

	public VirtualPos(String merchantId, String terminalId, String serverIp,
			int serverPort, byte[] masterKey, int timeout) {
		this.merchantId = merchantId;
		this.terminalId = terminalId;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.timeout = timeout;
		this.masterKey = masterKey;
	}

	@Override
	public Traner createTraner() throws IOException, ISOException {
		checkSingin();
		Traner traner = new Traner(merchantId, terminalId, serverIp,
				serverPort, timeout, new CheckSingin(this));
		return traner;
	}

	private synchronized void checkSingin() {
		if (needSingin) {

		}

		if (needParamDownload) {

		}
	}

	public void resetSingin() {
		needSingin = true;
	}

	public void resetParamDownload() {
		needParamDownload = true;
	}
}
