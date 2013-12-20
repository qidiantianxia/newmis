package com.yada.sdk.packages.transaction.jpos;

import java.nio.ByteBuffer;

import org.jpos.iso.ISOException;

import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

/**
 * 一个可定制的packer。
 * 
 * @author jiangfengming
 * 
 * @param <T>
 */
public abstract class CustomPacker<T extends JposMessage> extends JposPacker {
	CustomTranIdParser tranIdParser = new CustomTranIdParser();

	/**
	 * 
	 * @param headLength
	 *            头长度
	 * @param path
	 *            文件路径
	 * @throws ISOException
	 */
	public CustomPacker(int headLength, String path) throws ISOException {
		super(headLength, CustomPacker.class.getResourceAsStream(path), "");
	}

	/**
	 * 
	 * @param headLength
	 *            头长度
	 * @param path
	 *            文件路径
	 * @param realmName
	 *            解析8583时日志中的realm名称
	 * @throws ISOException
	 */
	public CustomPacker(int headLength, String path, String realmName) throws ISOException {
		super(headLength, CustomPacker.class.getResourceAsStream(path), realmName);
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

	/**
	 * 构建一个T类型的message。用于储存定制信息。
	 * 
	 * @return
	 */
	protected abstract T generateJposMessage();

	/**
	 * 生成一个交易唯一标志的方法
	 * 
	 * @param message
	 * @return
	 */
	protected abstract String generateTranId(JposMessage message);

	private class CustomTranIdParser implements ITranIdParser {

		@Override
		public String getTranId(JposMessage message) {
			return generateTranId(message);
		}
	}

}
