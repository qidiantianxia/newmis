package com.yada.sdk.packages.transaction.jpos;

import java.io.IOException;

import junit.framework.TestCase;

import com.yada.sdk.packages.comm.Tlv;

public class TlvTester extends TestCase {

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
