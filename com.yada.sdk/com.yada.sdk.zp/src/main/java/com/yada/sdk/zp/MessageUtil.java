package com.yada.sdk.zp;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import com.yada.sdk.device.encryption.IEncryption;

public final class MessageUtil {
	/**
	 * 处理中国银行IST（ZP）8583包的第7域。
	 * 
	 * @param date
	 * @return
	 */
	public static String getField07(Calendar calendar) {
		TimeZone tz = calendar.getTimeZone();
		calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
		String ret = String.format("%1$tm%1$td%1$tH%1$tM%1$tS", calendar);
		calendar.setTimeZone(tz);
		return ret;
	}

	/**
	 * 处理中国银行IST（ZP） 8583包的37域
	 * 
	 * @param date
	 * @param traceNo
	 * @return
	 */
	public static String handleField37(Calendar calendar, String traceNo) {
		String format = new SimpleDateFormat("yyDDDHH").format(calendar.getTime());
		StringBuilder field37 = new StringBuilder();
		field37.append(format.substring(1, format.length()));
		field37.append(traceNo);
		return field37.toString();
	}

	/**
	 * 处理中国银行IST（ZP） 8583包的43域。格式二。
	 * 
	 * @return
	 */
	public static String getField43Type2(String name) {
		StringBuilder field43 = new StringBuilder();
		field43.append(name);
		int position = name.getBytes(Charset.forName("GBK")).length;
		for (int i = position; i < 40; i++) {
			field43.append(" ");
		}
		return field43.toString();
	}

	/**
	 * 处理中国银行IST（ZP） 8583包的90域。
	 * 
	 * @param accNo
	 *            账号
	 * @param pwd
	 *            密码（明文）
	 * @param encryptPackager
	 * @return
	 * @throws Exception
	 */
	public static String handleField52(String accNo, String pwd, String zpk_lmk, IEncryption encryption) throws Exception {

		// TODO 改
//		// 组装发送的BA报文
//		String sendMsgBA = packager.packBA(pwd, accNo);
//		// 获得响应的BB报文
//		String receMsgBB = client.sendMsg(sendMsgBA);
//		// 解析BB报文
//		String pin_lmk = packager.unpackBB(receMsgBB);
//		// 组装发送的JG报文
//		String sendMsgJG = packager.packJG(zpk_lmk, pwd, accNo, pin_lmk);
//		// 获得的响应JH报文
//		String receMsgJH = client.sendMsg(sendMsgJG);
//		// 解析JG报文
//		String pwd_zpk = packager.unpackJH(receMsgJH);
//		return pwd_zpk;
		return null;
	}

	public static String getField61(String mobileNo) {
		StringBuilder field61 = new StringBuilder();
		field61.append("10000000100000000000000000000000");
		field61.append(String.format("%03d", mobileNo.length()));
		field61.append(mobileNo);
		StringBuilder field61Temp = new StringBuilder().append("AM");
		field61Temp.append(String.format("%03d", field61.length()));
		field61Temp.append(field61);
		return field61.toString();
	}

	/**
	 * 处理中国银行IST（ZP） 8583包的90域。
	 * 
	 * @param mti
	 *            原交易的MTI
	 * @param traceNo
	 *            原交易的traceNo
	 * @param bocTxnTime
	 *            原交易的第七域交易时间
	 * @param acqInsCode
	 *            原交易的acqInsCode
	 * @param sndInsCode
	 *            原交易的sndInsCode
	 * @return
	 */
	public static String getField90(String mti, String traceNo, String bocTxnTime, String acqInsCode, String sndInsCode) {
		StringBuilder field90 = new StringBuilder(42);
		field90.append(mti);
		field90.append(traceNo);
		field90.append(bocTxnTime);
		for (int i = 0; i < 11 - acqInsCode.length(); i++) {// origAcquiringOrgId左边补零
			field90.append("0");
		}
		field90.append(acqInsCode);
		for (int i = 0; i < 11 - sndInsCode.length(); i++) {// origForwardingOrgId左边补零
			field90.append("0");
		}
		field90.append(sndInsCode);
		return field90.toString();
	}
}
