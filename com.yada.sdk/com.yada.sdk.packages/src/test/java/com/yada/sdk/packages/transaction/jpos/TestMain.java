package com.yada.sdk.packages.transaction.jpos;

import java.math.BigInteger;

import com.payneteasy.tlv.*;
import com.yada.sdk.packages.comm.Tlv;

public class TestMain {

	public static void main(String[] args) {
		String strData = "DF2236166259960027562818000594135737120938EFAFDC9721F4B98000000000000000001907050103A02000010A0100000000009FC6F7FADF23020200";
		byte[] bs = HexUtil.parseHex(strData);

		BerTlvParser parser = new BerTlvParser();

		BerTlvs tlvs = parser.parse(bs, 0, bs.length);
		for(BerTlv tlv : tlvs.getList()) {
			System.out.println(tlv);
		}

		strData = "9F26088FB69357986AC9A29F2701809F101307010103A0A002010A010000000000686426CB9F3704EB158C419F36020009950500800488009A031312109C01319F02060000000000005F2A02015682027C009F1A0201569F03060000000000009F3303E0E1C89F34030203009F3501229F1E083330343531333931";
		bs = HexUtil.parseHex(strData);
		tlvs = parser.parse(bs, 0, bs.length);
		for(BerTlv tlv : tlvs.getList()) {
			System.out.println(tlv);
		}

		System.out.println();

		strData = "9F3303E0E1C89505008004E0009F1E0833303435313334359F101307010103A02002010A0100000000004DD6D8869F2608FA81EA9E5CF47D4D9F3602001282027C009C01009F1A0201569A031312109F02060000000060005F2A0201569F03060000000000009F3501229F34030203009F37040FB5FA259F2701809F4104000000018408A0000003330101019F090220E9";
		bs = HexUtil.parseHex(strData);
		tlvs = parser.parse(bs, 0, bs.length);
		for(BerTlv tlv : tlvs.getList()) {
			System.out.println(tlv);
		}

		System.out.println();

		strData = "9F3303E0E1C89505008004E0009F1E0833303435313337359F101307010103A02002010A01000000000076AD7BCD9F2608D1FEAAE5FC45B7A09F3602003282027C009C01009F1A0201569A031312109F02060000000175205F2A0201569F03060000000000009F3501229F34030203009F370441300C889F2701809F4104000000018408A0000003330101019F090220E9";
		bs = HexUtil.parseHex(strData);
		tlvs = parser.parse(bs, 0, bs.length);
		for(BerTlv tlv : tlvs.getList()) {
			System.out.println(tlv);
		}

		System.out.println();

		strData = "DF22381906228480316038946869000486123111120846C728CEA5D1478640020300008004E0001907080103602002010A010000000000A1E91A73DF23020200";
		bs = new BigInteger(strData, 16).toByteArray();
		bs = HexUtil.parseHex(strData);
		tlvs = parser.parse(bs, 0, bs.length);
		for(BerTlv tlv : tlvs.getList()) {
			System.out.println(tlv);
		}

		System.out.println();
	}

}
