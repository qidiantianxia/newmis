package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOException;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class PospPacker extends JposPacker {
	PospTranIdParser tranIdParser = new PospTranIdParser();
	
	public PospPacker(int headLength) throws ISOException
	{
		super(headLength, PospPacker.class.getResourceAsStream("/8583posp.xml"));
	}
	
	@Override
	public IMessage unpack(ByteBuffer byteBuffer) throws PackagingException {
		IMessage message = super.unpack(byteBuffer);
		((JposMessage)message).setTranIdParser(tranIdParser);
		return message;
	}
	
	private class PospTranIdParser implements ITranIdParser
	{

		@Override
		public String getTranId() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getOrgTranId() {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
}
