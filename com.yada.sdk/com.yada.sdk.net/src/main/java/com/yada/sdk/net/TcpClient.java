package com.yada.sdk.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

public class TcpClient {
	private Socket socket;
	private InetSocketAddress hostAddress;
	private IPackageSplitterFactory pkgSplitterFactory;
	private IPackageSplitter pkgSplitter;
	private ByteBuffer buffer;
	private int timeout;

	public TcpClient(InetSocketAddress hostAddress,
			IPackageSplitterFactory pkgSplitterFactory, int timeout) {
		this.hostAddress = hostAddress;
		this.pkgSplitterFactory = pkgSplitterFactory;
		buffer = ByteBuffer.allocate(2048);
		this.timeout = timeout;
	}

	public void open() throws IOException {
		close();
		socket = new Socket();
		socket.connect(hostAddress);
		socket.setSoTimeout(timeout);
		pkgSplitter = pkgSplitterFactory.create();
	}

	public void close() {
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

	public boolean isOpen() {
		return socket == null ? false : !socket.isClosed();
	}

	public ByteBuffer send(ByteBuffer sendBuffer) throws IOException {
		ByteBuffer rawBuffer = pkgSplitter.pack(sendBuffer);

		while (rawBuffer.hasRemaining()) {
			byte[] raw = new byte[rawBuffer.remaining()];
			rawBuffer.put(raw);
			socket.getOutputStream().write(raw);
		}

		ByteBuffer recvBuffer = null;

		while (recvBuffer == null) {
			try {
				int count = socket.getInputStream().read(buffer.array());

				if (count == -1) {
					throw new IOException("Server is closed!");
				}
				
				buffer.limit(count);

				recvBuffer = pkgSplitter.getPackage(buffer);
				buffer.clear();
			} catch (SocketTimeoutException tex) {
				throw new IOException("timeout!");
			}
		}

		return recvBuffer;
	}
}
