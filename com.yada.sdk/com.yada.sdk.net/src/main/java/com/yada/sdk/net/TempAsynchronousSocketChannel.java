package com.yada.sdk.net;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketOption;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.CompletionHandler;
import java.nio.channels.spi.AsynchronousChannelProvider;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/***
 * 临时异步套接字
 * <p>
 * 当Aix支持NIO2时，废掉次类(***IBM)，此类非常不完善，谨慎使用
 * 
 * @author tao
 * 
 */
class TempAsynchronousSocketChannel extends AsynchronousSocketChannel {
	private Socket socket = null;
	private final ReadWriteLock closeLock = new ReentrantReadWriteLock();

	public TempAsynchronousSocketChannel() {
		this(AsynchronousChannelProvider.provider());
	}

	protected TempAsynchronousSocketChannel(AsynchronousChannelProvider provider) {
		super(provider);

	}

	@Override
	public void close() throws IOException {
		if (socket != null) {
			if (!socket.isClosed()) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
			socket = null;
		}
	}

	@Override
	public boolean isOpen() {
		return socket == null ? false : !socket.isClosed();
	}

	@Override
	public SocketAddress getLocalAddress() throws IOException {
		return socket.getLocalSocketAddress();
	}

	@Override
	public <T> T getOption(SocketOption<T> name) throws IOException {
		throw new IOException();
	}

	@Override
	public Set<SocketOption<?>> supportedOptions() {
		throw new RuntimeException();
	}

	@Override
	public AsynchronousSocketChannel bind(SocketAddress local)
			throws IOException {
		socket.bind(local);
		return this;
	}

