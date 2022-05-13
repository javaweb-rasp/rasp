package org.javaweb.rasp.commons.constants;

/**
 * RASP 配置文件键名常量
 *
 * @author yz
 * Date: 2019-07-18
 */
public class RASPConfigConstants {

	public static final String LOG_VERSION = "log_version";

	/**
	 * 站点ID
	 */
	public static final String SITE_ID = "site_id";

	/**
	 * RASP RC4加密Key
	 */
	public static final String SYSTEM_RC4_KEY = "system.rc4_key";

	/**
	 * RASP 云端通讯KEY
	 */
	public static final String SYSTEM_CONNECT_KEY = "system.connect_key";

	/**
	 * RASP 配置文件同步接口
	 */
	public static final String SYSTEM_API_URL = "system.api_url";

	/**
	 * 用户指定的需要Hook处理包名或者类名
	 */
	public static final String INCLUDE_HOOK_CLASS_NAME = "include_hook_class_name";

	/**
	 * 设置不需要经过Agent处理的包名正则表达式
	 */
	public static final String PROTECTED_HOOK_PACKAGE_REGEXP = "protected_hook_package_regexp";

	/**
	 * 启动Agent时是否显示版本信息
	 */
	public static final String DISPLAY_VERSION = "display_version";

	/**
	 * 字节码编辑器类型
	 */
	public static final String BYTECODE_EDITOR = "bytecode_editor";

	/**
	 * 本地配置同步时间间隔（秒）
	 */
	public static final String SYNC_INTERVAL = "sync.interval";

	/**
	 * 设置日志刷新间隔时间（秒）
	 */
	public static final String FLUSH_INTERVAL = "flush.interval";

	/**
	 * 获取代理IP的请求头名称
	 */
	public static final String PROXY_IP_HEADER = "proxy_ip_header";

	/**
	 * RASP日志输出级别
	 */
	public static final String LOG_LEVEL = "log.level";

	/**
	 * RASP 版本号
	 */
	public static final String VERSION = "version";

}
