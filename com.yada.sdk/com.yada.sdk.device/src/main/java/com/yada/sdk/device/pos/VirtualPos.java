package com.yada.sdk.device.pos;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;
import com.yada.sdk.packages.transaction.IMessage;

public class VirtualPos {
	private String tellerNo;
	private volatile boolean isSignin;
	private TcpClient client;

	public VirtualPos(String merchantId, String terminalId, String serverIp,
			int serverPort) {
		client = new TcpClient(new InetSocketAddress(serverIp, serverPort),
				new FixLenPackageSplitterFactory(2, false), 5000);
	}

	public IMessage send(IMessage message) {
		return null;
	}

	public void signin(String tellerNo) {
		this.tellerNo = tellerNo;
	}

	public void signoff() {

	}

	public String getTellerNo() {
		return tellerNo;
	}

	public boolean isSignin() {
		return isSignin;
	}

	public void setSignin(boolean isSignin) {
		this.isSignin = isSignin;
	}
	
	public void send(ByteBuffer buffer) throws IOException
	{
		checkSocket();
	}
	
	private void checkSocket() throws IOException
	{
		if(!client.isOpen())
		{
			client.open();
		}
	}
}