	@Override
	public Future<Void> connect(SocketAddress remote) {
		final SocketAddress _remote = remote;
		FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				socket.connect(_remote);
				return null;
			}
		});
		return task;
	}

	@Override
	public <A> void connect(SocketAddress remote, A attachment,
			CompletionHandler<Void, ? super A> handler) {
		if (handler == null)
			throw new NullPointerException("'handler' is null");

		final SocketAddress _remote = remote;
		final A _attachment = attachment;
		final CompletionHandler<Void, ? super A> _handler = handler;

		FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				try {
					socket.connect(_remote);
					_handler.completed(null, _attachment);
				} catch (Exception e) {
					_handler.failed(e, _attachment);
				}
				return null;
			}
		});

		try {
			task.get();
		} catch (InterruptedException | ExecutionException e) {
			_handler.failed(e, attachment);
		}
	}

	@Override
	public SocketAddress getRemoteAddress() throws IOException {
		return socket.getRemoteSocketAddress();
	}

	@Override
	public Future<Integer> read(ByteBuffer dst) {
		final ByteBuffer _dst = dst;

		if (dst.isReadOnly())
			throw new IllegalArgumentException("Read-only buffer");

		FutureTask<Integer> task = new FutureTask<Integer>(
				new Callable<Integer>() {

					@Override
					public Integer call() throws Exception {
						try {
							int count = socket.getInputStream().read(
									_dst.array(), _dst.position(),
									_dst.remaining());
							_dst.position(_dst.position() + count);
							return new Integer(count);
						} catch (Exception e) {
							return new Integer(-1);
						}
					}
				});

		return task;
	}

	@Override
	public <A> void read(ByteBuffer dst, long timeout, TimeUnit unit,
			A attachment, CompletionHandler<Integer, ? super A> handler) {
		if (handler == null)
			throw new NullPointerException("'handler' is null");
		if (dst.isReadOnly())
			throw new IllegalArgumentException("Read-only buffer");

		final ByteBuffer _dst = dst;
		final A _attachment = attachment;
		final CompletionHandler<Integer, ? super A> _handler = handler;

		FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				try {
					int count = socket.getInputStream().read(_dst.array(),
							_dst.position(), _dst.remaining());
					_dst.position(_dst.position() + count);
					_handler.completed(new Integer(count), _attachment);
				} catch (Exception e) {
					_handler.failed(e, _attachment);
				}
				return null;
			}
		});

		try {
			task.get(timeout, unit);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			_handler.failed(e, attachment);
		}
	}

	@Override
	public <A> void read(ByteBuffer[] dsts, int offset, int length,
			long timeout, TimeUnit unit, A attachment,
			CompletionHandler<Long, ? super A> handler) {
		if (handler == null)
			throw new NullPointerException("'handler' is null");
		if ((offset < 0) || (length < 0) || (offset > dsts.length - length))
			throw new IndexOutOfBoundsException();

		for (int i = offset; i < offset + length; i++) {
			if (dsts[i].isReadOnly())
				throw new IllegalArgumentException("Read-only buffer");
		}

		final ByteBuffer[] _dsts = dsts;
		final A _attachment = attachment;
		final CompletionHandler<Long, ? super A> _handler = handler;
		final int _offset = offset;
		final int _length = length;

		FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				long totalCount = 0;
				try {
					for (int i = _offset; i < _offset + _length; i++) {
						int count = socket.getInputStream().read(
								_dsts[i].array(), _dsts[i].position(),
								_dsts[i].remaining());
						_dsts[i].position(_dsts[i].position() + count);

						totalCount += count;

						if (_dsts[i].position() != _dsts[i].capacity())
							break;

					}
				} catch (Exception e) {
					if (totalCount != 0)
						// 当totalCount != 0, 此异常可能发生两次，因为completed里可能再次注册读数据
						_handler.completed(new Long(totalCount), _attachment);
					_handler.failed(e, _attachment);
				}
				return null;
			}
		});

		try {
			task.get(timeout, unit);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			_handler.failed(e, attachment);
		}
	}

	@Override
	public <T> AsynchronousSocketChannel setOption(SocketOption<T> name, T value)
			throws IOException {
		throw new IOException();
	}

	@Override
	public AsynchronousSocketChannel shutdownInput() throws IOException {
		socket.shutdownInput();
		return this;
	}

	@Override
	public AsynchronousSocketChannel shutdownOutput() throws IOException {
		socket.shutdownOutput();
		return this;
	}

	@Override
	public Future<Integer> write(ByteBuffer src) {
		final ByteBuffer _src = src;

		FutureTask<Integer> task = new FutureTask<Integer>(
				new Callable<Integer>() {

					@Override
					public Integer call() throws Exception {
						int count = _src.remaining();
						try {
							socket.getOutputStream().write(_src.array(),
									_src.position(), _src.remaining());
							_src.position(_src.position() + _src.remaining());
						} catch (Exception e) {
							count = -1;
						}
						return new Integer(count);
					}
				});

		return task;
	}

	@Override
	public <A> void write(ByteBuffer src, long timeout, TimeUnit unit,
			A attachment, CompletionHandler<Integer, ? super A> handler) {
		final ByteBuffer _src = src;
		final A _attachment = attachment;
		final CompletionHandler<Integer, ? super A> _handler = handler;

		FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				int count = _src.remaining();
				try {
					socket.getOutputStream().write(_src.array(),
							_src.position(), _src.remaining());
					_src.position(_src.position() + _src.remaining());
					_handler.completed(new Integer(count), _attachment);
				} catch (Exception e) {
					_handler.failed(e, _attachment);
				}
				return null;
			}
		});

		try {
			task.get(timeout, unit);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			_handler.failed(e, _attachment);
		}
	}

	@Override
	public <A> void write(ByteBuffer[] srcs, int offset, int length,
			long timeout, TimeUnit unit, A attachment,
			CompletionHandler<Long, ? super A> handler) {
		final ByteBuffer[] _srcs = srcs;
		final A _attachment = attachment;
		final CompletionHandler<Long, ? super A> _handler = handler;
		final int _offset = offset;
		final int _length = length;

		FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				long count = 0;
				try {
					for (int i = _offset; i < _offset + _length; i++) {
						ByteBuffer _src = _srcs[i];
						socket.getOutputStream().write(_src.array(),
								_src.position(), _src.remaining());
						count += _src.remaining();
						_src.position(_src.position() + _src.remaining());
					}
					_handler.completed(new Long(count), _attachment);
				} catch (Exception e) {
					if (count != 0)
						// 当totalCount != 0, 此异常可能发生两次，因为completed里可能再次注册写数据
						_handler.completed(new Long(count), _attachment);
					_handler.failed(e, _attachment);
				}
				return null;
			}
		});

		try {
			task.get(timeout, unit);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			_handler.failed(e, _attachment);
		}
	}

	final void begin() throws IOException {
		closeLock.readLock().lock();
		if (!isOpen())
			throw new ClosedChannelException();
	}

	final void end() {
		closeLock.readLock().unlock();
	}

}
