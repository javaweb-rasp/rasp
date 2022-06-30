package org.javaweb.rasp.commons.config;

import ch.qos.logback.core.util.FileSize;

import java.util.concurrent.TimeUnit;

import static org.javaweb.rasp.commons.constants.RASPConfigConstants.*;
import static org.javaweb.rasp.commons.constants.RASPConstants.DEFAULT_PROTECTED_PACKAGE_REGEXP;
import static org.javaweb.rasp.commons.constants.RASPConstants.JAVASSIST;

public class RASPAgentProperties extends RASPProperties {

	private String logVersion;

	private String siteID;

	private String rc4Key;

	private String connectKey;

	private String apiUrl;

	private String includeHookClassName;

	private String protectedHookPackageRegexp;

	private boolean displayVersion;

	private String bytecodeEditor;

	private int syncInterval;

	private int flushInterval;

	private String proxyIpHeader;

	private String logLevel;

	private String version;

	private String logBufferSize;

	private static final String DEFAULT_BUFFER_SIZE = "10MB";

	/**
	 * 最大间隔时间：1天
	 */
	private static final long MAX_TIME_INTERVAL = TimeUnit.HOURS.toSeconds(1);

	@Override
	public void reloadConfig(RASPConfigMap<String, Object> configMap) {
		super.reloadConfig(configMap);

		this.logVersion = configMap.getString(LOG_VERSION);
		this.siteID = configMap.getString(SITE_ID);
		this.rc4Key = configMap.getString(SYSTEM_RC4_KEY);
		this.connectKey = configMap.getString(SYSTEM_CONNECT_KEY);
		this.apiUrl = configMap.getString(SYSTEM_API_URL);
		this.includeHookClassName = configMap.getString(INCLUDE_HOOK_CLASS_NAME);

		this.protectedHookPackageRegexp = configMap.getString(
				PROTECTED_HOOK_PACKAGE_REGEXP, DEFAULT_PROTECTED_PACKAGE_REGEXP
		);

		this.displayVersion = configMap.getBoolean(DISPLAY_VERSION, true);
		this.bytecodeEditor = configMap.getString(BYTECODE_EDITOR, JAVASSIST);
		this.syncInterval = configMap.getInt(SYNC_INTERVAL, 30);
		this.flushInterval = configMap.getInt(FLUSH_INTERVAL, 3);

		// 设置云端同步时间间隔范围
		if (syncInterval < 1 || syncInterval > MAX_TIME_INTERVAL) {
			syncInterval = 30;
		}

		// 设置日志刷新时间间隔范围
		if (flushInterval < 1 || flushInterval > MAX_TIME_INTERVAL) {
			flushInterval = 3;
		}

		this.proxyIpHeader = configMap.getString(PROXY_IP_HEADER, "x-forwarded-for");
		this.logLevel = configMap.getString(LOG_LEVEL);
		this.version = configMap.getString(VERSION);
		this.logBufferSize = configMap.getString(LOG_BUFFER_SIZE, DEFAULT_BUFFER_SIZE);

		try {
			// 检测缓存日志大小设置是否正确
			FileSize.valueOf(logBufferSize);
		} catch (IllegalArgumentException e) {
			this.logBufferSize = DEFAULT_BUFFER_SIZE;
		}
	}

	public String getLogVersion() {
		return logVersion;
	}

	public String getSiteID() {
		return siteID;
	}

	public String getRc4Key() {
		return rc4Key;
	}

	public String getConnectKey() {
		return connectKey;
	}

	public String getApiUrl() {
		return apiUrl;
	}

	public String getIncludeHookClassName() {
		return includeHookClassName;
	}

	public String getProtectedHookPackageRegexp() {
		return protectedHookPackageRegexp;
	}

	public boolean isDisplayVersion() {
		return displayVersion;
	}

	public String getBytecodeEditor() {
		return bytecodeEditor;
	}

	public int getSyncInterval() {
		return syncInterval;
	}

	public int getFlushInterval() {
		return flushInterval;
	}

	public String getProxyIpHeader() {
		return proxyIpHeader;
	}

	public String getLogLevel() {
		return logLevel;
	}

	public String getVersion() {
		return version;
	}

	public String getLogBufferSize() {
		return logBufferSize;
	}

}