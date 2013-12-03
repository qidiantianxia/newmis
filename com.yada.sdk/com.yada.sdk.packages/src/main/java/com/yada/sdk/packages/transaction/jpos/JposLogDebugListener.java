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
public class JposLogDebugListener implements LogListener {
	private Logger logger;

	public JposLogDebugListener(Logger logger) {
		this.logger = logger;
	}

	@Override
	public LogEvent log(LogEvent ev) {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PrintStream ps = new PrintStream(bos);) {
			ev.dump(ps, "");
			StringBuilder logMsg = new StringBuilder();
			logMsg.append(System.getProperty("line.separator"));
			logMsg.append(new String(bos.toByteArray()));
			logger.debug(logMsg.toString());
		} catch (Exception e) {
			logger.error("", e);
		}
		return ev;
	}

}