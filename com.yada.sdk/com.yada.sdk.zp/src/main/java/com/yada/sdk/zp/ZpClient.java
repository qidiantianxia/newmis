package com.yada.sdk.zp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Calendar;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

import org.jpos.iso.ISOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yada.sdk.device.encryption.hsm.EncryptionMachine;
import com.yada.sdk.net.AsyncTcpClient;
import com.yada.sdk.net.FixLenPackageSplitterFactory;
import com.yada.sdk.net.IPackageProcessorFactory;
import com.yada.sdk.net.IPackageSplitterFactory;
import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;
import com.yada.sdk.packages.transaction.jpos.ZpPacker;

public class ZpClient implements IZpkChangeNotify {
	private final static Logger logger = LoggerFactory.getLogger(ZpClient.class);
	private AsyncTcpClient client;
	private ZpPacker packer;
	private EncryptionMachine encryption;
	private ConcurrentMap<String, TranContext> map;
	private int timeout;
	private ExecutorService reversalPool;
	private ITraceNoService traceNoService;
	private String lmkZpk;
	private String lmkZmk;
	private IZpSystemConfigService zpSystemConfigService;

	public ZpClient(String zpServerIp, int zpServerPort, ITraceNoService traceNoService, int timeout) {
		this(zpServerIp, zpServerPort, null, 0, null, traceNoService, timeout);
	}

	public ZpClient(String zpServerIp, int zpServerPort, String encryptionIp, int encryptionPort, IZpSystemConfigService zpSystemConfigService,
			ITraceNoService traceNoService, int timeout) {
		this.timeout = timeout;
		int zpHeadLength = 4;
		this.map = new ConcurrentSkipListMap<String, TranContext>();
		this.reversalPool = Executors.newFixedThreadPool(10);
		this.traceNoService = traceNoService;
		this.zpSystemConfigService = zpSystemConfigService;
		IPackageSplitterFactory packageSplitterFactory = new FixLenPackageSplitterFactory(zpHeadLength, false);
		IPackageProcessorFactory packageProcessorFactory = new RecvPackageProcessorFactory(map, packer, zpSystemConfigService, this);

		try {
			this.packer = new ZpPacker(0);
		} catch (ISOException e) {
			throw new RuntimeException(e);
		}

		if (encryptionIp != null && encryptionPort != 0 && zpSystemConfigService != null) {
			this.lmkZmk = zpSystemConfigService.getLmkZmk();
			String zmkZpk = zpSystemConfigService.getPinKey();
			initEncryption(encryptionIp, encryptionPort, zmkZpk);
		}

		this.client = new AsyncTcpClient(new InetSocketAddress(zpServerIp, zpServerPort), packageSplitterFactory, packageProcessorFactory, timeout, true);
	}

	private void initEncryption(String encryptionIp, int encryptionPort, String zmkZpk) {
		if (zmkZpk != null) {
			this.encryption = new EncryptionMachine(encryptionIp, encryptionPort, lmkZmk);

			changeZpk(zmkZpk);
		}
	}

	public void setEncryptionPin(String accountNo, String pin, IMessage message) {
		String lmkPin = encryption.getTpkPin(accountNo, pin, lmkZpk);

		try {
			message.setFieldString(52, lmkPin);
		} catch (PackagingException e) {
			logger.debug("设置11域错误", e);
			throw new RuntimeException(e);
		}
	}

	public void setTraceNo(IMessage message) {
		String terminalId = message.getFieldString(41);
		try {
			message.setFieldString(11, traceNoService.getTraceNo(terminalId));
		} catch (PackagingException e) {
			logger.debug("设置11域错误", e);
			throw new RuntimeException(e);
		}
	}

	public IMessage Tran(IMessage pkg) throws InterruptedException, PackagingException, TimeoutException {
		TranContext tranContext = new TranContext();
		tranContext.reqMessage = pkg;
		String key = pkg.getTranId();
		map.put(key, tranContext);
		ByteBuffer rawBuffer;
		rawBuffer = packer.pack(pkg);
		client.send(rawBuffer);

		synchronized (tranContext) {
			tranContext.wait(timeout);

			if (Calendar.getInstance().getTimeInMillis() - tranContext.createDateTime >= timeout)
				throw new TimeoutException("交易超时");
		}

		map.remove(key);
		return tranContext.respMessage;
	}

