package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOException;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class EndPointPacker extends JposPacker {
	EndPointTranIdParser tranIdParser = new EndPointTranIdParser();

	public EndPointPacker(int headLength) throws ISOException {
		super(headLength, EndPointPacker.class.getResourceAsStream("/8583EndPoint.xml"));
	}

	@Override
	public IMessage unpack(ByteBuffer byteBuffer) throws PackagingException {
		IMessage message = super.unpack(byteBuffer);
		((JposMessage) message).setTranIdParser(tranIdParser);
		return message;
	}

	private class EndPointTranIdParser implements ITranIdParser {

		@Override
		public String getTranId(JposMessage message) {
			// EndPoint + termId + tranDate + traceNo + mti 
			StringBuilder sb = new StringBuilder();
			String termId = message.getFieldString(41);
			String tranDate = message.getFieldString(13);
			String traceNo = message.getFieldString(11);
			String mti = message.getFieldString(0);
			return sb.append("EndPoint").append(termId).append(tranDate).append(traceNo).append(mti).toString();
		}

	}
}
