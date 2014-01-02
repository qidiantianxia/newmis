package com.yada.sdk.device.encryption.hsm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.yada.sdk.device.encryption.IEncryption;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;

public class EncryptionMachine implements IEncryption {

	private String lmkZmk;
	private InetSocketAddress endPoint;
	private String messageGead = "-------";

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
		StringBuilder sb = new StringBuilder();
	
		//1.消息头 2.命令代码 3.密钥类型  4.lmkZmk
		sb.append(messageGead).append("A6").append("000").append(lmkZmk);
		//是否需要增加1A
		if(zmkTmk.length() != 16){
			sb.append("-");
		}
		//5.zmkTmk  6.tmk加密方案   7.Atalla变量    8.终止信息分隔符    9.消息尾
		sb.append(zmkTmk).append("X").append("").append("").append("");
		
		String respMessage = send(sb.toString());
		//1.消息头长度  2.响应码长度  3.错误代码长度
		int startIndex = messageGead.length() + 2 + 2;
		//返回密钥是否存在1A
		if(zmkTmk.length() != 16){
			startIndex = startIndex+1;
		}
		String lmkTmk = respMessage.substring(startIndex, startIndex + zmkTmk.length());
		return lmkTmk;
	}

	@Override
	public String getLmkTak(String lmkTmk, String tmkTak) {
		StringBuilder sb = new StringBuilder();
		sb.append(messageGead);
		return null;
	}

	@Override
	public String getLmkTpk(String lmkTmk, String tmkTpk) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTpkPin(String accountNo, String pin, String lmkTpk) {
		String subAccountNo = accountNo.substring(accountNo.length() - 13, accountNo.length() - 1);
		StringBuilder sb = new StringBuilder();
		sb.append(messageGead);
		sb.append("BA");
		sb.append(String.format("%-7s", pin).replace(' ', 'F'));
		sb.append(subAccountNo);
		sb.append("");
		sb.append("");
		
		String respMessage = send(sb.toString());
		int startIndex = messageGead.length() + 2 + 2;
		String lmkPin = respMessage.substring(startIndex);
		
		sb = new StringBuilder();
		sb.append(messageGead);
		sb.append("JG");
		sb.append("X");
		sb.append(lmkTpk);
		sb.append("01");
		sb.append(subAccountNo);
		sb.append(lmkPin);
		sb.append("");
		sb.append("");
		
		respMessage = send(sb.toString());
		String tmkPin = respMessage.substring(startIndex);
		return tmkPin;
	}

	@Override
	public String getTakMac(String macData, String lmkTak) {
		int padZeroCount = macData.length() % 16;
		StringBuilder sb = new StringBuilder();
		sb.append(macData);
		for(int i = 0; i < padZeroCount; i++)
		{
			sb.append("0");
		}
				
		// TODO Auto-generated method stub
		return null;
	}

}
