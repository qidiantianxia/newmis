package com.yada.sdk.packages.transaction.jpos;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOPackager;
import org.jpos.iso.packager.GenericPackager;
import org.jpos.util.SimpleLogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * 日志
	 */
	private Logger logger;

	/**
	 * 配置文件名称
	 */
	private String fileName;

	/**
	 * JPOS提供的标准的8583解析器
	 */
	private GenericPackager packer = null;

	/**
	 * 
	 * @param headLength
	 *            头长度
	 * @param filePath
	 *            配置文件路径
	 */
	public JposPacker(int headLength, String filePath) {
		try {
			Path path = Paths.get(filePath);
			instance(headLength, Files.newInputStream(path, StandardOpenOption.READ));
			fileName = path.getFileName().toString();
			logger = LoggerFactory.getLogger(JposPacker.class);
		} catch (IOException e) {
			throw new RuntimeException("JposPacker initialize failed.", e);
		}
	}

	/**
	 * 
	 * @param headLength
	 *            头长度
	 * @param inputStream
	 *            配置文件输入流
	 */
	public JposPacker(int headLength, InputStream inputStream) {
		instance(headLength, inputStream);
	}

	/**
	 * 通过流初始化
	 * 
	 * @param inputStream
	 */
	private void instance(int headLength, InputStream inputStream) {
		try {
			packer = new GenericPackager(inputStream);
			packer.setHeaderLength(headLength);
			if (logger != null) {
				if (logger.isDebugEnabled()) {
					// DEBUG级别
					org.jpos.util.Logger l = new org.jpos.util.Logger();
					l.addListener(new JposLogDebugListener(logger));
					packer.setLogger(l, fileName);
				} else {
					// ERROR级别
					org.jpos.util.Logger l = new org.jpos.util.Logger();
					l.addListener(new JposLogErrorListener(logger));
					packer.setLogger(l, fileName);
				}
			} else {
				org.jpos.util.Logger l = new org.jpos.util.Logger();
				l.addListener(new SimpleLogListener());
				packer.setLogger(l, "Test");
			}
		} catch (ISOException e) {
			throw new RuntimeException("JposPacker initialize failed.", e);
		}
	}

	@Override
	public ByteBuffer pack(IMessage message) throws PackagingException {
		try {
			JposMessage msg = (JposMessage) message;
			ISOPackager tempPacker = msg.getIsoMsg().getPackager();
			if (tempPacker == null) {
				// 如果为null，则为手工创建的message，设置默认的packer。
				msg.getIsoMsg().setPackager(packer);
			}
			return ByteBuffer.wrap(msg.getIsoMsg().pack());
		} catch (Exception e) {
			throw new PackagingException(e);
		}
	}

	@Override
	public IMessage unpack(ByteBuffer byteBuffer) throws PackagingException {
		try {
			byte[] bts = new byte[byteBuffer.remaining()];
			byteBuffer.get(bts);
			JposMessage message = new JposMessage();
			// packer.unpack(message.getIsoMsg(), bts);
			message.getIsoMsg().setPackager(packer);
			message.getIsoMsg().unpack(bts);
			return message;
		} catch (Exception e) {
			throw new PackagingException(e);
		}
	}
}