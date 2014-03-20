package com.yada.sdk.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AsyncTcpClientTest {

	@Mock
	private IPackageProcessorFactory ppf;
	private IPackageProcessor pp;
	@Mock
	private IPackageSplitterFactory psf;
	private IPackageSplitter ps;

	private TcpService service;
	private InetSocketAddress address;
	private AsyncTcpClient client;

	@Before
	public void setup() throws IOException {
		pp = new IPackageProcessor() {

			@Override
			public void proc(DataTransceiversContext context) {
				try {
					context.getDataTransceivers().send(context.getData());
				} catch (InterruptedException e) {
				}
			}
		};
		Mockito.spy(pp);

		ps = new IPackageSplitter() {

			@Override
			public ByteBuffer pack(ByteBuffer newData) {
				ByteBuffer buffer = ByteBuffer.allocate(newData.remaining());
				buffer.put(newData);
				buffer.flip();
				return buffer;
			}

			@Override
			public ByteBuffer getPackage(ByteBuffer newData) {
				ByteBuffer buffer = ByteBuffer.allocate(newData.remaining());
				buffer.put(newData);
				buffer.flip();
				return buffer;
			}
		};
		Mockito.when(ppf.create()).thenReturn(pp);
		Mockito.when(psf.create()).thenReturn(ps);
		service = new TcpService("test service", psf, ppf, 0);
		address = new InetSocketAddress("localhost", 4321);

		service.listen(address);
	}

	@After
	public void destroy() throws InterruptedException {
		service.close();
	}

	@Test
	public void testConnecte() throws IOException {
		client = new AsyncTcpClient(address, psf, ppf, 0, false);
		client.open();
		client.close();
	}

	@Test
	public void testSendMessage() throws InterruptedException, IOException {
		final Object lock = new Object();
		IPackageProcessor pp = new IPackageProcessor() {

			@Override
			public void proc(DataTransceiversContext context) {
				synchronized (lock) {
					lock.notify();
				}
				Assert.assertTrue(context.getData().array()[0] == 1
						&& context.getData().array()[1] == 2);
			}
		};

		IPackageProcessorFactory factory = Mockito
				.mock(IPackageProcessorFactory.class);
		Mockito.when(factory.create()).thenReturn(pp);
		client = new AsyncTcpClient(address, psf, factory, 10, false);
		client.open();
		ByteBuffer buffer = ByteBuffer.allocate(2).put(new byte[] { 1, 2 });
		buffer.flip();
		client.send(buffer);
		synchronized (lock) {
			lock.wait();
		}

		client.close();
	}
	
	@Test
	public void testReconnect() throws Exception
	{
		final Object lock = new Object();
		IPackageProcessor pp = new IPackageProcessor() {

			@Override
			public void proc(DataTransceiversContext context) {
				synchronized (lock) {
					lock.notify();
				}
				Assert.assertTrue(context.getData().array()[0] == 1
						&& context.getData().array()[1] == 2);
			}
		};

		IPackageProcessorFactory factory = Mockito
				.mock(IPackageProcessorFactory.class);
		Mockito.when(factory.create()).thenReturn(pp);
		client = new AsyncTcpClient(address, psf, factory, 10, true);
		
		client.open();
		Assert.assertTrue(client.isOpen());
		service.close();
		Thread.sleep(3000);
		Assert.assertTrue(!client.isOpen());
		service.listen(address);
		Thread.sleep(2500);

		Assert.assertTrue(client.isOpen());
	}
}
