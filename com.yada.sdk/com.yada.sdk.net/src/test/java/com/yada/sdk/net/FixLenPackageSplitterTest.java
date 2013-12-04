package com.yada.sdk.net;

import java.nio.ByteBuffer;

import junit.framework.TestCase;

public class FixLenPackageSplitterTest extends TestCase {
	public void test() {
		FixLenPackageSplitterFactory factory = new FixLenPackageSplitterFactory(
				2, false);
		IPackageSplitter splitter = factory.create();
		ByteBuffer buffer = ByteBuffer.wrap(new byte[] { 0, 1, 2 });
		ByteBuffer pkg = splitter.getPackage(buffer);
		
		assertTrue(pkg.remaining() == 1);
		assertTrue(pkg.get() == 2);
		assertTrue(buffer.remaining() == 0);
		
		buffer = ByteBuffer.wrap(new byte[]{0, 2, 3});
		pkg = splitter.getPackage(buffer);
		
		assertNull(pkg);
		
		buffer = ByteBuffer.wrap(new byte[]{4, 0, 1, 2});
		pkg = splitter.getPackage(buffer);
		
		assertNotNull(pkg);
		assertTrue(pkg.remaining() == 2);
		assertTrue(pkg.get() == 3);
		assertTrue(pkg.get() == 4);
		assertTrue(buffer.remaining() == 3);
		
		pkg = splitter.getPackage(buffer);
		
		assertTrue(pkg.remaining() == 1);
		assertTrue(pkg.get() == 2);
		assertTrue(buffer.remaining() == 0);
	}
	
	public void testReverse()
	{
		FixLenPackageSplitterFactory factory = new FixLenPackageSplitterFactory(
				2, true);
		IPackageSplitter splitter = factory.create();
		ByteBuffer buffer = ByteBuffer.wrap(new byte[] { 1, 0, 2 });
		ByteBuffer pkg = splitter.getPackage(buffer);
		
		assertTrue(pkg.remaining() == 1);
		assertTrue(pkg.get() == 2);
		assertTrue(buffer.remaining() == 0);
		
		buffer = ByteBuffer.wrap(new byte[]{2, 0, 3});
		pkg = splitter.getPackage(buffer);
		
		assertNull(pkg);
		
		buffer = ByteBuffer.wrap(new byte[]{4, 1, 0, 2});
		pkg = splitter.getPackage(buffer);
		
		assertNotNull(pkg);
		assertTrue(pkg.remaining() == 2);
		assertTrue(pkg.get() == 3);
		assertTrue(pkg.get() == 4);
		assertTrue(buffer.remaining() == 3);
		
		pkg = splitter.getPackage(buffer);
		
		assertTrue(pkg.remaining() == 1);
		assertTrue(pkg.get() == 2);
		assertTrue(buffer.remaining() == 0);
	}
}
