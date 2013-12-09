package com.yada.sdk.packages.transaction.jpos;

import org.jpos.iso.ISOException;

import junit.framework.TestCase;

public class PospPackerTester extends TestCase {
	public void test() throws ISOException
	{
		new PospPacker(8);
	}
}
