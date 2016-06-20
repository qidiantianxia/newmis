package com.yada.sdk.packages.transaction.jpos;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;
import com.yada.sdk.packages.transaction.IPacker;

/**
 * 通过JPOS实现的8583包的解析器
 * 
 * @author jiangfengming
 * 
 */
public class JposPacker implements IPacker {

	/**
	 * JPOS提供的标准的8583解析器
	 */
	private GenericPackager packer = null;

	/**
	 * 头长度
	 */
	private int headLength;
	/**
	 * 
	 * @param headLength
	 *            头长度
	 * @param filePath
	 *            配置文件路径
	 * @throws IOException
	 * @throws ISOException
	 */
	public JposPacker(int headLength, String filePath) throws ISOException, IOException {
		this(headLength, Files.newInputStream(Paths.get(filePath), StandardOpenOption.READ), "");
	}

	/**
	 * 
	 * @param headLength
	 *            头长度
	 * @param inputStream
	 *            配置文件输入流
	 * @param realmName
	 *            解析8583日志中的realm的名称
	 * @throws ISOException
	 */
	public JposPacker(int headLength, InputStream inputStream, String realmName) throws ISOException {
		this.headLength = headLength;
		packer = new GenericPackager(inputStream);
		packer.setHeaderLength(headLength);
		org.jpos.util.Logger l = new org.jpos.util.Logger();
		l.addListener(new JposLogListener());
		packer.setLogger(l, realmName);
	}

	@Override
	public ByteBuffer pack(IMessage message) throws PackagingException {
		try {
			JposMessage msg = (JposMessage) message;
			return ByteBuffer.wrap(msg.pack());
		} catch (Exception e) {
			throw new PackagingException(e);
		}
	}

	@Override
	public IMessage unpack(ByteBuffer byteBuffer) throws PackagingException {
		try {
			byte[] bts = new byte[byteBuffer.remaining()];
			byteBuffer.get(bts);
			JposMessage message = createEmpty();
			message.unpack(bts);
			return message;
		} catch (Exception e) {
			throw new PackagingException(e);
		}
	}

	@Override
	public JposMessage createEmpty() {
		JposMessage message = new JposMessage();
		message.setPackager(packer);
		return message;
	}
}