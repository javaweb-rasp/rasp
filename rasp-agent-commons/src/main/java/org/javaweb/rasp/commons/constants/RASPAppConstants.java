package org.javaweb.rasp.commons.constants;

public class RASPAppConstants {

	/**
	 * 应用ID
	 */
	public static final String APP_ID = "app_id";

	/**
	 * 开启的RASP 防御模块
	 */
	public static final String MODULES_OPEN = "modules.open";

	/**
	 * 是否是静默模式: true、false
	 */
	public static final String SILENT = "silent";

	/**
	 * IP黑名单列表
	 */
	public static final String IP_BLACKLIST = "ip.blacklist";

	/**
	 * URL黑名单列表
	 */
	public static final String URL_BLACKLIST = "url.blacklist";

	/**
	 * 请求头白名单
	 */
	public static final String HEADER_WHITELIST = "header.whitelist";

	/**
	 * 白名单列表
	 */
	public static final String WHITELIST = "whitelist";

	/**
	 * 补丁列表
	 */
	public static final String PATCH_LIST = "patch_list";

	/**
	 * 是否启用Servlet输入输出流Hook
	 */
	public static final String SERVLET_STREAM_HOOK = "servlet_stream";

	/**
	 * 最大缓存的Servlet输入输出流大小，默认10MB
	 */
	public static final String SERVLET_STREAM_MAX_CACHE_SIZE = "servlet_stream_max_cache_size";

}
