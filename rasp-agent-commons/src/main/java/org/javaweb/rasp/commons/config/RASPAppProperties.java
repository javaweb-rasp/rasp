package org.javaweb.rasp.commons.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.javaweb.rasp.commons.constants.RASPAppConstants.*;
import static org.javaweb.rasp.commons.utils.EncryptUtils.base64Decode;
import static org.javaweb.rasp.commons.utils.JsonUtils.toJsonSetMap;
import static org.javaweb.rasp.commons.utils.URLUtils.appendFirstSlash;
import static org.javaweb.rasp.commons.utils.URLUtils.urlNormalize;

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
	 * 请求头中无需过滤的白名单
	 */
	private String[] headerWhitelist;

	/**
	 * 白名单列表
	 */
	private String[] whitelist;

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
		this.headerWhitelist = configMap.getArray(HEADER_WHITELIST);

		String whitelistStr = configMap.getString(WHITELIST);
		String patchListStr = configMap.getString(PATCH_LIST);

		this.patchList.clear();
		whitelist = new String[0];

		// W10=表示[]，空
		if (whitelistStr != null && !"W10=".equals(whitelistStr)) {
			Set<Map<String, Object>> setMap = toJsonSetMap(base64Decode(whitelistStr));

			int index = 0;
			whitelist = new String[setMap.size()];

			// 白名单URL预处理（URL标准化）
			for (Map<String, Object> map : setMap) {
				String uri = appendFirstSlash(urlNormalize((String) map.get("request_uri")));
				whitelist[index++] = uri != null ? uri.replaceAll("/$", "") : null;
			}
		}

		// W10=表示[]，空
		if (patchListStr != null && !"W10=".equals(patchListStr)) {
			this.patchList = toJsonSetMap(base64Decode(patchListStr));
		}

		// URL黑名单预处理（URL标准化）
		for (int i = 0; i < urlBlacklist.length; i++) {
			urlBlacklist[i] = appendFirstSlash(urlNormalize(urlBlacklist[i]));
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

	public String[] getHeaderWhitelist() {
		return headerWhitelist;
	}

	public String[] getWhitelist() {
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
