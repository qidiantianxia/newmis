package com.yada.sdk.packages.transaction.jpos;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.jpos.util.LogEvent;
import org.jpos.util.LogListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JPOS日志监听器
 * 
 * @author jiangfengming
 * 
 */
class JposLogListener implements LogListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(JposLogListener.class);

	@Override
	public LogEvent log(LogEvent ev) {
		if (LOGGER.isDebugEnabled()) {
			return debugHandle(ev);
		} else {
			return errorHandle(ev);
		}
	}

	protected LogEvent debugHandle(LogEvent ev) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(bos);) {
			ev.dump(ps, "");
			StringBuilder logMsg = new StringBuilder();
			logMsg.append(System.getProperty("line.separator"));
			logMsg.append(new String(bos.toByteArray()));
			LOGGER.debug(logMsg.toString());
		} catch (Exception e) {
			LOGGER.error("", e);
		}
		return ev;
	}

	protected synchronized LogEvent errorHandle(LogEvent ev) {
		synchronized (ev.getPayLoad()) {
			for (Object o : ev.getPayLoad()) {
				if (o instanceof Throwable) {
					try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(bos);) {
						ev.dump(ps, "");
						LOGGER.error(new String(bos.toByteArray()));
					} catch (Exception e) {
						LOGGER.error("JPOS日志记录失败.", e);
					}
					return ev;
				}
			}
			return null;
		}

	}

}