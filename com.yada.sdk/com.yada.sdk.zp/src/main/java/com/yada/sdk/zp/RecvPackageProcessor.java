package com.yada.sdk.zp;

import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

	public RecvPackageProcessor(ConcurrentMap<String, TranContext> map, IPacker packer, IZpSystemConfigService zpSystemConfigService,ITraceNoService traceNoService) {
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
		
		if(tranContext != null)
		{
			synchronized (tranContext) {
				tranContext.respMessage = respMessage;
				tranContext.notify();
			}
		}
	}

}
