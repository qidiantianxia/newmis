package com.yada.sdk.packages.transaction.jpos;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import com.yada.sdk.packages.comm.Tlv;
import com.yada.sdk.packages.comm.TlvParser;
import com.yada.sdk.packages.comm.TlvParser.TLVException;

public class TlvTester extends TestCase {
	public void test() throws TLVException {
		byte[] buffer = new byte[] { 0x01, 0x01, 0x30, 0x02, 0x02, 0x31 };
		TlvParser tlv = new TlvParser(buffer);
		byte[] result = tlv.getValue();

		for (int i = 0; i < buffer.length; i++) {
			assertTrue(buffer[i] == result[i]);
		}

		List<TlvParser> listTlv = tlv.getChildren();

		assertTrue(listTlv.size() == 2);

		result = listTlv.get(0).getValue();

		for (int i = 0; i < result.length; i++) {
			assertTrue(buffer[i + 2] == result[i]);
		}
	}

	public void testTlv() throws IOException {
		Tlv testTlv = new Tlv();
		testTlv.setTag(new byte[] { 0x01 });
		testTlv.setStringValue("test tlv");

		byte[] result = testTlv.getRawByteArray();

		byte[] expect = new byte[] { 0x01, 0x08, 't', 'e', 's', 't', ' ', 't',
				'l', 'v' };
		
		assertTrue(expect.length == result.length);
		
		for(int i=0; i < expect.length; i++)
		{
			assertTrue(expect[i] == result[i]);
		}
		
		Tlv test2 = new Tlv(expect);
		
		assertTrue(test2.getChildren().length == 1);
		assertTrue(test2.getChildren()[0].getStringValue().equals("test tlv"));
	}
	
	public void testTlvOther() throws IOException
	{
		Tlv testTlv1 = new Tlv();
		
		Tlv testTlvC1 = new Tlv();
		Tlv testTlvC2 = new Tlv();
		
		testTlvC1.setTag(new byte[]{0x01});
		testTlvC1.setStringValue("testc1");
		testTlvC2.setTag(new byte[]{0x02});
		testTlvC2.setStringValue("testc2");
		
		testTlv1.addChild(testTlvC1);
		testTlv1.addChild(testTlvC2);
		
		byte[] result = testTlv1.getRawByteArray();
		
		Tlv testTlv2 = new Tlv(result);
		
		assertTrue(testTlv1.getChildren().length == testTlv2.getChildren().length);
		assertTrue(testTlv1.getChildren()[0].getStringValue().equals(testTlv2.getChildren()[0].getStringValue()));
		assertTrue(testTlv1.getChildren()[1].getStringValue().equals(testTlv2.getChildren()[1].getStringValue()));
	}
}
