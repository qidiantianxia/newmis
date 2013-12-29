package com.yada.sdk.device.pos.posp;

import java.io.IOException;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.encryption.IEncryption;
import com.yada.sdk.device.encryption.TerminalAuth;
import com.yada.sdk.device.pos.IVirtualPos;

public class VirtualPos implements IVirtualPos<Traner> {
	private static final String DEFAULT_TELLER_NO = "000";
	private String merchantId;
	private String terminalId;
	private String tellerNo;
	private String serverIp;
	private int serverPort;
	private int timeout;
	private volatile boolean needSignin = true;
	private volatile boolean needParamDownload = true;
	private TerminalAuth terminalAuth;

	public VirtualPos(String merchantId, String terminalId, String serverIp,
			int serverPort, String zmkTmk, int timeout,
			IEncryption encryptionMachine) {
		this(merchantId, terminalId, DEFAULT_TELLER_NO, serverIp, serverPort,
				zmkTmk, timeout, encryptionMachine);
	}

	public VirtualPos(String merchantId, String terminalId, String tellerNo,
			String serverIp, int serverPort, String zmkTmk, int timeout,
			IEncryption encryptionMachine) {
		this.merchantId = merchantId;
		this.terminalId = terminalId;
		this.tellerNo = tellerNo;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.timeout = timeout;
		this.terminalAuth = new TerminalAuth(encryptionMachine);
		terminalAuth.setTmk(zmkTmk);
	}

	@Override
	public Traner createTraner() throws IOException, ISOException {
		checkSingin();
		Traner traner = new Traner(merchantId, terminalId, tellerNo,
				serverIp, serverPort, timeout, new CheckSignin(this),
				terminalAuth);
		return traner;
	}

	private synchronized void checkSingin() throws IOException, ISOException {
		if (needSignin) {
			Traner traner = new Traner(merchantId, terminalId,
					tellerNo, serverIp, serverPort, timeout,
					new CheckSignin(this), terminalAuth);

			SigninInfo si = traner.singin();
			terminalAuth.setTak(si.tmkTak);
			terminalAuth.setTpk(si.tmkTpk);
			traner.close();
			needSignin = false;
		}

		if (needParamDownload) {
			Traner traner = new Traner(merchantId, terminalId,
					tellerNo, serverIp, serverPort, timeout,
					new CheckSignin(this), terminalAuth);
			traner.paramDownload();
			traner.close();
			needParamDownload = false;
		}
	}

	public void resetSingin() {
		needSignin = true;
	}

	public void resetParamDownload() {
		needParamDownload = true;
	}
}
