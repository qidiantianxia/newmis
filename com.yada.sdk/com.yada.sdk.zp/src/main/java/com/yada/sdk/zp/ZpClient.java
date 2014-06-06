package com.yada.sdk.zp;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
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
import com.yada.sdk.security.IBizSystemExitService;

public class ZpClient implements IZpkChangeNotify, IBizSystemExitService {
	private final static Logger logger = LoggerFactory.getLogger(ZpClient.class);
	private AsyncTcpClient client;
	private ZpPacker packer;
	private EncryptionMachine encryption;
	private ConcurrentMap<String, TranContext> map;
	private int timeout;
	private ExecutorService notifyPkgWorkPool;
	private ITraceNoService traceNoService;
	private String lmkZpk;
	private String lmkZmk;
	private IZpSystemConfigService zpSystemConfigService;
	private String acqOrgId;
	private ConcurrentLinkedDeque<IMessage> notifyMessageList = new ConcurrentLinkedDeque<IMessage>();

	public String getAcqOrgId() {
		return acqOrgId;
	}

	public ZpClient(String zpServerIp, int zpServerPort, ITraceNoService traceNoService, int timeout) {
		this(zpServerIp, zpServerPort, null, 0, null, traceNoService, timeout);
	}

	public ZpClient(String zpServerIp, int zpServerPort, String encryptionIp, int encryptionPort, IZpSystemConfigService zpSystemConfigService,
			ITraceNoService traceNoService, int timeout) {
		this.timeout = timeout;
		int zpHeadLength = 4;
		this.map = new ConcurrentSkipListMap<String, TranContext>();
		this.notifyPkgWorkPool = Executors.newFixedThreadPool(10);
		this.traceNoService = traceNoService;
		this.zpSystemConfigService = zpSystemConfigService;
		try {
			this.packer = new ZpPacker(0);
		} catch (ISOException e) {
			throw new RuntimeException(e);
		}
		IPackageSplitterFactory packageSplitterFactory = new FixLenPackageSplitterFactory(zpHeadLength, false);
		IPackageProcessorFactory packageProcessorFactory = new RecvPackageProcessorFactory(map, packer, zpSystemConfigService, this);

		acqOrgId = zpSystemConfigService.getAcqOrgId();

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

	public String getEncryptionPin(String accountNo, String pin) {
		return encryption.getTpkPin(accountNo, pin, lmkZpk);
	}

	public String getTraceNo(String terminalId) {
		return traceNoService.getTraceNo(terminalId);

	}

	public IMessage tran(IMessage pkg) throws InterruptedException, PackagingException, TimeoutException, ZpConnectionException {
		logger.info("向IST发送交易{}", pkg);
		TranContext tranContext = new TranContext();
		tranContext.reqMessage = pkg;
		String key = pkg.getTranId();
		map.put(key, tranContext);
		ByteBuffer rawBuffer;
		rawBuffer = packer.pack(pkg);

		if (!client.isOpen())
			throw new ZpConnectionException();
		client.send(rawBuffer);

		synchronized (tranContext) {
			tranContext.wait(timeout);

			if (Calendar.getInstance().getTimeInMillis() - tranContext.createDateTime >= timeout)
				throw new TimeoutException("交易超时");
		}

		map.remove(key);
		logger.info("从IST收到交易{}", tranContext.respMessage);
		return tranContext.respMessage;
	}

	public void reversal(IMessage tranPkg) {
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
		String field07 = tranPkg.getFieldString(7);
		// 系统跟踪号
		String field11 = tranPkg.getFieldString(11);
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
		// 41 终端号
		String field41 = tranPkg.getFieldString(41);
		// 42商户号
		String field42 = tranPkg.getFieldString(42);
		// 43商户名称
		String field43 = tranPkg.getFieldString(43);
		// 49
		String field49 = tranPkg.getFieldString(49);
		// 60*
		String field60 = tranPkg.getFieldString(60);
		// 90原交易数据
		String orig_mti = tranPkg.getFieldString(0);// 原交易的信息类型代码
		String orig_traceNo = tranPkg.getFieldString(11);// 原交易系统跟踪号
		String orig_bocTxnTime = tranPkg.getFieldString(7);// 原交易的交易日期和时间
		String orig_acqInsCode = tranPkg.getFieldString(32);// 原交易的收单机构
		String orig_sndInsCode = tranPkg.getFieldString(33);// 原交易的发送机构
		String field90 = ZpClient.getField90(orig_mti, orig_traceNo, orig_bocTxnTime, orig_acqInsCode, orig_sndInsCode);
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
			logger.debug("冲证包生成异常,原交易信息:{}", reversalMsg, e);
			throw new RuntimeException(e);
		}

		notifyTran(reversalMsg);
	}

