package com.yada.sdk.device.pos;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

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

	public AbsTraner(String merchantId, String terminalId,
			IPackageSplitterFactory pkgSplitterFactory, IPacker packer,
			String serverIp, int serverPort, int timeout) throws IOException {
		this.merchantId = merchantId;
		this.terminalId = terminalId;
		traceNoSeqGenerator = new SequenceGenerator(terminalId + "_traceNo");
		cerNoSeqGenerator = new SequenceGenerator(terminalId + "_cerNo");
		InetSocketAddress serverEndPoint = new InetSocketAddress(serverIp,
				serverPort);
		client = new TcpClient(serverEndPoint, pkgSplitterFactory, timeout);
		client.open();
	}

	protected int getTraceNo() {
		return traceNoSeqGenerator.getSequence();
	}

	protected int getCerNo() {
		return cerNoSeqGenerator.getSequence();
	}

	protected String getMerchantId() {
		return merchantId;
	}

	protected String getTerminalId() {
		return terminalId;
	}

	protected IMessage sendTran(IMessage requestMessage)
			throws PackagingException, IOException {
		ByteBuffer requestBuffer = packer.pack(requestMessage);
		ByteBuffer responseBuffer = client.send(requestBuffer);
		return packer.unpack(responseBuffer);
	}

	protected IMessage createMessage() {
		return packer.createEmpty();
	}

	public void close() {
		client.close();
	}
}
