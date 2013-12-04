package com.yada.sdk.net;


public class FixLenPackageSplitterFactory implements IPackageSplitterFactory {

	private int packagHeadByteSize;
	private boolean needReverse;
	public FixLenPackageSplitterFactory(int packagHeadByteSize, boolean needReverse)
	{
		this.packagHeadByteSize = packagHeadByteSize;
		this.needReverse = needReverse;
	}
	
	@Override
	public IPackageSplitter create() {
		return new FixLenPackageSplitter(packagHeadByteSize, needReverse);
	}

	
}
