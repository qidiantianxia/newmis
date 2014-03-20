package com.yada.sdk.zp;

import java.net.InetSocketAddress;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.encryption.hsm.EncryptionMachine;
import com.yada.sdk.net.AsyncTcpClient;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.net.IPackageProcessorFactory;
import com.yada.sdk.net.IPackageSplitterFactory;
import com.yada.sdk.packages.transaction.IMessage;
import com.yada.sdk.packages.transaction.jpos.ZpPacker;

public abstract class ZpClient {
	private AsyncTcpClient client;
	private ZpPacker packer;
	private EncryptionMachine encryption;
	private IZpSystemConfigService zpSystemConfigService;

	public ZpClient(String zpServerIp, int zpServerPort, String encryptionIp,
			int encryptionPort, IZpSystemConfigService zpSystemConfigService,
			ITraceNoService traceNoService, int timeout) {
		int zpHeadLength = 4;
		IPackageSplitterFactory packageSplitterFactory = new FixLenPackageSplitterFactory(
				zpHeadLength, false);
		IPackageProcessorFactory packageProcessorFactory = new RecvPackageProcessorFactory();
		this.client = new AsyncTcpClient(new InetSocketAddress(zpServerIp,
				zpServerPort), packageSplitterFactory, packageProcessorFactory,
				timeout, true);
		try {
			this.packer = new ZpPacker(0);
		} catch (ISOException e) {
			throw new RuntimeException(e);
		}
		this.zpSystemConfigService = zpSystemConfigService;
		this.encryption = new EncryptionMachine(encryptionIp, encryptionPort,
				zpSystemConfigService.getLmkZmk());
	}

	protected EncryptionMachine getEncryption() {
		return encryption;
	}

	protected IZpSystemConfigService getEncryptionKeyService() {
		return zpSystemConfigService;
	}

	protected ZpPacker getPacker() {
		return packer;
	}

	protected AsyncTcpClient getClient() {
		return client;
	}

	protected IMessage Tran(IMessage pkg) {
		return null;
	}
}
