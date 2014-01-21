package com.yada.sdk.device.pos.posp;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.encryption.IEncryption;
import com.yada.sdk.device.encryption.TerminalAuth;
import com.yada.sdk.device.pos.IVirtualPos;
import com.yada.sdk.device.pos.SequenceGenerator;
import com.yada.sdk.packages.PackagingException;

public class VirtualPos implements IVirtualPos<Traner> {
	private static final String DEFAULT_TELLER_NO = "000";
	private static final String DEFAULT_BATCH_NO = "000000";
	private String merchantId;
	private String terminalId;
	private String tellerNo;
	private String serverIp;
	private int serverPort;
	private int timeout;
	private volatile boolean needSignin = true;
	private volatile boolean needParamDownload = true;
	private TerminalAuth terminalAuth;
	private String batchNo;
	private SequenceGenerator traceNoSeqGenerator;
	private SequenceGenerator cerNoSeqGenerator;
	private ByteBuffer head;

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
		batchNo = DEFAULT_BATCH_NO;
		this.traceNoSeqGenerator = new SequenceGenerator("termNo_" + terminalId);
		this.cerNoSeqGenerator = new SequenceGenerator("cerNo_" + terminalId);
	}

	@Override
	public Traner createTraner() throws IOException, ISOException, PackagingException {
		checkSingin();
		Traner traner = new Traner(merchantId, terminalId, tellerNo, batchNo,
				serverIp, serverPort, timeout, new CheckSignin(this),
				terminalAuth, traceNoSeqGenerator, cerNoSeqGenerator,head);
		return traner;
	}

	private synchronized void checkSingin() throws IOException, ISOException, PackagingException {
		if (needSignin) {
			Traner traner = new Traner(merchantId, terminalId, tellerNo,
					batchNo, serverIp, serverPort, timeout, new CheckSignin(
							this), terminalAuth, traceNoSeqGenerator,
					cerNoSeqGenerator,head);

			SigninInfo si = traner.singin();
			batchNo = si.batchNo;
			terminalAuth.setTak(si.tmkTak);
			terminalAuth.setTpk(si.tmkTpk);
			traner.close();
			needSignin = false;
		}

		if (needParamDownload) {
			Traner traner = new Traner(merchantId, terminalId, tellerNo,
					batchNo, serverIp, serverPort, timeout, new CheckSignin(
							this), terminalAuth, traceNoSeqGenerator,
					cerNoSeqGenerator,head);
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

	public void setHead(ByteBuffer head) {
		this.head = head;
	}
	
}
