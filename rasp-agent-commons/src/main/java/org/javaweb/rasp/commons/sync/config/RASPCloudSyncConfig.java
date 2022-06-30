package org.javaweb.rasp.commons.sync.config;

import org.javaweb.rasp.commons.sync.RASPCloudSync;
import org.javaweb.rasp.commons.sync.RASPThreadSyncConfig;

import java.util.HashSet;
import java.util.Set;

import static java.util.Collections.synchronizedSet;
import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;
import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_PROPERTIES;
import static org.javaweb.rasp.loader.AgentConstants.AGENT_NAME;
import static org.javaweb.rasp.commons.utils.StringUtils.startWithIgnoreCase;

public class RASPCloudSyncConfig extends RASPThreadSyncConfig {

	/**
	 * RASP数据同步配置列表
	 */
	private static final Set<RASPCloudSync> RASP_DATA_SYNC_LIST = synchronizedSet(new HashSet<RASPCloudSync>());

	public RASPCloudSyncConfig(long syncInterval, boolean running) {
		super(syncInterval, running);
	}

	public static void addRASPDataSync(RASPCloudSync sync) {
		RASP_DATA_SYNC_LIST.add(sync);
	}

	public static void clearRASPDataSync() {
		RASP_DATA_SYNC_LIST.clear();
	}

	/**
	 * RASP数据同步服务（日志、配置文件）
	 */
	@Override
	public void dataSynchronization() {
		// 获取API同步地址
		String configApi = AGENT_PROPERTIES.getApiUrl();

		// 检测客户端是否配置了同步地址
		if (!startWithIgnoreCase(configApi, "http")) {
			AGENT_LOGGER.debug("{}未配置通讯地址，数据同步未启动...", AGENT_NAME);
			return;
		}

		AGENT_LOGGER.info("{}数据同步开始...", AGENT_NAME);

		for (RASPCloudSync sync : RASP_DATA_SYNC_LIST) {
			sync.sync();
		}
	}

}
