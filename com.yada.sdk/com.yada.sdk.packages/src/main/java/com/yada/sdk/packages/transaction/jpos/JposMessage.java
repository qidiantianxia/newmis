package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class JposMessage extends ISOMsg implements IMessage {

	private ITranIdParser tranIdParser;

	public JposMessage() {
		super();
	}

	void setTranIdParser(ITranIdParser tranIdParser) {
		this.tranIdParser = tranIdParser;
	}

	@Override
	public ByteBuffer getField(int fieldId) {
		return ByteBuffer.wrap(this.getBytes(fieldId));
	}

	@Override
	public String getFieldString(int fieldId) {
		return this.getString(fieldId);
	}

	@Override
	public void setFieldString(int fieldId, String fieldValue) throws PackagingException {
		try {
			this.set(fieldId, fieldValue);
		} catch (ISOException e) {
			throw new PackagingException(e);
		}
	}

	@Override
	public void setField(int fieldId, ByteBuffer fieldValue) throws PackagingException {
		byte[] bts = new byte[fieldValue.remaining()];
		fieldValue.get(bts);
		try {
			this.set(fieldId, bts);
		} catch (ISOException e) {
			throw new PackagingException(e);
		}
	}

	@Override
	public int getFieldMaxLen(int fieldId) {
		ISOBasePackager packer = (ISOBasePackager) this.getPackager();
		return packer.getFieldPackager(fieldId).getLength();
	}

	@Override
	public String getTranId() {
		return tranIdParser == null ? null : tranIdParser.getTranId(this);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(System.getProperty("line.separator"));
		for (int i = 0; i < 129; i++) {
			if (i == 1) {
				// 位图
				continue;
			}
			String value = this.getFieldString(i);
			if (value == null) {
				continue;
			}
			sb.append(String.format("Field[%03d]value[%s]%n", i, value));
		}
		return sb.toString();
	}

}