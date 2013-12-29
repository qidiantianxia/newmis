package com.yada.sdk.device.encryption.hsm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.yada.sdk.device.encryption.IEncryption;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;

public class EncryptionMachine implements IEncryption {

	private String lmkZmk;
	private TcpClient client;
	private InetSocketAddress endPoint;

	public EncryptionMachine(String serverIp, int port, String lmkZmk) {
		this.lmkZmk = lmkZmk;
		this.endPoint = new InetSocketAddress(serverIp, port);
	}

	private String send(String reqMessage) {
		ByteBuffer reqBuffer = ByteBuffer.wrap(reqMessage.getBytes());
		TcpClient client = new TcpClient(endPoint,
				new FixLenPackageSplitterFactory(2, false), 2000);
		try {
			client.open();
			ByteBuffer respBuffer = client.send(reqBuffer);
			return new String(respBuffer.array());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			if (client.isOpen()) {
				client.close();
			}
		}
	}

	@Override
	public String getLmkTmk(String zmkTmk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLmkTak(String lmkTmk, String tmkTak) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLmkTpk(String lmkTmk, String tmkTpk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTpkPin(String accountNo, String pin, String lmkTpk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTakMac(String macData, String lmkTak) {
		// TODO Auto-generated method stub
		return null;
	}

}
