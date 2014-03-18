package com.yada.sdk.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author tao
 * 
 */
public final class TcpService {

	private AsynchronousServerSocketChannel server;
	private String serviceName;
	private AsynchronousChannelGroup channelGroup;
	private ConcurrentLinkedQueue<DataTransceivers> dataTransceiversQueue;
	private IPackageSplitterFactory splitterFactory;
	private IPackageProcessorFactory processorFactory;
	private long recvTimeout;
	private volatile boolean isOpen = false;

	public TcpService(String serviceName,
			IPackageSplitterFactory splitterFactory,
			IPackageProcessorFactory processorFactory, long recvTimeout)
			throws IOException {
		this.serviceName = serviceName;
		dataTransceiversQueue = new ConcurrentLinkedQueue<DataTransceivers>();
		this.splitterFactory = splitterFactory;
		this.processorFactory = processorFactory;
		this.recvTimeout = recvTimeout;
	}

	public synchronized void listen(InetSocketAddress endPoint)
			throws IOException {
		close();
		channelGroup = AsynchronousChannelGroup.withFixedThreadPool(16,
				Executors.defaultThreadFactory());
		server = AsynchronousServerSocketChannel.open(channelGroup).bind(
				endPoint);

		isOpen = true;
		server.accept(server, new AcceptCompletionHandler());
	}

	public void close() {

		if (isOpen) {
			isOpen = false;
			if (server != null) {
				if (server.isOpen())
					try {
						server.close();
					} catch (IOException e) {
					}
				server = null;
			}

			for (DataTransceivers dt : dataTransceiversQueue) {
				dt.close();
			}

			dataTransceiversQueue.clear();

			if (channelGroup != null) {
				try {
					if (!channelGroup.isShutdown())
						channelGroup.shutdown();

					if (!channelGroup.isTerminated())
						channelGroup.shutdownNow();

					channelGroup.awaitTermination(10, TimeUnit.SECONDS);
				} catch (InterruptedException | IOException e) {
				} finally {
					channelGroup = null;
				}
			}
		}
	}

	public String getServiceName() {
		return serviceName;
	}

	public InetSocketAddress getEndPointStr() throws IOException {
		return (InetSocketAddress) server.getLocalAddress();
	}

	private class AcceptCompletionHandler
			implements
			CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel> {

		@Override
		public void completed(AsynchronousSocketChannel clientChannel,
				AsynchronousServerSocketChannel serverChannel) {

			final AsynchronousSocketChannel _clientChannel = clientChannel;
			serverChannel.accept(serverChannel, this);
			dataTransceiversQueue.add(new DataTransceivers(clientChannel,
					splitterFactory.create(), processorFactory.create(),
					new IChannelNeedToCloseHandler() {

						@Override
						public void needToCloseCallback(DataTransceivers sender, String message) {
							if (_clientChannel.isOpen()) {
								try {
									_clientChannel.close();
								} catch (IOException e) {
								}
							}
							dataTransceiversQueue.remove(sender);
						}
					}, recvTimeout));
		}

		@Override
		public void failed(Throwable exc,
				AsynchronousServerSocketChannel attachment) {
			close();
		}
	}
}
