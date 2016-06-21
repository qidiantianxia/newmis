package com.yada.sdk.device.pos.posp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.LinkedBlockingQueue;

import org.jpos.iso.ISOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yada.sdk.device.encryption.IEncryption;
import com.yada.sdk.device.encryption.TerminalAuth;
import com.yada.sdk.device.pos.IVirtualPos;
import com.yada.sdk.device.pos.SequenceGenerator;
import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class VirtualPos implements IVirtualPos<Traner> {

  private final static Logger LOGGER = LoggerFactory.getLogger(VirtualPos.class);

  private static final String DEFAULT_TELLER_NO = "000";
  private static final String DEFAULT_BATCH_NO = "000000";
  private String merchantId;
  private String terminalId;
  private String tellerNo;
  private String serverIp;
  private int serverPort;
  private int timeout;
  private volatile boolean needSignin = true;
  private volatile boolean needParamDownload = true;
  private TerminalAuth terminalAuth;
  private String batchNo;
  private SequenceGenerator traceNoSeqGenerator;
  private SequenceGenerator cerNoSeqGenerator;
  private byte[] head;
  // 可阻塞的队列
  private LinkedBlockingQueue<IMessage> queue;

  public VirtualPos(String merchantId, String terminalId, String serverIp,
                    int serverPort, String zmkTmk, int timeout,
                    IEncryption encryptionMachine, byte[] head) {
    this(merchantId, terminalId, DEFAULT_TELLER_NO, serverIp, serverPort,
      zmkTmk, timeout, encryptionMachine, head);
  }

  public VirtualPos(String merchantId, String terminalId, String tellerNo,
                    String serverIp, int serverPort, String zmkTmk, int timeout,
                    IEncryption encryptionMachine, byte[] head) {
    this.merchantId = merchantId;
    this.terminalId = terminalId;
    this.tellerNo = tellerNo;
    this.serverIp = serverIp;
    this.serverPort = serverPort;
    this.timeout = timeout;
    this.terminalAuth = new TerminalAuth(encryptionMachine);
    terminalAuth.setTmk(zmkTmk);
    this.head = head;
    this.batchNo = DEFAULT_BATCH_NO;
    this.traceNoSeqGenerator = new SequenceGenerator("termNo_" + terminalId);
    this.cerNoSeqGenerator = new SequenceGenerator("cerNo_" + terminalId);
    this.queue = new LinkedBlockingQueue<IMessage>();
    //加载存储的冲正交易
    load();
    //执行工作线程
    new Thread(new WorkThread()).start();
  }

  @Override
  public Traner createTraner() throws IOException, ISOException, PackagingException {
    checkSingin();
    Traner traner = new Traner(merchantId, terminalId, tellerNo, batchNo,
      serverIp, serverPort, timeout, new CheckSignin(this),
      terminalAuth, traceNoSeqGenerator, cerNoSeqGenerator, head, queue);
    return traner;
  }

  private synchronized void checkSingin() throws IOException, ISOException, PackagingException {
    if (needSignin) {
      Traner traner = new Traner(merchantId, terminalId, tellerNo,
        batchNo, serverIp, serverPort, timeout, new CheckSignin(
        this), terminalAuth, traceNoSeqGenerator,
        cerNoSeqGenerator, head, queue);

      SigninInfo si = traner.singin();
      batchNo = si.batchNo;
      terminalAuth.setTak(si.tmkTak);
      terminalAuth.setTpk(si.tmkTpk);
      traner.close();
      needSignin = false;
    }

    if (needParamDownload) {
      Traner traner = new Traner(merchantId, terminalId, tellerNo,
        batchNo, serverIp, serverPort, timeout, new CheckSignin(
        this), terminalAuth, traceNoSeqGenerator,
        cerNoSeqGenerator, head, queue);
      traner.paramDownload();
      traner.close();
      needParamDownload = false;
    }
  }

  public void resetSingin() {
    needSignin = true;
  }

  public void resetParamDownload() {
    needParamDownload = true;
  }

  public void forward(IMessage t) {
    try {
      Traner traner = createTraner();
      // 发送。
      traner.reversal(t);
    } catch (PackagingException e) {
      LOGGER.debug("when the message forward happen PackagingException", e);
    } catch (ISOException e) {
      LOGGER.debug("when the message forward happen ISOException", e);
    } catch (IOException e) {
      // 未响应添加至队列中。
      queue.add(t);
    }
  }

  public void store() {

    Path path = Paths.get(System.getProperty("user.dir")).resolve("IMessageQueue");
    File dir = path.toFile();

    if (!dir.exists())
      dir.mkdir();

    path = path.resolve(this.terminalId + ".StoreAndForward");

    try (OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
         ObjectOutputStream objOut = new ObjectOutputStream(outputStream);) {
      objOut.writeObject(queue);
    } catch (IOException e) {
      LOGGER.debug("when store happen IOException", e);
    }
  }

  /**
   * 读取硬盘上的持久化文件，并将内容放入到queue后，删除该文件
   */
  protected void load() {
    Path path = Paths.get(System.getProperty("user.dir")).resolve("IMessageQueue");
    File dir = path.toFile();

    if (!dir.exists())
      dir.mkdir();

    path = path.resolve(this.terminalId + ".StoreAndForward");

    File file = path.toFile();

    if (!file.exists())
      return;

    try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ); ObjectInputStream objIn = new ObjectInputStream(inputStream);) {
      Object readObject = objIn.readObject();
      // 由开发人员保证序列化对象一致。
      @SuppressWarnings("unchecked")
      LinkedBlockingQueue<IMessage> loadedQueue = ((LinkedBlockingQueue<IMessage>) readObject);
      for (IMessage message; (message = loadedQueue.poll()) != null; ) {
        queue.add(message);
      }
      // 程序正常执行结束。
      Files.delete(path);
    } catch (IOException e) {
      LOGGER.debug("when load happen IOException", e);
    } catch (ClassNotFoundException e) {
      LOGGER.debug("when load happen ClassNotFoundException", e);
    }
  }

  /**
   * 工作线程。
   */
  protected class WorkThread implements Runnable {
    @Override
    public void run() {
      while (!Thread.currentThread().isInterrupted()) {
        try {
          // 从队列中阻塞式取值
          IMessage message = queue.take();
          // 转发
          forward(message);
        } catch (InterruptedException e) {
          // 程序停止时，回出现该异常。
        } catch (RuntimeException e) {
          // TODO LOG 保证工作线程不会因为意外原因退出。
        }
      }
    }
  }

}
