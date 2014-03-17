//package com.yada.sdk.device;
//
//import java.io.IOException;
//import java.nio.ByteBuffer;
//
//import junit.framework.TestCase;
//
//import org.jpos.iso.ISOException;
//
//import com.yada.sdk.device.encryption.hsm.EncryptionMachine;
//import com.yada.sdk.device.pos.posp.Traner;
//import com.yada.sdk.device.pos.posp.VirtualPos;
//import com.yada.sdk.packages.PackagingException;
//
//public class VirtulaPosTest extends TestCase{
//
//	private VirtualPos pos;
//	
//	private EncryptionMachine encryptionMachine;
//	private String zmkTmk;
//	
//	private String merchantId = "104110041121014";
//	private String terminalId = "11034478";
////	private String merchantId = "104130070110001";
////	private String terminalId = "13012003";
////	private String serverIp = "22.7.16.91";
////	private int serverPort = 3001;
//	private String serverIp = "21.7.2.59";
//	private int serverPort = 1000;
//	private int timeout = 3000;
//	
//	private String tpduStr = "60001200000100";
//	
//	@Override
//	protected void setUp() throws Exception {
//		super.setUp();
//		encryptionMachine = new EncryptionMachine("21.7.24.190", 8, "8E54EEECCC1EB00E779FCF84AC794C7C");
//		zmkTmk = "620D2892F9189CA30BDA03DADB6B1B88";
//		pos = new VirtualPos(merchantId, terminalId, serverIp, serverPort, zmkTmk, timeout, encryptionMachine);
//		
//		byte[] b = new byte[7];
//		for(int i=0;i<tpduStr.length()/2 ; i++){
//			b[i] = Byte.parseByte(tpduStr.substring(i*2, i*2 +2),16);
//		}
//		
//		ByteBuffer tpdu = ByteBuffer.wrap(b);
//		
//		pos.setHead(tpdu);
//	}
//
//	public void testTraner(){
//		try {
//			Traner traner = pos.createTraner();
//			String cardNo = "5149587707356843";
//			String validity = "0417";
//			String amt = "1";
//			String pin = "111111";
//			String stagesId = "IP01";
//			int stagesCount = 3;
//			traner.stagesPay(cardNo, validity, amt, pin, stagesId, stagesCount);
//			
//		} catch (IOException | ISOException | PackagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//}
