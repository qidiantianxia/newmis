package com.yada.sdk.packages.comm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Tlv {
	public static final Charset GBK = Charset.forName("GBK");
	public static final Charset UTF8 = Charset.forName("UTF-8");
	public static final byte CONSTRUCTED_BIT = 0x20;
	public static final byte MULTIPLE_BYTES_FLAG = 0x1F;
	public static final byte HAS_ANOTHER_BYTE = (byte) 0x80;
	private static final byte[] ROOT_TAG = { (byte) 0xFF };

	private static final Tlv[] emptyTlvArray = new Tlv[] {};

	private byte[] tag;
	private byte[] value;
	private List<Tlv> children = new LinkedList<>();

	public Tlv() {
		this.tag = ROOT_TAG;
	}

	public Tlv(byte[] rawByteArray) {
		this(rawByteArray, 0, rawByteArray.length, ROOT_TAG);
	}

	public Tlv(byte[] rawByteArray, int index, int len, byte[] tag) {
		this.tag = tag;

		if ((tag[0] & Tlv.CONSTRUCTED_BIT) != 0) {
			try {
				parseChildren(rawByteArray, index, len);
			} catch (IOException e) {
				value = Arrays.copyOfRange(rawByteArray, index, index + len);
			}
		} else {
			value = Arrays.copyOfRange(rawByteArray, index, index + len);
		}
	}

	private void parseChildren(byte[] rawByteArray, int index, int len)
			throws IOException {
		int endIndex = index + len;

		while (index < endIndex) {

			ByteArrayOutputStream tagOutputStream = new ByteArrayOutputStream();
			try {
				byte tt = rawByteArray[index++];

				if ((tt & Tlv.MULTIPLE_BYTES_FLAG) == Tlv.MULTIPLE_BYTES_FLAG) {
					tagOutputStream.write(tt);

					while (((tt = rawByteArray[index++]) & HAS_ANOTHER_BYTE) != 0) {
						tagOutputStream.write(tt);
					}
				}

				tagOutputStream.write(tt);

				byte[] tag = tagOutputStream.toByteArray();

				byte tl = rawByteArray[index++];
				int length = 0;

				if (getUnsignedValue(tl) >= 0x80) {
					int count = tl & 0x7F;

					for (int i = 0; i < count; i++) {
						length <<= 8;
						length += getUnsignedValue(rawByteArray[index++]);
					}
				} else {
					length = getUnsignedValue(tl);
				}

				Tlv cTlv = new Tlv(rawByteArray, index, length, tag);
				children.add(cTlv);
				index += length;
			} finally {
				tagOutputStream.close();
			}
		}
	}

	private int getUnsignedValue(byte b) {
		return b & 0xff;
	}

	public byte[] getTag() {
		return tag;
	}

	public void setTag(byte[] tag) {
		this.tag = tag;
	}

	public byte[] getValue() {
		return value;
	}

	public void setValue(byte[] value) {
		this.value = value;
	}

	public String getStringValue() {
		return new String(value, Tlv.GBK);
	}

	public void setStringValue(String strValue) {
		value = strValue.getBytes(Tlv.GBK);
	}

	public String getStringValue(Charset encoding) {
		return new String(value, encoding);
	}

	public void setStringValue(String strValue, Charset encoding) {
		value = strValue.getBytes(encoding);
	}

	public Tlv[] getChildren() {
		return children.toArray(Tlv.emptyTlvArray);
	}

	public void addChild(Tlv child) {
		children.add(child);
	}

	public void addChildren(Tlv[] children) {
		this.children.addAll(Arrays.asList(children));
	}

	public byte[] getRawByteArray() throws IOException {
		ByteArrayOutputStream bufferOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream valueBufferOutputStream = new ByteArrayOutputStream();
		ByteArrayOutputStream lenBufferOutputStream = new ByteArrayOutputStream();

		try {

			if (tag == ROOT_TAG) {
				for (Tlv child : children) {
					valueBufferOutputStream.write(child.getRawByteArray());
				}
			} else {
				bufferOutputStream.write(tag);

				if (value == null) {
					for (Tlv child : children) {
						valueBufferOutputStream.write(child.getRawByteArray());
					}
				} else {
					valueBufferOutputStream.write(value);
				}

				int size = valueBufferOutputStream.size();

				if (size < 0x80) {
					lenBufferOutputStream.write(size);
				} else {

					int count = (int) (Math.log(size) / Math.log(0x100)) + 1;

					lenBufferOutputStream.write(0x80 & count);
					lenBufferOutputStream.write(
							ByteBuffer.allocate(4).putInt(size).array(),
							4 - count, count);
				}
			}

			bufferOutputStream.write(lenBufferOutputStream.toByteArray());
			bufferOutputStream.write(valueBufferOutputStream.toByteArray());
			return bufferOutputStream.toByteArray();
		} catch (IOException e) {
			throw e;
		} finally {
			bufferOutputStream.close();
			lenBufferOutputStream.close();
			valueBufferOutputStream.close();
		}
	}
	
	@Override
	public String toString() {
		return toString("");
	}
	
	private String toString(String prefix)
	{
		StringBuilder sb = new StringBuilder();
		
		if(value == null)
		{
			for(Tlv t : children)
			{
				sb.append(t.toString(prefix + "\t"));
			}
		}
		else
		{
			sb.append(prefix + "tag = ");
			
			for(byte b : tag)
			{
				sb.append(String.format("%02X", b));
			}
			
			sb.append(" length = ");
			sb.append(value.length);
			
			sb.append(" value = ");
			for(byte b : value)
			{
				sb.append(String.format("%02X", b));
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
