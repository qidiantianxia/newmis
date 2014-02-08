//package com.yada.sdk.device;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.nio.ByteBuffer;
//
//import junit.framework.TestCase;
//
//import com.yada.sdk.device.encryption.hsm.EncryptionMachine;
//import com.yada.sdk.net.FixLenPackageSplitterFactory;
//import com.yada.sdk.net.TcpClient;
//
//public class EncryptionMachineTest extends TestCase {
//
//	private EncryptionMachine encryptionMachine;
//	private String zmkTmk ;
//	private String serverIp ;
//	private int port;
//	private String lmkZmk;
//	
//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//		serverIp = "21.7.24.190";
//		port = 8;
//		lmkZmk = "8E54EEECCC1EB00E779FCF84AC794C7C";
//		encryptionMachine = new EncryptionMachine(serverIp, port, lmkZmk);
//		zmkTmk = "620D2892F9189CA30BDA03DADB6B1B88";
//		
//	}
//
//	private String send(String reqMessage) {
//		
//		ByteBuffer reqBuffer = ByteBuffer.wrap(reqMessage.getBytes());
//		TcpClient client = new TcpClient(new InetSocketAddress(serverIp, port),new FixLenPackageSplitterFactory(2, false), 20000);
//		try {
//			client.open();
//			ByteBuffer respBuffer = client.send(reqBuffer);
//			return new String(respBuffer.array());
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		} finally {
//			if (client.isOpen()) {
//				client.close();
//			}
//		}
//	}
//	
//	public void testGetLmkTmk(){
//		String lmkTmk = encryptionMachine.getLmkTmk(zmkTmk);
//		System.out.println(lmkTmk);
//	}
//	
//	/*public void testGetLmkTpk(){
//		
//		//encryptionMachine.getLmkTmk(zmkTmk)的返回值 （04-05下加密的）
//		String lmkTmk = "B6F0F9262F4C535DC45348A551EE419F";
//		
//		//使用lmkTmk生成ZPK。 包含了受ZMK包含的和受LMK保护的ZPK。
//		StringBuilder sb = new StringBuilder();
//		//1.消息头 2.命令代码 3.模式  4.密钥类型 5.密钥方案 6.lmkZmk 7.输出密钥的加密方案
//		sb.append("-------").append("IAX").append(lmkTmk).append(";XX0");
//		String respMessage = send(sb.toString());
//		int startIndex = "-------".length() + 2 + 2;
//		String zmkTpk = respMessage.substring(startIndex+1,startIndex+1+32);
//		System.out.println("zmkZpk="+zmkTpk);
//		String genLmkTpk = respMessage.substring(startIndex+1+32+1,startIndex+1+1+32+32);
//		System.out.println("genLmkTpk="+genLmkTpk);
//		
//		//由ZMK保护的TPK转成LmkTpk
//		String lmkTpk = encryptionMachine.getLmkTpk(lmkTmk, zmkTpk);
//		System.out.println("lmkTpk="+lmkTpk);
//		Assert.assertTrue(genLmkTpk.equals(lmkTpk));
//	}*/
//	/*
//	public void testGetTpkPin(){
//		//encryptionMachine.getLmkTpk(lmkTmk, zmkTpk)的返回值
//		String lmkTpk = "AD3BDA0CD45FC804CA8B57C3E034DF3D";
//
//		String accountNo = "6225880131239628";
//		String pin = "111111";
//		
//		String tpkPin = encryptionMachine.getTpkPin(accountNo, pin, lmkTpk);
//		System.out.println("tpkPin="+tpkPin);
//	}
//	*/
//	public void testGetLmkTak(){
//		//（04-05下加密的）
//		String lmkTmk = "B6F0F9262F4C535DC45348A551EE419F";
//		StringBuilder sb = new StringBuilder();
//		//0405下的ZMK
//		sb.append("-------").append("FI").append("1").append("X").append(lmkTmk).append(";XX0");
//		String respMsg = send(sb.toString());
//		System.out.println(respMsg);
//		String tmkTak = respMsg.substring(11+1,11+1+32);
//		System.out.println("tmkTak="+tmkTak);
//		String genLmkTak = respMsg.substring(11+1+32+1,11+1+32+1+32);
//		System.out.println("genLmkTak="+genLmkTak);
//		
//		//返回的是 1617下的TAK  使用0405的LMKZMK加密 ZMKTAK
//		String lmkTak = encryptionMachine.getLmkTak(lmkTmk, tmkTak);
//		System.out.println("lmkTak="+lmkTak);
//	}
//	
//	/*public void testGetMac(){
//		String cardNo = "6225880131239628";
//		String processCode = "000000";
//		String amt = "100";
//		String termNo = "11111111";
//		
//		String formatAmt = String.format("%12s", amt).replace(' ', '0');
//		String traceNo = "111111";
//		String currency = "156";
//		
//		//模拟macData
//		StringBuilder macData = new StringBuilder();
//		macData.append(cardNo.length() % 2 == 0 ? cardNo : "0" + cardNo);
//		macData.append(processCode);
//		macData.append(formatAmt);
//		macData.append(traceNo);
//		macData.append("0" + currency);
//		macData.append(termNo);
//		
//		//encryptionMachine.getLmkTak(lmkTmk, tmkTak); 的返回值。 
//		String lmkTak = "454A0525EC8188738B4458F2C3957C6F";
//		//1617下的TAK
//		String mac = encryptionMachine.getTakMac(macData.toString(), lmkTak);
//		System.out.println("mac="+mac);
//	}*/
//	
//}
