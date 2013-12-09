package com.yada.sdk.packages.transaction.jpos;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jpos.util.LogEvent;
import org.jpos.util.LogListener;
import org.slf4j.Logger;

/**
 * JPOS日志Debug级别监听器
 * 
 * @author jiangfengming
 * 
 */
class JposLogErrorListener implements LogListener {
	private Logger logger;

	public JposLogErrorListener(Logger logger) {
		this.logger = logger;
	}

	@Override
	public synchronized LogEvent log(LogEvent ev) {
		synchronized (ev.getPayLoad()) {
			boolean recordLog = false;
			for (Object o : ev.getPayLoad()) {
				if (o instanceof Throwable) {
					recordLog = true;
					break;
				} else {
					return ev;
				}
			}
			if (recordLog) {
				try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
						PrintStream ps = new PrintStream(bos);) {
					ev.dump(ps, "");
					logger.error(new String(bos.toByteArray()));
				} catch (Exception e) {
					logger.error("JPOS日志记录失败.", e);
				}
			}

			return ev;
		}

	}
}