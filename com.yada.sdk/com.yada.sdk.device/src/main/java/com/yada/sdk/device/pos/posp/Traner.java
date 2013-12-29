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
//		Date currentDate = new Date(System.currentTimeMillis());
//		reqMessage.setFieldString(12, String.format("%tH%tM%tS", currentDate,
//				currentDate, currentDate));
//		reqMessage.setFieldString(13,
//				String.format("%tm%td", currentDate, currentDate));
		reqMessage.setFieldString(24, "009");
		reqMessage.setFieldString(41, getTerminalId());
		reqMessage.setFieldString(42, getMerchantId());
		reqMessage.setFieldString(61, getBatchNo() + "001");
		IMessage respMessage = sendTran(reqMessage);
		ByteBuffer field48 = respMessage.getField(48);
		byte[] raw = new byte[field48.remaining()];
		field48.get(raw);
		Tlv tlv = new Tlv(raw);

		SigninInfo si = new SigninInfo();
		String batchNo = respMessage.getFieldString(61).substring(0, 6);
		si.batchNo = batchNo;
		
		for (Tlv childTlv : tlv.getChildren()) {
			byte[] tg = childTlv.getTag();
			byte[] value = childTlv.getValue();
			String key = getStringKey(value);

			if (tg[0] == 0x98) {
				si.tmkTpk = key;
			}

			if (tg[0] == 0x99) {
				si.tmkTak = key;
			}
		}

		return si;
	}

	private String getStringKey(byte[] value) {
		String key;
		if (value.length == 23) {
			byte[] rawKey = new byte[16];
			System.arraycopy(value, 1, rawKey, 0, 16);
			key = new String(rawKey);
		} else {
			byte[] rawKey = new byte[32];
			System.arraycopy(value, 1, rawKey, 0, 32);
			key = new String(rawKey);
		}
		return key;
	}

	void paramDownload() {

	}
	
	public String stagesPay(String cardNo, String validity, String amt, String stagesId, String stagesCount) throws PackagingException, IOException
	{
		IMessage reqMessage = createMessage();
		reqMessage.setFieldString(2, cardNo);
		reqMessage.setFieldString(3, "000000");
		reqMessage.setFieldString(4, String.format("%12s", amt).replace(' ', '0'));
		reqMessage.setFieldString(11, getTraceNo());
		reqMessage.setFieldString(14, validity);
		reqMessage.setFieldString(22, "011");
		reqMessage.setFieldString(24, "009");
		reqMessage.setFieldString(25, "14");
		reqMessage.setFieldString(41, getTerminalId());
		reqMessage.setFieldString(42, getMerchantId());
		Tlv tlv48 = new Tlv();
		Tlv tlv48_90 = new Tlv();
		tlv48_90.setTag(new byte[]{(byte) 0x90});
		tlv48_90.setStringValue("");;
		// TODO 48域TLV确认
		return null;
	}
	
	public String pay()
	{
		return null;
	}
}