	public void notifyTran(final IMessage notifyMessage) {

		Runnable work = new Runnable() {

			@Override
			public void run() {
				while (true)
					try {
						notifyMessageList.add(notifyMessage);
						IMessage message = tran(notifyMessage);
						notifyMessageList.remove(notifyMessage);
						String respCode = message.getFieldString(39);
						if ("00".equals(respCode)) {
							break;

						} else if ("19".equals(respCode) || "91".equals(respCode) || "96".equals(respCode) || "84".equals(respCode)) {
							logger.warn("通知类交易已发送成功，但返回码需重做交易,reqMessage:{} respMessage{}", notifyMessage, message);
						} else {
							logger.error("通知类交易已发送成功，但返回码错误,reqMessage:{} respMessage{}", notifyMessage, message);
							break;
						}
					} catch (InterruptedException e) {
						logger.error("系统中断:原包信息{}", notifyMessage, e);
						break;
					} catch (PackagingException e) {
						logger.debug("包错误:原包信息{}", notifyMessage, e);
						break;
					} catch (TimeoutException e) {
						logger.error("通知交易超时，准备重发:原包信息{}", notifyMessage, e);
					} catch (ZpConnectionException e) {
						logger.error("无法连接ZP，10秒后准备重发:原包信息{}", notifyMessage);
						try {
							Thread.sleep(10000);
						} catch (InterruptedException e1) {
							logger.error("系统中断:原包信息{}", notifyMessage, e1);
							break;
						}
					}
			}
		};
		notifyPkgWorkPool.execute(work);
	}

	@Override
	public void changeZpk(String newzpk) {
		if (encryption != null) {
			this.lmkZpk = encryption.getLmkTpk(lmkZmk, newzpk);
		}
	}

