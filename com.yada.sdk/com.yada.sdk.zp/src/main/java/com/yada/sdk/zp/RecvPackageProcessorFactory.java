package com.yada.sdk.zp;

import java.util.concurrent.ConcurrentMap;

import com.yada.sdk.net.IPackageProcessor;
import com.yada.sdk.net.IPackageProcessorFactory;
import com.yada.sdk.packages.transaction.IPacker;

class RecvPackageProcessorFactory implements IPackageProcessorFactory {

	private ConcurrentMap<String, TranContext> map;
	private IPacker packer;
	private IZpSystemConfigService zpSystemConfigService;

	public RecvPackageProcessorFactory(ConcurrentMap<String, TranContext> map,
			IPacker packer, IZpSystemConfigService zpSystemConfigService) {
		this.map = map;
		this.packer = packer;
		this.zpSystemConfigService = zpSystemConfigService;
	}

	@Override
	public IPackageProcessor create() {
		return new RecvPackageProcessor(map, packer, zpSystemConfigService);
	}

}