	public void Reversal(IMessage tranPkg) {
		Calendar calendar = Calendar.getInstance();
		final IMessage reversalMsg = packer.createEmpty();
		// 消息头
		String mti = "0420";
		// 02账号
		String field02 = tranPkg.getFieldString(2);
		// 03 处理码
		String field03 = tranPkg.getFieldString(3);
		// 04交易金额
		String field04 = tranPkg.getFieldString(4);
		// 07交易传输日期时间
		String field07 = MessageUtil.getField07(calendar);// 交易传送日期和时间MMddHHmmss
		// 41 终端号
		String field41 = tranPkg.getFieldString(41);
		// 12交易发生时间
		String field12 = tranPkg.getFieldString(12);
		// 13交易发生日期
		String field13 = tranPkg.getFieldString(13);
		// 18 MCC
		String field18 = tranPkg.getFieldString(18);
		// 22 POS
		String field22 = tranPkg.getFieldString(22);
		// 25终端类型
		String field25 = tranPkg.getFieldString(25);
		// 32收单机构代码
		String field32 = tranPkg.getFieldString(32);
		// 33转发机构代码
		String field33 = tranPkg.getFieldString(33);
		// 37参考号
		String field37 = tranPkg.getFieldString(37);
		// 42商户号
		String field42 = tranPkg.getFieldString(42);
		// 43商户名称
		String field43 = tranPkg.getFieldString(43);
		// 49
		String field49 = tranPkg.getFieldString(49);
		// 60*
		String field60 = "PD05000000000000000000000000000000000000000000000000000";
		// 90原交易数据
		String orig_mti = tranPkg.getFieldString(0);// 原交易的信息类型代码
		String orig_traceNo = tranPkg.getFieldString(11);// 原交易系统跟踪号
		String orig_bocTxnTime = tranPkg.getFieldString(7);// 原交易的交易日期和时间
		String orig_acqInsCode = tranPkg.getFieldString(32);// 原交易的收单机构
		String orig_sndInsCode = tranPkg.getFieldString(33);// 原交易的发送机构
		String field90 = MessageUtil.getField90(orig_mti, orig_traceNo, orig_bocTxnTime, orig_acqInsCode, orig_sndInsCode);
		// 11系统跟踪号(和原交易相同)
		String field11 = orig_traceNo;
		try {
			reversalMsg.setFieldString(0, mti); // 消息头
			reversalMsg.setFieldString(2, field02);// 账号
			reversalMsg.setFieldString(3, field03);
			reversalMsg.setFieldString(4, field04);
			reversalMsg.setFieldString(7, field07);
			reversalMsg.setFieldString(11, field11);
			reversalMsg.setFieldString(12, field12);
			reversalMsg.setFieldString(13, field13);
			reversalMsg.setFieldString(18, field18);
			reversalMsg.setFieldString(22, field22);
			reversalMsg.setFieldString(25, field25);
			reversalMsg.setFieldString(32, field32);
			reversalMsg.setFieldString(33, field33);
			reversalMsg.setFieldString(37, field37);
			reversalMsg.setFieldString(41, field41);
			reversalMsg.setFieldString(42, field42);
			reversalMsg.setFieldString(43, field43);
			reversalMsg.setFieldString(49, field49);
			reversalMsg.setFieldString(60, field60);
			reversalMsg.setFieldString(90, field90);

		} catch (PackagingException e) {
			logger.debug("冲证包生成异常", e);
			throw new RuntimeException(e);
		}

		Runnable work = new Runnable() {

			@Override
			public void run() {
				while (true)
					try {
						Tran(reversalMsg);
						break;
					} catch (InterruptedException e) {
						logger.error("系统中断:原包信息{}", reversalMsg.toString(), e);
						break;
					} catch (PackagingException e) {
						logger.debug("包错误:原包信息{}", reversalMsg.toString(), e);
						break;
					} catch (TimeoutException e) {
						logger.error("冲证超时，准备重发:原包信息{}", reversalMsg.toString(), e);
					}
			}
		};
		reversalPool.execute(work);
	}

	@Override
	public void changeZpk(String newzpk) {
		if (encryption != null) {
			this.lmkZpk = encryption.getLmkTpk(lmkZmk, newzpk);
		}
	}

	public void SendNetManagement(String infoCode) {
		final IMessage netManagementMessage = packer.createEmpty();
		Calendar calendar = Calendar.getInstance();
		String mti = "0800";
		String field07 = MessageUtil.getField07(calendar);// 交易传送日期和时间MMddHHmmss
		String field11 = traceNoService.getTraceNo("00000000");
		String field33 = zpSystemConfigService.getAcqOrgId();// 183001
		String field70 = infoCode;
		String field100 = "140140";

		try {
			netManagementMessage.setFieldString(0, mti);
			netManagementMessage.setFieldString(7, field07);
			netManagementMessage.setFieldString(11, field11);
			netManagementMessage.setFieldString(33, field33);
			netManagementMessage.setFieldString(70, field70);
			netManagementMessage.setFieldString(100, field100);
		} catch (PackagingException e) {
			logger.error("网络管理类交易组装异常。包内容【{}】", netManagementMessage.toString(), e);
			throw new RuntimeException(e);
		}
		try {
			Tran(netManagementMessage);
		} catch (InterruptedException e) {
			logger.error("系统中断:原包信息【{}】" + netManagementMessage.toString(), e);
		} catch (PackagingException e) {
			logger.error("网络管理类交易组装异常。包内容【{}】", netManagementMessage.toString(), e);
		} catch (TimeoutException e) {
			logger.error("冲证超时，准备重发:原包信息【{}】" + netManagementMessage.toString(), e);
		}

	}
	
	public IMessage createMessage()
	{
		return packer.createEmpty();
	}
}
