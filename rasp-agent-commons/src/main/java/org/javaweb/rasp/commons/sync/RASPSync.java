package org.javaweb.rasp.commons.sync;

import org.javaweb.rasp.commons.config.RASPAppProperties;
import org.javaweb.rasp.commons.config.RASPPropertiesConfiguration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Collections.synchronizedSet;
import static org.javaweb.rasp.commons.RASPLogger.getLoggerName;
import static org.javaweb.rasp.commons.RASPLogger.hasLogger;
import static org.javaweb.rasp.commons.config.RASPConfiguration.*;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;
import static org.javaweb.rasp.commons.loader.AgentConstants.ATTACK_LOGGER_PREFIX;
import static org.javaweb.rasp.commons.utils.StringUtils.startWithIgnoreCase;
import static org.javaweb.rasp.commons.utils.URLUtils.getStandardContextPath;

public class RASPSync {

	/**
	 * 是否是运行中
	 */
	private boolean running;

	/**
	 * 间隔时间
	 */
	private final long syncInterval;

	/**
	 * RASP数据同步配置列表
	 */
	private static final Set<RASPDataSync> RASP_DATA_SYNC_LIST = synchronizedSet(new HashSet<RASPDataSync>());

	public RASPSync(long syncInterval) {
		this.syncInterval = syncInterval;
		this.running = true;
	}

	public static void addRASPDataSync(RASPDataSync sync) {
		RASP_DATA_SYNC_LIST.add(sync);
	}

	public static void clearRASPDataSync(RASPDataSync sync) {
		RASP_DATA_SYNC_LIST.clear();
	}

	/**
	 * 获取Web应用的RASP配置对象
	 *
	 * @return 应用配置列表
	 */
	public static List<RASPPropertiesConfiguration<RASPAppProperties>> getAppConfigList() {
		List<RASPPropertiesConfiguration<RASPAppProperties>> configList =
				new ArrayList<RASPPropertiesConfiguration<RASPAppProperties>>();

		File[] apps = RASP_APP_CONFIG_DIRECTORY.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".properties");
			}
		});

		if (apps == null) return configList;

		for (File app : apps) {
			// 截取web应用名称
			String appName     = app.getName().substring(0, app.getName().lastIndexOf('.'));
			String contextPath = getStandardContextPath(appName);
			String loggerName  = getLoggerName(ATTACK_LOGGER_PREFIX, contextPath);

			if (hasLogger(loggerName)) {
				configList.add(getWebApplicationConfig(contextPath));
			}
		}

		return configList;
	}

	/**
	 * RASP数据同步服务（日志、配置文件）
	 */
	public void dataSynchronization() {
		// 获取API同步地址
		String configApi = AGENT_PROPERTIES.getApiUrl();

		// 检测客户端是否配置了同步地址
		if (!startWithIgnoreCase(configApi, "http")) {
			AGENT_LOGGER.debug("{}未配置通讯地址，数据同步未启动...", AGENT_NAME);
			return;
		}

		AGENT_LOGGER.info("{}数据同步开始...", AGENT_NAME);

		for (RASPDataSync sync : RASP_DATA_SYNC_LIST) {
			sync.sync();
		}
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public long getSyncInterval() {
		return syncInterval;
	}

}
