package com.yada.sdk.device.pos;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Arrays;

import com.yada.sdk.device.encryption.TerminalAuth;
import com.yada.sdk.net.IPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;
import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;
import com.yada.sdk.packages.transaction.IPacker;

public abstract class AbsTraner {
	private IPacker packer;
	private SequenceGenerator traceNoSeqGenerator;
	private SequenceGenerator cerNoSeqGenerator;
	private TcpClient client;
	private String merchantId;
	private String terminalId;
	private String batchNo;
	private TerminalAuth terminalAuth;
	private String tellerNo;
	private ByteBuffer head;
	

	public AbsTraner(String merchantId, String terminalId, String tellerNo,
			String batchNo, IPackageSplitterFactory pkgSplitterFactory,
			IPacker packer, String serverIp, int serverPort, int timeout,
			TerminalAuth terminalAuth, SequenceGenerator traceNoSeqGenerator,
			SequenceGenerator cerNoSeqGenerator,ByteBuffer head) throws IOException {
		this.merchantId = merchantId;
		this.terminalId = terminalId;
		this.batchNo = batchNo;
		this.tellerNo = tellerNo;
		this.packer = packer;
		this.traceNoSeqGenerator = traceNoSeqGenerator;
		this.cerNoSeqGenerator = cerNoSeqGenerator;
		this.head = head;
		InetSocketAddress serverEndPoint = new InetSocketAddress(serverIp,
				serverPort);
		client = new TcpClient(serverEndPoint, pkgSplitterFactory, timeout);
		client.open();
		
		this.terminalAuth = terminalAuth;
	}

	protected String getTraceNo() {
		return String.format("%06d", traceNoSeqGenerator.getSequence());
	}

	protected String getCerNo() {
		return String.format("%06d", cerNoSeqGenerator.getSequence());
	}

	protected String getMerchantId() {
		return merchantId;
	}

	protected String getTerminalId() {
		return terminalId;
	}

	protected String getTellerNo() {
		return tellerNo;
	}

	protected String getBatchNo() {
		return batchNo;
	}

	protected String getPin(String accountNo, String pin) {
		return terminalAuth.getPin(accountNo, pin);
	}

	protected String getMac(String macData) {
		return terminalAuth.getMac(macData);
	}

	protected IMessage sendTran(IMessage requestMessage)
			throws PackagingException, IOException {
		ByteBuffer requestBuffer = packer.pack(requestMessage);
		ByteBuffer responseBuffer = client.send(requestBuffer);
		return packer.unpack(responseBuffer);
	}

	protected IMessage createMessage() throws PackagingException {
		IMessage message = packer.createEmpty();
		
		//TODO 是否抽取？
		byte[] headBytes = head.array();
		byte[] tpduId = Arrays.copyOfRange(headBytes, 0, 1);
		byte[] tpduToAddress = Arrays.copyOfRange(headBytes, 1, 3);
		byte[] tpduFromAddress = Arrays.copyOfRange(headBytes, 3, 5);
		byte[] version = Arrays.copyOfRange(headBytes, 5, 7);
		
		message.setTpduId(ByteBuffer.wrap(tpduId));
		message.setTpduToAddress(ByteBuffer.wrap(tpduToAddress));
		message.setTpduFromAddress(ByteBuffer.wrap(tpduFromAddress));
		message.setVersion(ByteBuffer.wrap(version));
		
		
		return message;
	}

	public void close() {
		client.close();
	}
}
