package com.yada.sdk.net;

import java.nio.ByteBuffer;

public class FixLenPackageSplitter implements IPackageSplitter {

	private int packagHeadByteSize;
	private boolean needReverse;
	private ByteBuffer headSizeBuffer;
	private ByteBuffer buffer;

	public FixLenPackageSplitter(int packagHeadByteSize,
			boolean needReverse) {
		this.packagHeadByteSize = packagHeadByteSize;
		this.needReverse = needReverse;
		headSizeBuffer = ByteBuffer.allocate(packagHeadByteSize);
	}

	@Override
	public ByteBuffer getPackage(ByteBuffer newData) {

		if (headSizeBuffer.hasRemaining()) {
			if (newData.hasRemaining()) {

				fillByteBuffer(headSizeBuffer, newData);

				if (headSizeBuffer.hasRemaining())
					return null;
				else {
					int len = 0;
					byte[] raw = headSizeBuffer.array();
					for (int i = 0; i < packagHeadByteSize; i++) {
						len = needReverse ? (len << 8)
								+ getUbyte(raw[packagHeadByteSize - 1 - i]) : (len << 8)
								+ getUbyte(raw[i]);
					}

					buffer = ByteBuffer.allocate(len);
				}
				return getPackage(newData);
			} else
				return null;
		}

		if (buffer == null)
			return null;
		else if (buffer.hasRemaining()) {
			if (newData.hasRemaining()) {
				fillByteBuffer(buffer, newData);

				if (buffer.hasRemaining())
					return null;

				return getPackage(newData);
			} else
				return null;
		} else {
			headSizeBuffer.clear();
			buffer.flip();
			return buffer;
		}
	}

	private void fillByteBuffer(ByteBuffer desc, ByteBuffer src) {
		int count = desc.remaining() > src.remaining() ? src.remaining() : desc
				.remaining();

		for (int i = 0; i < count; i++) {
			desc.put(src.get());
		}
	}

	@Override
	public ByteBuffer pack(ByteBuffer newData) {
		byte[] intLenRaw = ByteBuffer.allocate(4).putInt(newData.remaining())
				.array();
		byte[] relLenRaw = new byte[packagHeadByteSize];

		System.arraycopy(intLenRaw, 4 - packagHeadByteSize, relLenRaw, 0,
				packagHeadByteSize);
		ByteBuffer buffer = ByteBuffer.allocate(packagHeadByteSize
				+ newData.remaining());

		if (!needReverse) {
			for (int i = 0; i < packagHeadByteSize; i++) {
				buffer.put(relLenRaw[i]);
			}
		} else {
			for (int i = packagHeadByteSize; i >= 0; i--) {
				buffer.put(relLenRaw[i]);
			}
		}

		buffer.put(newData);

		buffer.flip();
		return buffer;
	}

	private int getUbyte(byte b)
	{
		return b & 0xFF;
	}
}
