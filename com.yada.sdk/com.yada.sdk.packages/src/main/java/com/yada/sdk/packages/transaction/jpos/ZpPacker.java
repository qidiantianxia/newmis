package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOException;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class ZpPacker extends JposPacker {
	ZpParser tranIdParser = new ZpParser();

	public ZpPacker(int headLength) throws ISOException {
		super(headLength, ZpPacker.class.getResourceAsStream("/8583zp.xml"), "zp");
	}

	@Override
	public IMessage unpack(ByteBuffer byteBuffer) throws PackagingException {
		IMessage message = super.unpack(byteBuffer);
		((JposMessage) message).setTranIdParser(tranIdParser);
		return message;
	}

	private class ZpParser implements ITranIdParser {

		@Override
		public String getTranId(JposMessage message) {
			// posp + [terminal id] + [批次号] + [trance no] + [mti]
			// 4+8+6+6+4=28
			StringBuilder sb = new StringBuilder();
			String termId = message.getFieldString(41);
			String field61 = message.getFieldString(61);
			String batchNo = field61.substring(0, 6);
			String traceNo = message.getFieldString(11);
			String mti = message.getFieldString(0);
			return sb.append("posp").append(termId).append(batchNo).append(traceNo).append(mti).toString();
		}

	}
}
