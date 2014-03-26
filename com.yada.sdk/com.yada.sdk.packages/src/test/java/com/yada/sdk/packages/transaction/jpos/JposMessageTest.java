package com.yada.sdk.packages.transaction.jpos;

import org.jpos.iso.ISOException;
import org.junit.Assert;
import org.junit.Test;

public class JposMessageTest {

	@Test
	public void testToString() throws ISOException {
		JposMessage message = new JposMessage();
		message.set(0, "a");
		message.set(50, "b");
		String expected = "Field[000]value[a]" + System.getProperty("line.separator") + "Field[050]value[b]" + System.getProperty("line.separator");
		Assert.assertEquals(expected, message.toString());
	}
}
