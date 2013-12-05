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
 * 数据收发器包含了接收超时{@link #recvTimeout}，关闭回掉的处理{@link IChannelCloseHandler}。
 * </p>
 * </blockquote>
 * 
 * @author tao
 * 
 */
public class DataTransceivers {
	private AsynchronousSocketChannel asyncChannel;
	private LinkedBlockingQueue<ByteBuffer> bufferQueue;
	private volatile boolean valid = true;
	private IPackageSplitter splitter;
	private IPackageProcessor processor;
	private DataTransceivers owner;
	private IChannelCloseHandler closeHandler;
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
	 * @param closeHandler
	 * @param recvTimeout
	 *            接收超时时间 <i>单位：毫秒</i>
	 */
	DataTransceivers(AsynchronousSocketChannel channel,
			IPackageSplitter splitter, IPackageProcessor processor,
			IChannelCloseHandler closeHandler, long recvTimeout) {
		this.asyncChannel = channel;
		this.splitter = splitter;
		this.processor = processor;
		this.closeHandler = closeHandler;
		this.recvTimeout = recvTimeout;
		owner = this;

		// 构建阻塞队列
		bufferQueue = new LinkedBlockingQueue<ByteBuffer>();

		pool = Executors.newFixedThreadPool(50);
		pool.execute(new Runnable() {

			@Override
			public void run() {
				try {
					while (true) {
						ByteBuffer buffer = bufferQueue.take();
						asyncChannel.write(buffer).get();
					}
				} catch (InterruptedException | ExecutionException e) {
				}
			}
		});
		
		// 信道读取数据
		ByteBuffer readBuffer = ByteBuffer.allocate(2048);
		this.asyncChannel.read(readBuffer, this.asyncChannel,
				new ReadCompletionHandler(readBuffer));
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
	 * {@link IChannelCloseHandler#closeCallback(DataTransceivers)}。
	 * </p>
	 * </blockquote>
	 */
	public void close() {

		if (valid) {
			valid = false;
			if (asyncChannel.isOpen())
				try {
					asyncChannel.close();
				} catch (IOException e) {
				}
			closeHandler.closeCallback(this);
			
			if(!pool.isShutdown())
				pool.shutdown();
			
			if(!pool.isTerminated())
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
				close();
				return;
			}

			// 从新组织原始数据，整理成系统能识别的业务数据包
			while (true) {
				// 准备缓存
				buffer.flip();

				// 整理缓存
				final ByteBuffer pkg = splitter.getPackage(buffer);

				if (pkg != null) {
					// 得到一个整理好的数据包

					pool.execute(new Runnable(){
						@Override
						public void run() {
							// 处理数据包
							processor.proc(new DataTransceiversContext(owner, pkg));
						}});

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
							close();
							return;
						}
					} catch (InterruptedException | ExecutionException e) {
						// 线程中断
						close();
						return;
					} catch (TimeoutException e) {
						// 接收超时
						close();
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
			close();
		}
	}
}