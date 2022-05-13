package org.javaweb.rasp.commons.sync.config;

import org.javaweb.rasp.commons.log.RASPLog;
import org.javaweb.rasp.commons.log.RASPLogData;
import org.javaweb.rasp.commons.sync.RASPThreadSyncConfig;
import org.javaweb.rasp.commons.utils.JsonUtils;
import org.slf4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;

import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_PROPERTIES;
import static org.javaweb.rasp.commons.utils.EncryptUtils.enContent;

public class RASPLoggerSyncConfig extends RASPThreadSyncConfig {

	public RASPLoggerSyncConfig(long syncInterval, boolean running) {
		super(syncInterval, running);
	}

	/**
	 * 设置内存中最大放置的RASP日志数
	 */
	private static final int MAX_LOG_COUNT = 1000;

	/**
	 * RASP日志队列
	 */
	protected static ArrayBlockingQueue<RASPLogData> raspLogQueue = new ArrayBlockingQueue<RASPLogData>(MAX_LOG_COUNT);

	public static void addRASPLogData(RASPLogData log) {
		if (raspLogQueue.size() < MAX_LOG_COUNT) {
			raspLogQueue.offer(log);
		} else {
			writeLog(log);
		}
	}

	private static void writeLog(RASPLogData logData) {
		Logger  logger = logData.getLogger();
		RASPLog log    = logData.getRaspLog();

		if (logger != null && log != null) {
			String json = JsonUtils.toJson(log);

			if (logData.isEncrypt()) {
				json = enContent(json, AGENT_PROPERTIES.getRc4Key());
			}

			logger.info(json);
		}
	}

	@Override
	public void dataSynchronization() {
		RASPLogData log;

		while ((log = raspLogQueue.poll()) != null) {
			writeLog(log);
		}
	}

}
