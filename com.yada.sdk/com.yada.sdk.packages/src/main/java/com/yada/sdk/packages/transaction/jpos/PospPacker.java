package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOException;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class PospPacker extends JposPacker {
	PospTranIdParser tranIdParser = new PospTranIdParser();

	public PospPacker(int headLength) throws ISOException {
		super(headLength, PospPacker.class.getResourceAsStream("/8583posp.xml"));
	}

	@Override
	public IMessage unpack(ByteBuffer byteBuffer) throws PackagingException {
		IMessage message = super.unpack(byteBuffer);
		((JposMessage) message).setTranIdParser(tranIdParser);
		return message;
	}

	private class PospTranIdParser implements ITranIdParser {

		@Override
		public String getTranId(JposMessage message) {
			String termId = message.getFieldString(42);
			String tranDate = message.getFieldString(13);
			if (tranDate == null) {
				tranDate = "";
			}
			String traceNo = message.getFieldString(11);
			// 返回日期+终端号+跟踪号
			return new StringBuilder().append(tranDate).append(termId).append(traceNo).toString();
		}

	}
}
