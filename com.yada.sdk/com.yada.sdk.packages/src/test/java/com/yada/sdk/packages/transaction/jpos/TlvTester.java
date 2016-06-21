package com.yada.sdk.packages.transaction.jpos;

import java.io.IOException;

import com.payneteasy.tlv.BerTag;
import com.payneteasy.tlv.BerTlvBuilder;
import junit.framework.TestCase;

public class TlvTester extends TestCase {

	public void testTlv() throws IOException {
    byte[] result =  new BerTlvBuilder().addText(new BerTag(0x01), "test tlv").buildArray();

		byte[] expect = new byte[] { 0x01, 0x08, 't', 'e', 's', 't', ' ', 't',
				'l', 'v' };

		assertTrue(expect.length == result.length);
	}
}
