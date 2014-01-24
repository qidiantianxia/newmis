package com.yada.sdk.device.pos;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.LinkedBlockingQueue;

import org.jpos.iso.ISOException;

import com.yada.sdk.device.pos.posp.Traner;
import com.yada.sdk.packages.PackagingException;
import com.yada.sdk.packages.transaction.IMessage;

public class SimpleIMessageStoreAndForward implements IStoreAndForward<IMessage> {
	// 可阻塞的队列
	private LinkedBlockingQueue<IMessage> queue;
	// 持久化文件的路径。
	private String filePath;
	// 持久化文件的名称
	private String fileName = "IMessageQueue.StoreAndForward";

	// 发送目标的IP
	private String targetIp;
	// 目标端口
	private int targetPort;
	// 连接超时时间。
	private int connectTimeOut;

	public SimpleIMessageStoreAndForward(String targetIp, int targetPort, int connectTimeOut) {
		queue = new LinkedBlockingQueue<IMessage>();
		this.targetIp = targetIp;
		this.targetPort = targetPort;
		this.connectTimeOut = connectTimeOut;
		load();
	}

	@Override
	public void forward(IMessage t) {
		try {
			Traner traner = createTranner(t);
			// 发送。
			traner.reversal(t);
		} catch (PackagingException e) {
			// LOG
		} catch (ISOException e) {
			// LOG
		} catch (IOException e) {
			// 未响添加至队列中。
			addElementToQueue(t);
		}
	}

	protected Traner createTranner(IMessage message) throws IOException, ISOException {
		return new Traner(null, null, null, null, targetIp, targetPort, connectTimeOut, null, null, null, null,null);
	}

	protected void addElementToQueue(IMessage message) {
		try {
			queue.put(message);
		} catch (InterruptedException e) {
			// LOG
		}
	}

	@Override
	public void store() {
		Path path = Paths.get(filePath, fileName);
		try (OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
				ObjectOutputStream objOut = new ObjectOutputStream(outputStream);) {
			objOut.writeObject(queue);
		} catch (IOException e) {
			// LOG
		}
	}

	/**
	 * 读取硬盘上的持久化文件，并将内容放入到queue后，删除该文件
	 */
	protected void load() {
		Path path = Paths.get(filePath, fileName);
		try (InputStream inputStream = Files.newInputStream(path, StandardOpenOption.READ); ObjectInputStream objIn = new ObjectInputStream(inputStream);) {
			Object readObject = objIn.readObject();
			// 由开发人员保证序列化对象一致。
			@SuppressWarnings("unchecked")
			LinkedBlockingQueue<IMessage> loadedQueue = ((LinkedBlockingQueue<IMessage>) readObject);
			for (IMessage message; (message = loadedQueue.poll()) != null;) {
				addElementToQueue(message);
			}
			// 程序正常执行结束。
			Files.delete(path);
		} catch (IOException e) {
			// TODO LOG
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO LOG
			e.printStackTrace();
		}
	}

	/**
	 * 工作线程。
	 * 
	 * @author jiangfengming
	 * 
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
