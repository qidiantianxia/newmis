package com.yada.sdk.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * 数据收发器 <blockquote>
 * <p>
 * 从{@link TcpService}上联的信道收发管理器，配合{@link IPackageSplitter}和
 * {@link IPackageProcessor}使用。 {@link IPackageSplitter}负责把上送的数据流分割成业务数据包，
 * {@link IPackageProcessor}负责处理包。
 * </p>
 * <p>
 * 每个数据包的上下文由{@link DataTransceiversContext}传递，当需要向信道发送信息时，调用
 * {@link #send(ByteBuffer)}即可。
 * </p>
 * <p>
 * 数据收发器包含了接收超时{@link #recvTimeout}，关闭回掉的处理{@link IChannelNeedToCloseHandler}。
 * </p>
 * </blockquote>
 * 
 * @author tao
 * 
 */
public class DataTransceivers {
	private final static Logger logger = LoggerFactory
			.getLogger(DataTransceivers.class);
	private String remoteAddress;
	private String localAddress;
	private AsynchronousSocketChannel asyncChannel;
	private LinkedBlockingQueue<ByteBuffer> bufferQueue;
	private volatile boolean valid = true;
	private IPackageSplitter splitter;
	private IPackageProcessor processor;
	private DataTransceivers owner;
	private IChannelNeedToCloseHandler channelNeedToCloseHandler;
	private long recvTimeout;
	private ExecutorService pool;

	/**
	 * 数据收发器 <blockquote>
	 * <p>
	 * 该构造函数只能在{@link TcpService}接收到链接请求时构造。
	 * </p>
	 * </blockquote>
	 * 
	 * @param channel
	 *            数据收发器信道
	 * @param splitter
	 *            数据包分割器
	 * @param processor
	 *            包处理器
	 * @param channelNeedToCloseHandler
	 * @param recvTimeout
	 *            接收超时时间 <i>单位：毫秒</i>
	 */
	DataTransceivers(AsynchronousSocketChannel channel,
			IPackageSplitter splitter, IPackageProcessor processor,
			IChannelNeedToCloseHandler channelNeedToCloseHandler,
			long recvTimeout) {
		try {
			this.remoteAddress = channel.getRemoteAddress().toString();
			this.localAddress = channel.getLocalAddress().toString();
		} catch (IOException e) {
			// 当套接字关闭时，获取不了地址
			// 一个死人还能分辨男女呢，一个关闭的套接字就不能获得一个地址属性！！
			// 非要说：“这是个死人，不能看，怕吓着你，生殖器可能有蛆。”，我去你MB
			// 我操你妈，一群sb，吃饱了没事想多了吧！！！！调死你爷爷了，出离愤怒了，只想反射弄死你MB！！
		}

		this.asyncChannel = channel;
		this.splitter = splitter;
		this.processor = processor;
		this.channelNeedToCloseHandler = channelNeedToCloseHandler;
		this.recvTimeout = recvTimeout;
		owner = this;

		// 构建阻塞队列
		bufferQueue = new LinkedBlockingQueue<ByteBuffer>();

		pool = Executors.newFixedThreadPool(40);
		pool.execute(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						ByteBuffer buffer = bufferQueue.take();
						int start = buffer.position();
						asyncChannel.write(buffer).get();
						int end = buffer.position();

						if (logger.isDebugEnabled()) {
							logger.debug(
									"发送数据 remote address{} local address{}"
											+ System.getProperty("line.separator")
											+ "{}",
									remoteAddress,
									localAddress,
									byte2Hex(buffer.array(), start, end - start));
						}
					}
				} catch (InterruptedException | ExecutionException e) {
					if (!bufferQueue.isEmpty()) {
						logger.error("数据无法发送", e);
						while (!bufferQueue.isEmpty()) {
							ByteBuffer buffer;
							try {
								buffer = bufferQueue.take();
								logger.error(byte2Hex(buffer));
							} catch (InterruptedException e1) {
							}
						}
					}
				}
			}
		});

		// 信道读取数据
		ByteBuffer readBuffer = ByteBuffer.allocate(2048);
		ReadCompletionHandler readHandler = new ReadCompletionHandler(
				readBuffer);
		this.asyncChannel.read(readBuffer, this.asyncChannel, readHandler);
	}

	/**
	 * 向信道发送数据 <blockquote>
	 * <p>
	 * 发送的数据只是被放到发送队列里，没有真正的发送。发送有数据发送线程异步处理。
	 * </p>
	 * </blockquote>
	 * 
	 * @param buffer
	 *            数据
	 * @throws InterruptedException
	 *             当线程被中断时抛出
	 */
	public void send(ByteBuffer buffer) throws InterruptedException {
		bufferQueue.put(splitter.pack(buffer));
	}

	/**
	 * 返回数据收发器是否有效
	 * 
	 * @return {@code true}有效，否则无效。
	 */
	public boolean isValid() {
		return valid;
	}

	/**
	 * 关闭数据收发器 <blockquote>
	 * <p>
	 * 当数据收发器有效时{@link #isValid()}，会关闭信道并通知回掉接口
	 * {@link IChannelNeedToCloseHandler#needToCloseCallback(DataTransceivers)}。
	 * </p>
	 * </blockquote>
	 */
	public void close() {
		logger.info("连接主动关闭remote addresslocalhost{} local address {}",
				remoteAddress, localAddress);
		internalClose();
	}

	private void internalClose() {
		if (valid) {
			valid = false;
			channelNeedToCloseHandler.needToCloseCallback(this);

			if (!pool.isShutdown())
				pool.shutdown();

			if (!pool.isTerminated())
				pool.shutdownNow();

			try {
				pool.awaitTermination(10, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * 信道的异步读取句柄
	 * 
	 * @author tao
	 * 
	 */
	private class ReadCompletionHandler implements
			CompletionHandler<Integer, AsynchronousSocketChannel> {
		private ByteBuffer buffer;

		/**
		 * 
		 * @param buffer
		 *            缓冲
		 */
		public ReadCompletionHandler(ByteBuffer buffer) {
			this.buffer = buffer;
		}

		@Override
		public void completed(Integer result,
				AsynchronousSocketChannel clientChannel) {

			// 获取当前的开始时间
			long beginDateTime = Calendar.getInstance().getTimeInMillis();

			// 当result为-1时，表示客户端已经主动断开
			if (result == -1 || Thread.interrupted()) {
				logger.warn("远程主机已经断开1 remote address{} local address {}",
						remoteAddress, localAddress);
				internalClose();
				return;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("收到数据："
						+ System.getProperty("line.separator")
						+ byte2Hex(buffer.array(),
								buffer.position() - result.intValue(),
								result.intValue()));
			}
			// 从新组织原始数据，整理成系统能识别的业务数据包
			while (true) {
				// 准备缓存
				buffer.flip();

				// 整理缓存
				final ByteBuffer pkg = splitter.getPackage(buffer);

				if (pkg != null) {
					// 得到一个整理好的数据包

					pool.execute(new Runnable() {
						@Override
						public void run() {
							// 处理数据包
							if(logger.isDebugEnabled())
							{
								logger.debug("处理数据包："
										+ System.getProperty("line.separator")
										+ byte2Hex(pkg));
							}
							processor.proc(new DataTransceiversContext(owner,
									pkg));
						}
					});

					if (buffer.hasRemaining()) {
						// 还有多余数据没有处理

						// 从新设置开始时间
						beginDateTime = Calendar.getInstance()
								.getTimeInMillis();

						// 压缩缓存
						buffer.compact();
						continue;
					} else {
						// 没有多余的数据
						buffer.clear();
						break;
					}
				} else {
					// 数据缓存中的数据不足，需要继续处理

					// 清理缓存
					buffer.clear();
					try {

						// 阻塞读取数据在指定的时间内
						int ret = clientChannel.read(buffer).get(
								recvTimeout
										- Calendar.getInstance()
												.getTimeInMillis()
										+ beginDateTime, TimeUnit.MILLISECONDS);

						if (ret == -1) {
							// 客户端已经主动断开
							logger.warn(
									"远程主机已经断开2 remote address{} local address {}",
									remoteAddress, localAddress);

							internalClose();
							return;
						}
					} catch (InterruptedException | ExecutionException e) {
						// 线程中断
						logger.warn("线程中断 remote address{} local address {}",
								remoteAddress, localAddress);
						internalClose();
						return;
					} catch (TimeoutException e) {
						// 接收超时
						logger.warn("接收超时 remote address{} local address {}",
								remoteAddress, localAddress);
						internalClose();
						return;
					}
				}
			}

			// 继续异步读取
			clientChannel.read(buffer, clientChannel, this);
		}

		@Override
		public void failed(Throwable exc,
				AsynchronousSocketChannel clientChannel) {
			// 出错
			logger.warn(String.format("失败 remote address%s local address%s",
					remoteAddress, localAddress), exc);
			internalClose();
		}
	}

	private static String byte2Hex(ByteBuffer buffer) {
		return byte2Hex(buffer.array(), buffer.position(), buffer.remaining());
	}

	private static String byte2Hex(byte[] buffer, int index, int length) {
		StringBuilder sb = new StringBuilder();
		for (int i = index; i < index + length; i++) {
			sb.append(String.format("%02X ", buffer[i]));
			if (i != 0 && i % 8 == 0)
				sb.append(" ");
			if (i != 0 && i % 16 == 0)
				sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}