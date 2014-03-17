package com.yada.sdk.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class AsyncTcpClient {
	private InetSocketAddress hostAddress;
	private IPackageSplitterFactory pkgSplitterFactory;
	private IPackageProcessorFactory processorFactory;
	private int timeout;
	private DataTransceivers dt;
	private boolean autoReconnection;

	public AsyncTcpClient(InetSocketAddress hostAddress,
			IPackageSplitterFactory pkgSplitterFactory,
			IPackageProcessorFactory processorFactory, int timeout,
			boolean autoReconnection) {
		this.hostAddress = hostAddress;
		this.pkgSplitterFactory = pkgSplitterFactory;
		this.timeout = timeout;
		this.processorFactory = processorFactory;
		this.autoReconnection = autoReconnection;
	}

	public void open() throws IOException {
		close();
		AsynchronousSocketChannel socket = new TempAsynchronousSocketChannel();
		socket.connect(hostAddress);
		dt = new DataTransceivers(socket, pkgSplitterFactory.create(),
				processorFactory.create(), new IChannelCloseHandler() {

					@Override
					public void closeCallback(DataTransceivers sender) {
						if (autoReconnection) {
							FutureTask<Void> task = new FutureTask<Void>(
									new Callable<Void>() {

										@Override
										public Void call() throws Exception {
											while (!Thread.currentThread()
													.isInterrupted()) {
												try {
													if (!isOpen())
														open();
												} catch (IOException e) {
													continue;
												}

												if (isOpen())
													break;
												else
													try {
														Thread.sleep(5000);
													} catch (InterruptedException e) {
														break;
													}
											}
											return null;
										}
									});
							Thread thread = new Thread(task);
							thread.start();
						}
					}
				}, timeout);
	}

	public void close() {
		if (dt != null && dt.isValid()) {
			dt.close();
		}

		dt = null;
	}

	public boolean isOpen() {
		return dt == null ? false : dt.isValid();
	}

	public void send(ByteBuffer sendBuffer) throws IOException {
		try {
			dt.send(sendBuffer);
		} catch (InterruptedException e) {
		}
	}
}
