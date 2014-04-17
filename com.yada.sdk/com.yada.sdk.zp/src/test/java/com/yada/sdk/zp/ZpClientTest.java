package com.yada.sdk.zp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class ZpClientTest {

	@Test
	public void testGetField07() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
		SimpleDateFormat outFormat = new SimpleDateFormat("MMddHHmmss");
		String txnDate = dateFormat.format(date);
		String txnTime = timeFormat.format(date);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR_OF_DAY, -8);
		date = calendar.getTime();
		String gmtTime = outFormat.format(date);

		String field07 = ZpClient.getField07(txnDate, txnTime);
		// System.out.println(field07);
		Assert.assertEquals(field07.length(), 10);
		Assert.assertEquals(field07, gmtTime);
	}

	@Test
	public void testGetField14() {
		String yearOfValidThru = "3";
		String monthOfValidThru = "1";
		String field14 = ZpClient.getField14(yearOfValidThru, monthOfValidThru);
		Assert.assertEquals(field14.length(), 4);

		yearOfValidThru = "11";
		monthOfValidThru = "21";
		field14 = ZpClient.getField14(yearOfValidThru, monthOfValidThru);
		Assert.assertEquals(field14.length(), 4);
	}

	@Test
	public void testGetField37() {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat timeFormat = new SimpleDateFormat("HHmmss");
		String txnDate = dateFormat.format(date);
		String txnTime = timeFormat.format(date);
		String traceNo = "123456";

		String field37 = ZpClient.getField37(txnDate, txnTime, traceNo);
		Assert.assertEquals(field37.length(), 12);
	}

	@Test
	public void testGetField48Usage4Tag9203() {
		String cvn2 = "123";
		String field48 = ZpClient.getField48Usage4Tag9203(cvn2);
		Assert.assertEquals("9203123", field48);
	}

	@Test
	public void testGetField61() {

		Boolean hasPassword;
		Boolean hasValidityPeriod;
		Boolean hasCVN2;
		String credentialsType;
		String credentialsNo;
		String mobileNo;
		String field61;
		String str61;
		int length;

		hasPassword = false;
		hasValidityPeriod = false;
		hasCVN2 = false;
		credentialsType = "";
		credentialsNo = "";
		mobileNo = "";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "0");
		Assert.assertEquals(str61.substring(1, 2), "0");
		Assert.assertEquals(str61.substring(5, 6), "0");
		Assert.assertEquals(str61.substring(2, 3), "0");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = false;
		hasCVN2 = false;
		credentialsType = "";
		credentialsNo = "";
		mobileNo = "";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "0");
		Assert.assertEquals(str61.substring(5, 6), "0");
		Assert.assertEquals(str61.substring(2, 3), "0");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = true;
		hasCVN2 = false;
		credentialsType = "";
		credentialsNo = "";
		mobileNo = "";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "1");
		Assert.assertEquals(str61.substring(5, 6), "0");
		Assert.assertEquals(str61.substring(2, 3), "0");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = true;
		hasCVN2 = true;
		credentialsType = "";
		credentialsNo = "";
		mobileNo = "";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "1");
		Assert.assertEquals(str61.substring(5, 6), "1");
		Assert.assertEquals(str61.substring(2, 3), "0");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = true;
		hasCVN2 = true;
		credentialsType = null;
		credentialsNo = "";
		mobileNo = "";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "1");
		Assert.assertEquals(str61.substring(5, 6), "1");
		Assert.assertEquals(str61.substring(2, 3), "0");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = true;
		hasCVN2 = true;
		credentialsType = "credentialsType";
		credentialsNo = "";
		mobileNo = "";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "1");
		Assert.assertEquals(str61.substring(5, 6), "1");
		Assert.assertEquals(str61.substring(2, 3), "0");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = true;
		hasCVN2 = true;
		credentialsType = "credentialsType";
		credentialsNo = null;
		mobileNo = "";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "1");
		Assert.assertEquals(str61.substring(5, 6), "1");
		Assert.assertEquals(str61.substring(2, 3), "0");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = true;
		hasCVN2 = true;
		credentialsType = "credentialsType";
		credentialsNo = "credentialsNo";
		mobileNo = "";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "1");
		Assert.assertEquals(str61.substring(5, 6), "1");
		Assert.assertEquals(str61.substring(2, 3), "1");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = true;
		hasCVN2 = true;
		credentialsType = "credentialsType";
		credentialsNo = "credentialsNo";
		mobileNo = null;
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "1");
		Assert.assertEquals(str61.substring(5, 6), "1");
		Assert.assertEquals(str61.substring(2, 3), "1");
		Assert.assertEquals(str61.substring(8, 9), "0");
		Assert.assertEquals(field61.length(), length + 5);

		hasPassword = true;
		hasValidityPeriod = true;
		hasCVN2 = true;
		credentialsType = "credentialsType";
		credentialsNo = "credentialsNo";
		mobileNo = "mobileNo";
		field61 = ZpClient.getField61(hasPassword, hasValidityPeriod, hasCVN2, credentialsType, credentialsNo, mobileNo);
		str61 = field61.substring(5);
		length = Integer.parseInt(field61.substring(2, 5));
		Assert.assertEquals(str61.substring(0, 1), "1");
		Assert.assertEquals(str61.substring(1, 2), "1");
		Assert.assertEquals(str61.substring(5, 6), "1");
		Assert.assertEquals(str61.substring(2, 3), "1");
		Assert.assertEquals(str61.substring(8, 9), "1");
		Assert.assertEquals(field61.length(), length + 5);
	}

	@Test
	public void testHandleCredentialsNo() {
		String credentialsNo = "1afda2342";
		String hcn = ZpClient.handleCredentialsNo(credentialsNo);
		Assert.assertEquals(hcn.length(), 20);
	}

	@Test
	public void testGetField90() {
		String mti = "0010";
		String traceNo = "123456";
		String bocTxnTime = "0323104103";
		String acqInsCode = "UN";
		String sndInsCode = "BOC";
		String field90 = ZpClient.getField90(mti, traceNo, bocTxnTime, acqInsCode, sndInsCode);
		Assert.assertEquals(field90.length(), 42);
	}

	@Test
	public void testGetRespMessage() {
		Assert.assertNotNull(ZpClient.getRespMessage("00"));
	}

}
