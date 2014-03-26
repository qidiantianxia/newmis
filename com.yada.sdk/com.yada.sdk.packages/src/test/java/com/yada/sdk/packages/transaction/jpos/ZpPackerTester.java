package com.yada.sdk.packages.transaction.jpos;

import org.jpos.iso.ISOException;

import junit.framework.TestCase;

public class ZpPackerTester extends TestCase {
	public void test() throws ISOException
	{
		new ZpPacker(0);
	}
}
