package com.yada.sdk.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncTcpClient {
	private final static Logger log = LoggerFactory.getLogger(AsyncTcpClient.class);
	private InetSocketAddress hostAddress;
	private IPackageSplitterFactory pkgSplitterFactory;
	private IPackageProcessorFactory processorFactory;
	private int timeout;
	private DataTransceivers dt;
	private ExecutorService pool;

	public AsyncTcpClient(InetSocketAddress hostAddress, IPackageSplitterFactory pkgSplitterFactory, IPackageProcessorFactory processorFactory, int timeout,
			boolean autoReconnection) {
		this.hostAddress = hostAddress;
		this.pkgSplitterFactory = pkgSplitterFactory;
		this.timeout = timeout;
		this.processorFactory = processorFactory;

		if (autoReconnection) {
			pool = Executors.newFixedThreadPool(1);

			pool.execute(new Runnable() {

				@Override
				public void run() {
					try {
						while (true) {
							Thread.sleep(5000);
							open();
						}
					} catch (InterruptedException e) {
					}
				}
			});
		}
	}

	public synchronized void open() {
		if (isOpen())
			return;

		final AsynchronousSocketChannel socket = createChannel();
		try {
			socket.connect(hostAddress).get();
			dt = new DataTransceivers(socket, pkgSplitterFactory.create(), processorFactory.create(), new IChannelNeedToCloseHandler() {

				@Override
				public void needToCloseCallback(DataTransceivers sender) {
					if (socket.isOpen()) {
						try {
							socket.shutdownInput();
							socket.shutdownOutput();

							socket.close();
						} catch (IOException e) {
						}
					}
				}
			}, timeout);
			log.warn("连接成功，主机地址：{}", hostAddress);
		} catch (InterruptedException | ExecutionException e1) {
			log.error("连接失败，主机地址：{}", hostAddress, e1);
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					log.error("关闭SOCKET异常。", e);
				}
			}
		}
	}

	public void close() {
		if (dt != null && dt.isValid()) {
			dt.close();
		}

		dt = null;

		if (pool != null) {
			if (!pool.isShutdown())
				pool.shutdown();

			if (!pool.isTerminated())
				pool.shutdownNow();

			try {
				pool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
			pool = null;
		}
	}

	public boolean isOpen() {
		return dt == null ? false : dt.isValid();
	}

	public void send(ByteBuffer sendBuffer) throws InterruptedException {
		dt.send(sendBuffer);
	}

	protected AsynchronousSocketChannel createChannel() {
		return new TempAsynchronousSocketChannel();
	}
}
