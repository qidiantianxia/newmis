package com.yada.sdk.device.pos.posp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.encryption.TerminalAuth;
import com.yada.sdk.device.pos.AbsTraner;
import com.yada.sdk.device.pos.SequenceGenerator;
import com.yada.sdk.device.pos.util.Utils;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;
import com.yada.sdk.packages.transaction.jpos.PospPacker;

public class Traner extends AbsTraner {
	private CheckSignin cs;

	public Traner(String merchantId, String terminalId, String tellerNo,
			String batchNo, String serverIp, int serverPort, int timeout,
			CheckSignin cs, TerminalAuth terminalAuth,
			SequenceGenerator traceNoSeqGenerator,
			SequenceGenerator cerNoSeqGenerator,ByteBuffer head) throws IOException,
			ISOException {
		super(merchantId, terminalId, tellerNo, batchNo,
				new FixLenPackageSplitterFactory(2, false), new PospPacker(7),
				serverIp, serverPort, timeout, terminalAuth,
				traceNoSeqGenerator, cerNoSeqGenerator,head);
		this.cs = cs;
	}

	/**
	 * 签到
	 * @return
	 * @throws PackagingException
	 * @throws IOException
	 */
	SigninInfo singin() throws PackagingException, IOException {
		IMessage reqMessage = createMessage();
		reqMessage.setFieldString(0, "0800");
		reqMessage.setFieldString(3, "990000");
		reqMessage.setFieldString(24, "009");
		reqMessage.setFieldString(41, getTerminalId());
		reqMessage.setFieldString(42, getMerchantId());
		reqMessage.setFieldString(61, getBatchNo() + "001");
		
		reqMessage.setFieldString(11, getTraceNo());
		
		
		IMessage respMessage = sendTran(reqMessage);
		
		String temp = new String(respMessage.getField(48).array(),Charset.forName("GBK"));
		
		//返回参数数据继续发送签到交易直到返回密钥为止
		while(temp.substring(0,2).equals("97")){
			respMessage = sendTran(reqMessage);
		}
		
		SigninInfo si = new SigninInfo();
		String batchNo = respMessage.getFieldString(61).substring(0, 6);
		si.batchNo = batchNo;
		
		String field48 = new String(respMessage.getField(48).array(),Charset.forName("GBK"));
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
	//解48域密钥
	private String getStringKey(String value) {
		String key;
		if (value.length() == 23) {
			key = value.substring(1, 17);
		} else {
			key = value.substring(1, 33);
		}
		return key;
	}
	//参数下载
	void paramDownload() {
		
	}
	/**
	 * 
	 * @param cardNo
	 * 			卡号
	 * @param validity
	 * 			效期
	 * @param amt
	 * 			金额
	 * @param pin
	 * 			PIN码
	 * @param stagesId
	 * 			分期交易ID
	 * @param stagesCount
	 * 			分期期数
	 * @return
	 */
	public IMessage stagesPay(String cardNo, String validity, String amt, String pin, String stagesId, int stagesCount)
	{
		String processCode = "000000";
		String formatAmt = String.format("%12s", amt).replace(' ', '0');
		String traceNo = getTraceNo();
		String currency = "156";
		IMessage respMessage = null;
		try {
			IMessage reqMessage = createMessage();
			reqMessage.setFieldString(0,"0200");
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
			
			String field48 = "9003905" + "9106" + stagesId + String.format("%02d", stagesCount);
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
			
			byte[] bcdMacData = Utils.ASCII_To_BCD(macData.toString().getBytes());
			byte[] terminalByte = getTerminalId().getBytes();
			
			ByteBuffer buf = ByteBuffer.allocate(bcdMacData.length+terminalByte.length);
			buf.put(bcdMacData).put(terminalByte);
			
			reqMessage.setField(64, getMac(buf));
			
			respMessage = sendTran(reqMessage);
			
			//检查是否需要签到或参数下载
			cs.checkMessage(respMessage);
			
		} catch (PackagingException e) {
			//TODO LOG
			e.printStackTrace();
		} catch (IOException e) {
			//TODO 存储转发
			e.printStackTrace();
		}
		return respMessage;
	}
	
	/**
	 * 冲正交易
	 * @param orgMessage
	 * @throws PackagingException
	 * @throws IOException
	 */
	public void reversal(IMessage orgMessage) throws PackagingException, IOException{
		IMessage reqMessage = createMessage();
		reqMessage.setFieldString(0, "0400");
		reqMessage.setFieldString(2, orgMessage.getFieldString(2));
		reqMessage.setFieldString(3, orgMessage.getFieldString(3));
		reqMessage.setFieldString(4, orgMessage.getFieldString(4));
		reqMessage.setFieldString(11, getTraceNo());
		if(orgMessage.getFieldString(14) != null){
			reqMessage.setFieldString(14, orgMessage.getFieldString(14));
		}
		reqMessage.setFieldString(22, orgMessage.getFieldString(22));
		if(orgMessage.getFieldString(23) != null){
			reqMessage.setFieldString(23, orgMessage.getFieldString(23));
		}
		reqMessage.setFieldString(24, "009");
		reqMessage.setFieldString(25, orgMessage.getFieldString(25));
		if(orgMessage.getFieldString(38) != null){
			reqMessage.setFieldString(38, orgMessage.getFieldString(38));
		}
		reqMessage.setFieldString(41, orgMessage.getFieldString(41));
		reqMessage.setFieldString(42, orgMessage.getFieldString(42));
		if(orgMessage.getFieldString(44) != null){
			reqMessage.setFieldString(44, orgMessage.getFieldString(44));
		}
		if(orgMessage.getFieldString(48) != null){ 
			reqMessage.setFieldString(48, orgMessage.getFieldString(48));
		}
		reqMessage.setFieldString(49, orgMessage.getFieldString(49));
		reqMessage.setFieldString(61, getBatchNo()+getTellerNo()+getCerNo());
		reqMessage.setFieldString(62, orgMessage.getFieldString(0)+orgMessage.getFieldString(11)+"0000000000");
		
		sendTran(reqMessage);
	}
}
