package com.yada.sdk.net;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import com.yada.sdk.net.DataTransceiversContext;
import com.yada.sdk.net.IPackageProcessor;
import com.yada.sdk.net.IPackageProcessorFactory;
import com.yada.sdk.net.IPackageSplitter;
import com.yada.sdk.net.IPackageSplitterFactory;
import com.yada.sdk.net.TcpService;

import junit.framework.TestCase;

public class TcpServiceTest extends TestCase {
	
	public void test() throws Exception
	{
		TestPackageSplitterFactory sf = new TestPackageSplitterFactory();
		TestPackageProcessorFactory pf = new TestPackageProcessorFactory();
		TcpService service = new TcpService("test service", sf, pf, 10);
		
		InetSocketAddress address = new InetSocketAddress("localhost", 4321);
		
		service.listen(address);
		service.close();
		service.listen(address);
		service.close();
		
		service.listen(address);
		
		AsynchronousSocketChannel channel = AsynchronousSocketChannel.open();
		channel.connect(address).get();
		
		byte[] testData = new byte[]{1,2};
		ByteBuffer buffer = ByteBuffer.wrap(testData);
		channel.write(buffer).get();
		buffer = ByteBuffer.allocate(10);
		channel.read(buffer).get();
		buffer.flip();
		assertTrue(buffer.remaining() == testData.length);
		assertTrue(buffer.get() == testData[0]);
		assertTrue(buffer.get() == testData[1]);
		
		channel.close();
		service.close();
	}
	
	private class TestPackageSplitterFactory implements IPackageSplitterFactory
	{

		@Override
		public IPackageSplitter create() {
			IPackageSplitter splitter = new IPackageSplitter(){

				@Override
				public ByteBuffer getPackage(ByteBuffer newData) {
					ByteBuffer buffer = ByteBuffer.allocate(newData.remaining());
					buffer.put(newData);
					buffer.flip();
					return buffer;
				}

				@Override
				public ByteBuffer pack(ByteBuffer newData) {
					ByteBuffer buffer = ByteBuffer.allocate(newData.remaining());
					buffer.put(newData);
					buffer.flip();
					return buffer;
				}};
			return splitter;
		}
	}
	
	private class TestPackageProcessorFactory implements IPackageProcessorFactory
	{
		@Override
		public IPackageProcessor create() {
			IPackageProcessor processor = new IPackageProcessor(){

				@Override
				public void proc(DataTransceiversContext context) {
					try {
						context.getDataTransceivers().send(context.getData());
					} catch (InterruptedException e) {
					}
				}};
			return processor;
		}	
	}
}
