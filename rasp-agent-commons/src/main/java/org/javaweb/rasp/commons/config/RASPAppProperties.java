package org.javaweb.rasp.commons.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.javaweb.rasp.commons.constants.RASPAppConstants.*;
import static org.javaweb.rasp.commons.utils.EncryptUtils.base64Decode;
import static org.javaweb.rasp.commons.utils.JsonUtils.toJsonSetMap;
import static org.javaweb.rasp.commons.utils.StringUtils.isNotEmpty;

public class RASPAppProperties extends RASPProperties {

	/**
	 * 应用ID
	 */
	private String appID;

	/**
	 * RASP 当前开放的防御模块
	 */
	protected int[] openModules;

	/**
	 * 是否是静默模式: true、false
	 */
	private boolean silent;

	/**
	 * IP黑名单列表
	 */
	private String[] ipBlacklist;

	/**
	 * URL黑名单列表
	 */
	private String[] urlBlacklist;

	/**
	 * 白名单列表
	 */
	private Set<Map<String, Object>> whitelist = new HashSet<Map<String, Object>>();

	/**
	 * 补丁列表
	 */
	private Set<Map<String, Object>> patchList = new HashSet<Map<String, Object>>();

	/**
	 * 是否启用Servlet输入输出流Hook
	 */
	private boolean servletStreamHook;

	/**
	 * 最大缓存的Servlet输入输出流大小，默认10MB
	 */
	private int servletStreamMaxCacheSize;

	public void reloadConfig(RASPConfigMap<String, Object> configMap) {
		super.reloadConfig(configMap);

		this.appID = (String) configMap.get(APP_ID);
		String[] modules = configMap.getArray(MODULES_OPEN);

		this.openModules = new int[modules.length];

		for (int i = 0; i < openModules.length; i++) {
			this.openModules[i] = modules[i].hashCode();
		}

		this.silent = configMap.getBoolean(SILENT, false);
		this.ipBlacklist = configMap.getArray(IP_BLACKLIST);
		this.urlBlacklist = configMap.getArray(URL_BLACKLIST);

		String whitelistStr = configMap.getString(WHITELIST);
		String patchListStr = configMap.getString(PATCH_LIST);

		this.whitelist.clear();
		this.patchList.clear();

		// W10=表示[]，空
		if (isNotEmpty(whitelistStr) && !"W10=".equals(whitelistStr)) {
			this.whitelist = toJsonSetMap(base64Decode(whitelistStr));
		}

		// W10=表示[]，空
		if (isNotEmpty(patchListStr) && !"W10=".equals(patchListStr)) {
			this.patchList = toJsonSetMap(base64Decode(patchListStr));
		}

		this.servletStreamHook = configMap.getBoolean(SERVLET_STREAM_HOOK, false);
		this.servletStreamMaxCacheSize = configMap.getInt(SERVLET_STREAM_MAX_CACHE_SIZE);
	}

	public String getAppID() {
		return appID;
	}

	public int[] getOpenModules() {
		return openModules;
	}

	public boolean isSilent() {
		return silent;
	}

	public String[] getIpBlacklist() {
		return ipBlacklist;
	}

	public String[] getUrlBlacklist() {
		return urlBlacklist;
	}

	public Set<Map<String, Object>> getWhitelist() {
		return whitelist;
	}

	public Set<Map<String, Object>> getPatchList() {
		return patchList;
	}

	public boolean isServletStreamHook() {
		return servletStreamHook;
	}

	public int getServletStreamMaxCacheSize() {
		return servletStreamMaxCacheSize;
	}

}
