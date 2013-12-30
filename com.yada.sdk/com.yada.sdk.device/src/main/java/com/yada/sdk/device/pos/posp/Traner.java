package com.yada.sdk.device.pos.posp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.encryption.TerminalAuth;
import com.yada.sdk.device.pos.AbsTraner;
import com.yada.sdk.device.pos.SequenceGenerator;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.comm.Tlv;
import com.yada.sdk.packages.transaction.IMessage;
import com.yada.sdk.packages.transaction.jpos.PospPacker;

public class Traner extends AbsTraner {
	private CheckSignin cs;

	public Traner(String merchantId, String terminalId, String tellerNo,
			String batchNo, String serverIp, int serverPort, int timeout,
			CheckSignin cs, TerminalAuth terminalAuth,
			SequenceGenerator traceNoSeqGenerator,
			SequenceGenerator cerNoSeqGenerator) throws IOException,
			ISOException {
		super(merchantId, terminalId, tellerNo, batchNo,
				new FixLenPackageSplitterFactory(2, false), new PospPacker(7),
				serverIp, serverPort, timeout, terminalAuth,
				traceNoSeqGenerator, cerNoSeqGenerator);
		this.cs = cs;
	}

	SigninInfo singin() throws PackagingException, IOException {
		IMessage reqMessage = createMessage();
		reqMessage.setFieldString(0, "0800");
		reqMessage.setFieldString(3, "990000");
		reqMessage.setFieldString(11, getTraceNo());
		reqMessage.setFieldString(24, "009");
		reqMessage.setFieldString(41, getTerminalId());
		reqMessage.setFieldString(42, getMerchantId());
		reqMessage.setFieldString(61, getBatchNo() + "001");
		IMessage respMessage = sendTran(reqMessage);
		
		SigninInfo si = new SigninInfo();
		String batchNo = respMessage.getFieldString(61).substring(0, 6);
		si.batchNo = batchNo;
		
		String field48 = respMessage.getFieldString(48);
		String tag, len, value;
		int index = 0;
		
		while(index < field48.length())
		{
			tag = field48.substring(index, index + 2);
			len = field48.substring(index + 2, index + 2 + 2);
			int ilen = Integer.parseInt(len);
			value = field48.substring(index + 2 + 2, index + 2 + 2 + ilen);
			index = index + 2 + 2 + ilen;
			if(tag.equals("98"))
			{
				si.tmkTpk = getStringKey(value);
			}
			
			if(tag.equals("99"))
			{
				si.tmkTak = getStringKey(value);
			}
		}
		
		return si;
	}

	private String getStringKey(String value) {
		String key;
		if (value.length() == 23) {
			key = value.substring(0, 16);
		} else {
			key = value.substring(0, 32);
		}
		return key;
	}

	void paramDownload() {

	}
	
	public String stagesPay(String cardNo, String validity, String amt, String pin, String stagesId, int stagesCount) throws PackagingException, IOException
	{
		String processCode = "000000";
		String formatAmt = String.format("%12s", amt).replace(' ', '0');
		String traceNo = getTraceNo();
		String currency = "156";
		
		IMessage reqMessage = createMessage();
		reqMessage.setFieldString(2, cardNo);
		reqMessage.setFieldString(3, processCode);
		reqMessage.setFieldString(4, formatAmt);
		reqMessage.setFieldString(11, traceNo);
		reqMessage.setFieldString(14, validity);
		reqMessage.setFieldString(22, "011");
		reqMessage.setFieldString(24, "009");
		reqMessage.setFieldString(25, "14");
		reqMessage.setFieldString(41, getTerminalId());
		reqMessage.setFieldString(42, getMerchantId());
		
		String field48 = "9003905" + "9006" + stagesId + String.format("%02d", stagesCount);
		reqMessage.setFieldString(48, field48);
		reqMessage.setFieldString(49, currency);
		reqMessage.setFieldString(52, getPin(cardNo, pin));
		reqMessage.setFieldString(61, getBatchNo() + getTellerNo() + getCerNo());
		
		StringBuilder macData = new StringBuilder();
		macData.append(cardNo.length() % 2 == 0 ? cardNo : "0" + cardNo);
		macData.append(processCode);
		macData.append(formatAmt);
		macData.append(traceNo);
		macData.append("0" + currency);
		macData.append(getTerminalId());
		String mac = getMac(macData.toString());
		reqMessage.setFieldString(64, mac);
		
		IMessage respMessage = sendTran(reqMessage);
		return null;
	}
	
	public String pay()
	{
		return null;
	}
	
	private void reversal(IMessage orgMessage) throws PackagingException{
		IMessage reqMessage = createMessage();
		reqMessage.setFieldString(0, "0400");
		reqMessage.setFieldString(2, orgMessage.getFieldString(2));
		reqMessage.setFieldString(3, orgMessage.getFieldString(3));
		reqMessage.setFieldString(4, orgMessage.getFieldString(4));
		reqMessage.setFieldString(11, getTraceNo());
		reqMessage.setFieldString(14, orgMessage.getFieldString(14));
		reqMessage.setFieldString(22, orgMessage.getFieldString(22));
		if(orgMessage.getFieldString(23) != null){
			reqMessage.setFieldString(23, orgMessage.getFieldString(23));
		}
		reqMessage.setFieldString(24, "009");
		reqMessage.setFieldString(25, orgMessage.getFieldString(25));
		reqMessage.setFieldString(41, orgMessage.getFieldString(41));
		reqMessage.setFieldString(42, orgMessage.getFieldString(42));
		
		reqMessage.setFieldString(44, orgMessage.getFieldString(44));
		reqMessage.setFieldString(48, orgMessage.getFieldString(48));
		reqMessage.setFieldString(49, orgMessage.getFieldString(49));
		reqMessage.setFieldString(54, orgMessage.getFieldString(54));
		reqMessage.setFieldString(55, orgMessage.getFieldString(55));
		
		reqMessage.setFieldString(61, getBatchNo()+getTellerNo()+getCerNo());
		
		reqMessage.setFieldString(62, orgMessage.getFieldString(0)+orgMessage.getFieldString(11));
		
	}
}