	public void sendNetManagement(NetMngInfoCode enumNetMngInfoCode) {
		String infoCode = enumNetMngInfoCode.getValue();
		final IMessage netManagementMessage = packer.createEmpty();
		Calendar calendar = Calendar.getInstance();
		String mti = "0800";
		String field07 = ZpClient.getField07(String.format("%1$tY%1$tm%1$td", calendar), String.format("%1$tH%1$tM%1$tS", calendar));// 交易传送日期和时间MMddHHmmss
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
			tran(netManagementMessage);
		} catch (InterruptedException e) {
			logger.error("系统中断:原包信息【{}】" + netManagementMessage.toString(), e);
		} catch (PackagingException e) {
			logger.error("网络管理类交易组装异常。包内容【{}】", netManagementMessage.toString(), e);
		} catch (TimeoutException e) {
			logger.error("交易超时:原包信息【{}】" + netManagementMessage.toString(), e);
		} catch (ZpConnectionException e) {
			logger.error("无法连接ZP:原包信息【{}】" + netManagementMessage.toString(), e);
		}

	}

	public IMessage createMessage() {
		return packer.createEmpty();
	}

	public static String getField07(String txnDate, String txnTime) {
		try {
			String currentDateTime = new StringBuilder().append(txnDate).append(txnTime).toString();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			// 先将时间解析成标准日期
			Date dateTime = dateFormat.parse(currentDateTime);
			// 输出日期的格式
			SimpleDateFormat outDateFormat = new SimpleDateFormat("MMddHHmmss");
			// 设置格林威治时间
			outDateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
			return outDateFormat.format(dateTime);
		} catch (ParseException e) {
			logger.error("时间无法解析【{}{}】", txnDate, txnTime, e);
			throw new RuntimeException(e);
		}
	}

	public static String getField14(String yearOfValidThru, String monthOfValidThru) {
		StringBuilder field14 = new StringBuilder();
		if (yearOfValidThru.length() < 2) {
			field14.append("0");
		}
		field14.append(yearOfValidThru);
		if (monthOfValidThru.length() < 2) {
			field14.append("0");
		}
		field14.append(monthOfValidThru);
		return field14.toString();
	}

	/**
	 * 处理中国银行IST（ZP） 8583包的37域
	 * 
	 * @param date
	 * @param traceNo
	 * @return
	 */
	public static String getField37(String txnDate, String txnTime, String traceNo) {
		try {
			String currentDateTime = new StringBuilder().append(txnDate).append(txnTime).toString();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			// 先将时间解析成标准日期
			Date dateTime = dateFormat.parse(currentDateTime);
			StringBuilder field37 = new StringBuilder();
			field37.append(new SimpleDateFormat("yyDDDHH").format(dateTime).substring(1, 7)).append(traceNo);
			return field37.toString();
		} catch (ParseException e) {
			logger.error("时间无法解析【{}{}】", txnDate, txnTime, e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * 48域用法4，TLV标签92，用法03
	 * 
	 * @param cvn2
	 * @return
	 */
	public static String getField48Usage4Tag9203(String cvn2) {
		return new StringBuilder().append("9203").append(cvn2).toString();
	}

	public static String getField61(Boolean hasPassword, Boolean hasValidityPeriod, Boolean hasCVN2, String credentialsType, String credentialsNo,
			String mobileNo) {
		StringBuilder field61 = new StringBuilder();
		field61.append("00000000000000000000000000000000");
		if (hasPassword != null && hasPassword) {
			field61.replace(0, 1, "1");
		}
		if (hasValidityPeriod != null && hasValidityPeriod) {
			field61.replace(1, 2, "1");
		}
		if (hasCVN2 != null && hasCVN2) {
			field61.replace(5, 6, "1");
		}
		if (credentialsType != null && credentialsNo != null && !credentialsType.isEmpty() && !credentialsNo.isEmpty()) {
			field61.replace(2, 3, "1");
			field61.append(credentialsType);
			field61.append(handleCredentialsNo(credentialsNo));
		}
		if (mobileNo != null && !mobileNo.isEmpty()) {
			field61.replace(8, 9, "1");
			field61.append(String.format("%03d", mobileNo.length()));
			field61.append(mobileNo);
		}
		return new StringBuilder("AM").append(String.format("%03d", field61.length())).append(field61).toString();
	}

	protected static String handleCredentialsNo(String credentialsNo) {
		StringBuilder tmpCredentialsNo = new StringBuilder();
		// 以下参照IST接口规范5.9 61域AM用法 进行证件号码处理。
		char[] nums = new char[6];
		int noLen = nums.length;
		char[] credentialsNos = credentialsNo.toCharArray();
		for (int i = credentialsNos.length; i > 0 && noLen > 0; i--) {
			if (Character.isDigit(credentialsNos[i - 1])) {
				nums[noLen - 1] = credentialsNos[i - 1];
				noLen--;
			}
		}
		for (; noLen > 0; noLen--) {
			nums[noLen - 1] = '0';
		}
		tmpCredentialsNo.append(nums);
		tmpCredentialsNo.append("              ");// 后边补14个空格

		return tmpCredentialsNo.toString();
	}

	/**
	 * 处理中国银行IST（ZP） 8583包的90域。
	 * 
	 * @param mti
	 *            原交易的MTI
	 * @param traceNo
	 *            原交易的traceNo
	 * @param bocTxnTime
	 *            原交易的第七域交易时间
	 * @param acqInsCode
	 *            原交易的acqInsCode
	 * @param sndInsCode
	 *            原交易的sndInsCode
	 * @return
	 */
	public static String getField90(String mti, String traceNo, String bocTxnTime, String acqInsCode, String sndInsCode) {
		StringBuilder field90 = new StringBuilder(42);
		field90.append(mti);
		field90.append(traceNo);
		field90.append(bocTxnTime);
		for (int i = 0; i < 11 - acqInsCode.length(); i++) {// origAcquiringOrgId左边补零
			field90.append("0");
		}
		field90.append(acqInsCode);
		for (int i = 0; i < 11 - sndInsCode.length(); i++) {// origForwardingOrgId左边补零
			field90.append("0");
		}
		field90.append(sndInsCode);
		return field90.toString();
	}

	/**
	 * 获取返回码对应的信息
	 * 
	 * @param respCode
	 * @return
	 */
	public static String getRespMessage(String respCode) {
		return RespCodeMap.getMessage(respCode);
	}

	@Override
	public void beginClose() {
	}

	@Override
	public List<Object> getBizData() {
		List<Object> list = new ArrayList<Object>();
		list.addAll(notifyMessageList);
		return list;
	}

	@Override
	public String getSystemName() {
		return "ZP Client";
	}

	public void close() {
		if (!this.notifyPkgWorkPool.isShutdown()) {
			this.notifyPkgWorkPool.shutdown();
		}
		if (!this.notifyPkgWorkPool.isTerminated()) {
			this.notifyPkgWorkPool.shutdownNow();
		}
		try {
			this.notifyPkgWorkPool.awaitTermination(10, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		this.client.close();
	}
}
