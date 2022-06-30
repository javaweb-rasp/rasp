package org.javaweb.rasp.commons.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.util.FileSize;
import org.javaweb.rasp.commons.config.RASPConfiguration;
import org.javaweb.rasp.loader.AgentConstants;
import org.javaweb.rasp.commons.logback.RASPFileAppender;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.rasp.proxy.loader.RASPModuleType;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static java.lang.Long.MAX_VALUE;
import static org.javaweb.rasp.commons.constants.RASPConstants.*;

public class RASPLogger {

	private static final LoggerContext LOGGER_CONTEXT = (LoggerContext) LoggerFactory.getILoggerFactory();

	public static Logger createRASPLogger(String name, File file, Level level) {
		return createRASPLogger(name, file, level, "%date %level [%thread] %logger{10} [%file:%line] %msg%n", null);
	}

	public static Logger createRASPLogger(String name, File file, Level level, String pattern, String fileSize) {
		// 设置日志格式
		PatternLayoutEncoder layout = new PatternLayoutEncoder();
		layout.setPattern(pattern);
		layout.setContext(LOGGER_CONTEXT);
		layout.start();

		RASPFileAppender<ILoggingEvent> fileAppender = new RASPFileAppender<ILoggingEvent>();
		fileAppender.setEncoder(layout);

		if (fileSize != null) {
			try {
				FileSize size = FileSize.valueOf(fileSize);
				fileAppender.setFileSize(size.getSize());
			} catch (IllegalArgumentException e) {
				fileAppender.setFileSize(MAX_VALUE);
			}
		} else {
			fileAppender.setFileSize(MAX_VALUE);
		}

		fileAppender.setFile(file.toString());
		fileAppender.setContext(LOGGER_CONTEXT);
		fileAppender.start();

		// 初始化日志配置
		Logger logger = (Logger) LoggerFactory.getLogger(name);
		logger.addAppender(fileAppender);
		logger.setLevel(level);
		logger.setAdditive(false);

		return logger;
	}

	/**
	 * 检测Logger上下文中是否注册了传入的logger
	 *
	 * @param loggerName logger名称
	 * @return 检测传入的logger是否已初始化
	 */
	public static boolean hasLogger(String loggerName) {
		List<Logger> loggerList = LOGGER_CONTEXT.getLoggerList();

		for (Logger logger : loggerList) {
			String name = logger.getName();

			if (name.equals(loggerName)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 刷新rasp-attack.log日志文件
	 *
	 * @param loggerPrefix logger前缀
	 */
	public static void rollover(String loggerPrefix) {
		List<Logger> loggerList = LOGGER_CONTEXT.getLoggerList();

		for (Logger logger : loggerList) {
			String loggerName = logger.getName();

			if (loggerName.startsWith(loggerPrefix)) {
				Iterator<Appender<ILoggingEvent>> iterator = logger.iteratorForAppenders();

				while (iterator.hasNext()) {
					Appender<ILoggingEvent> e = iterator.next();

					if (e instanceof RASPFileAppender) {
						RASPFileAppender<ILoggingEvent> appender = (RASPFileAppender<ILoggingEvent>) e;
						appender.rollover();
						appender.start();
					}
				}
			}
		}
	}

	public static void moduleErrorLog(RASPModuleType type, Exception e, Object... args) {
		StringBuilder sb = new StringBuilder(AgentConstants.AGENT_NAME).append("检测").append(type.getModuleName());

		if (args.length > 0) {
			sb.append("参数：").append(Arrays.toString(args));
		}

		sb.append("异常：").append(e);

		RASPConfiguration.MODULES_LOGGER.error(sb.toString(), e);
	}

	public static String getLoggerName(String prefix, String name) {
		return prefix + name.hashCode();
	}

	public static String getLoggerFileName(String logType) {
		if (ATTACK_LOG.equals(logType)) {
			return ATTACK_LOG_FILE_NAME;
		} else if (TRACE_LOG.equals(logType)) {
			return TRACE_LOG_FILE_NAME;
		}

		return ACCESS_LOG_FILE_NAME;
	}

	public static String getLoggerPrefix(String logType) {
		if (ATTACK_LOG.equals(logType)) {
			return ATTACK_LOGGER_PREFIX;
		} else if (TRACE_LOG.equals(logType)) {
			return TRACE_LOGGER_PREFIX;
		}

		return ACCESS_LOGGER_PREFIX;
	}

}