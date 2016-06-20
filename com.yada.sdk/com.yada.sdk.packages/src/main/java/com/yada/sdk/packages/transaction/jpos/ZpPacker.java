package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOException;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class ZpPacker extends JposPacker {
	ZpParser tranIdParser = new ZpParser();

	public ZpPacker() throws ISOException {
		super(0, ZpPacker.class.getResourceAsStream("/8583zp.xml"), "zp");
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
			// posp + [term_id] + [traceNo]
			// 4+15+6
			String termId = message.getFieldString(41);
			String traceNo = message.getFieldString(11);
			return "zp" + termId + traceNo;
		}

	}

	@Override
	public JposMessage createEmpty() {
		JposMessage message = super.createEmpty();
		message.setTranIdParser(tranIdParser);
		return message;
	}

}
