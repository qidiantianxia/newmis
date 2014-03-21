package com.yada.sdk.zp;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yada.sdk.net.DataTransceivers;
import com.yada.sdk.net.DataTransceiversContext;
import com.yada.sdk.net.IPackageProcessor;
import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;
import com.yada.sdk.packages.transaction.IPacker;

class RecvPackageProcessor implements IPackageProcessor {
	private final static Logger logger = LoggerFactory.getLogger(RecvPackageProcessor.class);
	private IPacker packer;
	private IZpSystemConfigService zpSystemConfigService;
	private ITraceNoService traceNoService;
	private ConcurrentMap<String, TranContext> map;

	public RecvPackageProcessor(ConcurrentMap<String, TranContext> map, IPacker packer, IZpSystemConfigService zpSystemConfigService,
			ITraceNoService traceNoService) {
		this.packer = packer;
		this.zpSystemConfigService = zpSystemConfigService;
		this.traceNoService = traceNoService;
		this.map = map;
	}

	@Override
	public void proc(DataTransceiversContext context) {
		IMessage respMessage;
		try {
			respMessage = packer.unpack(context.getData());
		} catch (PackagingException e) {
			logger.error("解包错误", e);
			return;
		}

		String key = respMessage.getTranId();
		TranContext tranContext = map.get(key);

		if (tranContext != null) {
			synchronized (tranContext) {
				tranContext.respMessage = respMessage;
				tranContext.notify();
			}
		} else {
			procNotMatchMessage(respMessage, context);
		}
	}

	protected void procNotMatchMessage(IMessage respMessage, DataTransceiversContext context) {
		String mti = respMessage.getFieldString(0);
		switch (mti) {
		case "0800":
			// 网络管理类信息
			procNetManagementMessage(respMessage, context);
			break;
		default:
			logger.warn("未匹配，并且非网络管理请求类报文，无法处理，该报文为【{}】", respMessage.toString());
			break;
		}
	}

	/**
	 * 处理网络管理类交易
	 * 
	 * @param respMessage
	 */
	protected void procNetManagementMessage(IMessage respMessage, DataTransceiversContext context) {
		// 必须要IST的请求。
		String field70 = respMessage.getFieldString(70);
		switch (field70) {
		case "001":// 签到
			logger.info("接收到签到请求，但是无特殊处理。");
			break;

		case "002":// 签退
			logger.info("接收到签退请求，但是无特殊处理。");
			break;
		case "101":// 密钥交换
			String field48 = respMessage.getFieldString(48);
			// 根据用法3，采用双倍密钥长
			String keyType = field48.substring(1, 2);
			if (!"1".equals(keyType)) {
				// 不支持的用法
				logger.error("不支持的密钥交换类型,48域信息为【{}】", field48);
				return;
			}
			zpSystemConfigService.savePinKey(field48.substring(2, 34));
			break;
		case "301":// 网络测试
			logger.info("接收到网络测试请求，但是无特殊处理。");
			break;
		}

	}

	protected void netManagementResponse(IMessage respMessage, DataTransceiversContext context) {
		DataTransceivers transceivers = context.getDataTransceivers();
		if (!transceivers.isValid()) {
			logger.warn("数据收发器关闭，网络管理类报文无法响应");
		}
		try {
			IMessage responseMessage = generateNetManagementMessage(respMessage);
			ByteBuffer byteBuffer = packer.pack(responseMessage);
			transceivers.send(byteBuffer);
			logger.info("网络管理类交易发送完毕");
		} catch (InterruptedException e) {
			logger.error("网络管理类交易发送失败", e);
		} catch (PackagingException e) {
			logger.error("网络管理类交易打包失败", e);
		}
	}

	protected IMessage generateNetManagementMessage(IMessage receMessage) throws PackagingException {
		String mti = "0810";
		String field7 = receMessage.getFieldString(7);
		String field11 = receMessage.getFieldString(11);
		String field33 = "";// TODO acqOrgId
		String field39 = "00";
		String field70 = receMessage.getFieldString(70);// 密钥交换请求
		String field100 = receMessage.getFieldString(100);
		IMessage responseMessage = packer.createEmpty();
		responseMessage.setFieldString(0, mti);
		responseMessage.setFieldString(7, field7);
		responseMessage.setFieldString(11, field11);
		responseMessage.setFieldString(33, field33);
		responseMessage.setFieldString(39, field39);
		responseMessage.setFieldString(70, field70);
		responseMessage.setFieldString(100, field100);
		return responseMessage;
	}

}
