package org.javaweb.rasp.commons.config;

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
		this.proxyIpHeader = configMap.getString(PROXY_IP_HEADER, "x-forwarded-for");
		this.logLevel = configMap.getString(LOG_LEVEL);
		this.version = configMap.getString(VERSION);
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

}