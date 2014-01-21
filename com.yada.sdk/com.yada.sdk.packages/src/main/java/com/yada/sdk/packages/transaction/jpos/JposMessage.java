package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOBasePackager;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class JposMessage extends ISOMsg implements IMessage {

	private ITranIdParser tranIdParser;
	
	void setTranIdParser(ITranIdParser tranIdParser)
	{
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
	public ByteBuffer getTpduFromAddress() throws PackagingException {
		byte[] header = this.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("There is not TPDU header in this message");
		}
		ByteBuffer retBuf = ByteBuffer.allocate(2).put(header[3]).put(header[4]);
		retBuf.flip();
		return retBuf;
	}

	@Override
	public ByteBuffer getTpduToAddress() throws PackagingException {
		byte[] header = this.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("There is not TPDU header in this message");
		}
		ByteBuffer retBuf = ByteBuffer.allocate(2).put(header[1]).put(header[2]);
		retBuf.flip();
		return retBuf;
	}

	@Override
	public void setTpduFromAddress(ByteBuffer tpduFromAddress) throws PackagingException {
		byte[] header = this.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("You can`t do that. Because TPDU head is null or its len less than 5");
		}
		if (tpduFromAddress.remaining() != 2) {
			throw new PackagingException("The length of TpduFromAddress must be 2 bytes.");
		}
		header[3] = tpduFromAddress.get();
		header[4] = tpduFromAddress.get();
		this.setHeader(header);
	}

	@Override
	public void setTpduToAddress(ByteBuffer tpduFromAddress) throws PackagingException {
		byte[] header = this.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("You can`t do that. Because TPDU head is null or its len less than 5");
		}
		if (tpduFromAddress.remaining() != 2) {
			throw new PackagingException("The length of TpduToAddress must be 2 bytes.");
		}
		header[1] = tpduFromAddress.get();
		header[2] = tpduFromAddress.get();
		this.setHeader(header);
	}

	@Override
	public void setTpduId(ByteBuffer tpduId) throws PackagingException {
		byte[] header = this.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("You can`t do that. Because TPDU head is null or its len less than 5");
		}
		if (tpduId.remaining() != 1) {
			throw new PackagingException("The length of tpduId must be 1 bytes.");
		}
		header[0] = tpduId.get();
		
		this.setHeader(header);
	}

	@Override
	public void setVersion(ByteBuffer version) throws PackagingException {
		byte[] header = this.getHeader();
		if (header == null || header.length < 5) {
			throw new PackagingException("You can`t do that. Because TPDU head is null or its len less than 5");
		}
		if (version.remaining() != 1) {
			throw new PackagingException("The length of tpduId must be 1 bytes.");
		}
		
		header[5] = version.get();
		header[6] = version.get();
		this.setHeader(header);
	}
	

}