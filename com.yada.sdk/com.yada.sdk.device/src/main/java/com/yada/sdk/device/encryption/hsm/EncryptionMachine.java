package com.yada.sdk.device.encryption.hsm;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.yada.sdk.device.encryption.IEncryption;
import com.yada.sdk.device.pos.util.Utils;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.net.TcpClient;

public class EncryptionMachine implements IEncryption {

	private String lmkZmk;
	private InetSocketAddress endPoint;
	private String messageHead = "-------";

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
	
	//用于直接发送字节
	private String send(ByteBuffer reqBuffer) {
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
		sb.append(messageHead).append("A6").append("000").append(lmkZmk);
		//是否需要增加1A
		if(zmkTmk.length() != 16){
			sb.append("X");
		}
		//5.zmkTmk  6.tmk加密方案 
		sb.append(zmkTmk).append("X");
		String respMessage = send(sb.toString());
		//1.消息头长度  2.响应码长度  3.错误代码长度
		int startIndex = messageHead.length() + 2 + 2;
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
		//1.消息头 2.命令代码 
		sb.append(messageHead).append("MI");
		//是否需要增加1A
		if(lmkTmk.length() != 16){
			sb.append("X");
		}
		sb.append(lmkTmk);
		//是否需要增加1A
		if(tmkTak.length() != 16){
			sb.append("X");
		}
		sb.append(tmkTak).append(";XX0");
		String respMessage = send(sb.toString());
		System.out.println("getLmkTak="+respMessage);
		//1.消息头长度  2.响应码长度  3.错误代码长度
		int startIndex = messageHead.length() + 2 + 2;
		//返回密钥是否存在1A
		if(tmkTak.length() != 16){
			startIndex = startIndex+1;
		}
		String lmkTak = respMessage.substring(startIndex, startIndex + tmkTak.length());
		return lmkTak;
	}

	@Override
	public String getLmkTpk(String lmkTmk, String tmkTpk) {
		
		StringBuilder sb = new StringBuilder();
		//1.消息头 2.命令代码 
		sb.append(messageHead).append("FA");
		//是否需要增加1A
		if(lmkTmk.length() != 16){
			sb.append("X");
		}
		sb.append(lmkTmk);
		//是否需要增加1A
		if(tmkTpk.length() != 16){
			sb.append("X");
		}
		sb.append(tmkTpk).append(";XX0");
		String respMessage = send(sb.toString());

		//1.消息头长度  2.响应码长度  3.错误代码长度
		int startIndex = messageHead.length() + 2 + 2;
		//返回密钥是否存在1A
		if(tmkTpk.length() != 16){
			startIndex = startIndex+1;
		}
		String lmkTpk = respMessage.substring(startIndex, startIndex + tmkTpk.length());
		return lmkTpk;
	}

	@Override
	public String getTpkPin(String accountNo, String pin, String lmkTpk) {
		String subAccountNo = accountNo.substring(accountNo.length() - 13, accountNo.length() - 1);
		StringBuilder sb = new StringBuilder();
		sb.append(messageHead);
		sb.append("BA");
		sb.append(String.format("%-7s", pin).replace(' ', 'F'));
		sb.append(subAccountNo);
		sb.append("");
		sb.append("");
		
		String respMessage = send(sb.toString());
		int startIndex = messageHead.length() + 2 + 2;
		String lmkPin = respMessage.substring(startIndex);
		
		sb = new StringBuilder();
		sb.append(messageHead);
		sb.append("JG");
		if(lmkTpk.length() != 16){
			sb.append("X");
		}
		sb.append(lmkTpk);
		sb.append("01");
		sb.append(subAccountNo);
		sb.append(lmkPin);
		sb.append("");
		sb.append("");
		
		respMessage = send(sb.toString());
		String tmkPin = respMessage.substring(startIndex,startIndex+8);
		return tmkPin;
	}

	@Override
	public ByteBuffer getTakMac(ByteBuffer macData, String lmkTak) {

		StringBuilder sb = new StringBuilder();
		sb.append(messageHead).append("MS").append("0").append("0").append("1").append("0");
		if(lmkTak.length() != 16){
			sb.append("X").append(lmkTak);
		}
		String len = String.format("%04X", macData.array().length);
		sb.append(len);
		
		byte[] reqMsg = sb.toString().getBytes();
		
		ByteBuffer buf = ByteBuffer.allocate(macData.array().length+reqMsg.length);
		macData.flip();
		buf.put(reqMsg).put(macData);
		
		buf.flip();
		
		String respMessage = send(buf);
		
		int startIndex = messageHead.length() + 2 + 2;
		
		String mac = respMessage.substring(startIndex, startIndex + 16);
		
		byte[] macByte = Utils.decodeHex(mac.toCharArray());
		
		return ByteBuffer.wrap(macByte) ;
	}

}
