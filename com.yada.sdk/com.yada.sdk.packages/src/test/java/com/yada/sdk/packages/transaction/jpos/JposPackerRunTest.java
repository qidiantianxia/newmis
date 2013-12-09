package com.yada.sdk.packages.transaction.jpos;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jpos.iso.ISOException;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class JposPackerRunTest {

	public static void main(String[] args) throws ISOException {
		InputStream is = JposPackerRunTest.class.getResourceAsStream(JposPackerRunTest.class.getSimpleName() + ".xml");
		JposPacker packer = new JposPacker(4, is);
		String receMsg = "00 00 00 D6 30 31 31 30 37 32 33 41 38 30 38 31 38 45 43 30 38 30 31 30 31 36 35 31 34 39 35 38 37 37 30 37 33 35 36 38 34 33 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 31 32 33 30 37 31 39 31 30 35 31 30 30 31 32 33 34 32 31 31 30 35 31 30 30 30 37 31 39 30 37 31 39 30 37 31 39 30 38 30 38 30 31 30 34 31 31 30 30 30 36 31 38 33 30 30 31 33 31 39 39 31 30 31 32 33 34 32 31 37 38 35 37 38 37 30 30 36 35 30 31 30 30 32 34 31 30 34 36 35 30 30 35 33 31 31 30 30 31 31 31 35 36 30 35 35 50 44 30 35 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30";
		receMsg = receMsg.replaceAll(" ", "");
//		receMsg = receMsg.substring(8);
		try {
			IMessage message = packer.unpack(ByteBuffer.wrap(hexDecode(receMsg)));
			Charset charset = Charset.forName("ASCII");
			message.setField(49, ByteBuffer.wrap("157".getBytes()));
			packer.pack(message);
			System.out.println(charset.decode(message.getField(49)).toString());
		} catch (PackagingException e) {
			e.printStackTrace();
		}
	}
	
	public static byte[] hexDecode(String hex) {
		// A null string returns an empty array
		if (hex == null || hex.length() == 0) {
			return new byte[0];
		} else if (hex.length() < 3) {
			return new byte[] { (byte) (Integer.parseInt(hex, 16) & 0xff) };
		}
		// Adjust accordingly for odd-length strings
		int count = hex.length();
		int nibble = 0;
		if (count % 2 != 0) {
			count++;
			nibble = 1;
		}
		byte[] buf = new byte[count / 2];
		char c = 0;
		int holder = 0;
		int pos = 0;
		for (int i = 0; i < buf.length; i++) {
			for (int z = 0; z < 2 && pos < hex.length(); z++) {
				c = hex.charAt(pos++);
				if (c >= 'A' && c <= 'F') {
					c -= 55;
				} else if (c >= '0' && c <= '9') {
					c -= 48;
				} else if (c >= 'a' && c <= 'f') {
					c -= 87;
				}
				if (nibble == 0) {
					holder = c << 4;
				} else {
					holder |= c;
					buf[i] = (byte) holder;
				}
				nibble = 1 - nibble;
			}
		}
		return buf;
	}
}
