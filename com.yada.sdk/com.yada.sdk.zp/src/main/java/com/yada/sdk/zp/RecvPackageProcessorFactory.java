package com.yada.sdk.zp;

import java.util.concurrent.ConcurrentMap;

import com.yada.sdk.net.IPackageProcessor;
import com.yada.sdk.net.IPackageProcessorFactory;
import com.yada.sdk.packages.transaction.IPacker;

class RecvPackageProcessorFactory implements IPackageProcessorFactory {

	private ConcurrentMap<String, TranContext> map;
	private IPacker packer;
	private IZpSystemConfigService zpSystemConfigService;
	private IZpkChangeNotify zpkChangedNotify;

	public RecvPackageProcessorFactory(ConcurrentMap<String, TranContext> map,
			IPacker packer, IZpSystemConfigService zpSystemConfigService,
			IZpkChangeNotify zpkChangedNotify) {
		this.map = map;
		this.packer = packer;
		this.zpSystemConfigService = zpSystemConfigService;
		this.zpkChangedNotify = zpkChangedNotify;
	}

	@Override
	public IPackageProcessor create() {
		return new RecvPackageProcessor(map, packer, zpSystemConfigService, zpkChangedNotify);
	}

}
