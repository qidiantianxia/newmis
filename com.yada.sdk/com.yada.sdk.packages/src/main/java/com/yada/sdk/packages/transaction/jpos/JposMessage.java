package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class JposMessage implements IMessage {
	/**
	 * JPOS的包对象
	 */
	private ISOMsg isoMsg;

	public JposMessage() {
		this.isoMsg = new ISOMsg();
	}

	@Override
	public boolean hasField(int fieldId) {
		return this.isoMsg.hasField(fieldId);
	}

	@Override
	public ByteBuffer getField(int fieldId) {
		return ByteBuffer.wrap(isoMsg.getBytes(fieldId));
	}

	@Override
	public String getFieldString(int fieldId) {
		return isoMsg.getString(fieldId);
	}

	@Override
	public void setFieldString(int fieldId, String fieldValue) throws PackagingException {
		try {
			isoMsg.set(fieldId, fieldValue);
		} catch (ISOException e) {
			throw new PackagingException(e);
		}
	}

	@Override
	public void setField(int fieldId, ByteBuffer fieldValue) throws PackagingException {
		byte[] bts = new byte[fieldValue.remaining()];
		fieldValue.get(bts);
		try {
			isoMsg.set(fieldId, bts);
		} catch (ISOException e) {
			throw new PackagingException(e);
		}
	}

	public ISOMsg getIsoMsg() {
		return isoMsg;
	}

	@Override
	public int getFieldMaxLen(int fieldId) {
		ISOBasePackager packer = (ISOBasePackager) isoMsg.getPackager();
		return packer.getFieldPackager(fieldId).getLength();
	}

	@Override
	public ByteBuffer getTpduFromAddress() throws PackagingException {
		byte[] header = isoMsg.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("There is not TPDU header in this message");
		}
		ByteBuffer retBuf = ByteBuffer.allocate(2).put(header[3]).put(header[4]);
		retBuf.flip();
		return retBuf;
	}

	@Override
	public ByteBuffer getTpduToAddress() throws PackagingException {
		byte[] header = isoMsg.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("There is not TPDU header in this message");
		}
		ByteBuffer retBuf = ByteBuffer.allocate(2).put(header[1]).put(header[2]);
		retBuf.flip();
		return retBuf;
	}

	@Override
	public void setTpduFromAddress(ByteBuffer tpduFromAddress) throws PackagingException {
		byte[] header = isoMsg.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("You can`t do that. Because TPDU head is null or its len less than 5");
		}
		if (tpduFromAddress.remaining() != 2) {
			throw new PackagingException("The length of TpduFromAddress must be 2 bytes.");
		}
		header[3] = tpduFromAddress.get();
		header[4] = tpduFromAddress.get();
		isoMsg.setHeader(header);
	}

	@Override
	public void setTpduToAddress(ByteBuffer tpduFromAddress) throws PackagingException {
		byte[] header = isoMsg.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("You can`t do that. Because TPDU head is null or its len less than 5");
		}
		if (tpduFromAddress.remaining() != 2) {
			throw new PackagingException("The length of TpduToAddress must be 2 bytes.");
		}
		header[1] = tpduFromAddress.get();
		header[2] = tpduFromAddress.get();
		isoMsg.setHeader(header);
	}

}