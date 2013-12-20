package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOException;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public abstract class CustomPacker<T extends JposMessage> extends JposPacker {
	CustomTranIdParser tranIdParser = new CustomTranIdParser();

	public CustomPacker(int headLength, String path) throws ISOException {
		super(headLength, CustomPacker.class.getResourceAsStream(path));
	}

	@Override
	public IMessage unpack(ByteBuffer byteBuffer) throws PackagingException {
		IMessage message = super.unpack(byteBuffer);
		((JposMessage) message).setTranIdParser(tranIdParser);
		return message;
	}

	@Override
	protected JposMessage newJposMessage() {
		return generateJposMessage();
	}

	protected abstract T generateJposMessage();

	protected abstract String generateTranId(JposMessage message);

	private class CustomTranIdParser implements ITranIdParser {

		@Override
		public String getTranId(JposMessage message) {
			return generateTranId(message);
			// EndPoint + termId + tranDate + traceNo + mti
//			StringBuilder sb = new StringBuilder();
//			String termId = message.getFieldString(41);
//			String tranDate = message.getFieldString(13);
//			String traceNo = message.getFieldString(11);
//			String mti = message.getFieldString(0);
//			return sb.append("EndPoint").append(termId).append(tranDate).append(traceNo).append(mti).toString();
		}
	}

}
