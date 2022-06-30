package org.javaweb.rasp.commons.config;

import ch.qos.logback.classic.Level;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.javaweb.rasp.commons.constants.RASPConstants.*;
import static org.javaweb.rasp.commons.log.RASPLogger.createRASPLogger;
import static org.javaweb.rasp.commons.utils.StringUtils.*;
import static org.javaweb.rasp.loader.AgentConstants.*;
import static org.javaweb.rasp.commons.utils.FileUtils.copyFile;

/**
 * RASP 配置
 * Created by yz on 2016/11/17.
 */
public class RASPConfiguration {

	// RASP 安装目录
	public static final File RASP_DIRECTORY = new File(
			RASPConfiguration.class.getProtectionDomain().getCodeSource().getLocation().getFile()
	).getParentFile();

	// RASP 适配文件
	public static final File RASP_ADAPTER_FILE = new File(RASP_DIRECTORY, ADAPTER_FILE_NAME);

	// RASP 配置目录
	public static final File RASP_CONFIG_DIRECTORY = getDirectory(new File(RASP_DIRECTORY, "config"));

	// RASP 应用目录
	public static final File RASP_APP_CONFIG_DIRECTORY = new File(RASP_CONFIG_DIRECTORY, "apps");

	// RASP Hook目录
	public static final File RASP_HOOK_DIRECTORY = getDirectory(new File(RASP_DIRECTORY, "hooks"));

	// RASP 日志目录
	public static final File RASP_LOG_DIRECTORY = getDirectory(new File(RASP_DIRECTORY, "logs"));

	// RASP 数据目录
	public static final File RASP_DATABASE_DIRECTORY = getDirectory(new File(RASP_DIRECTORY, "database"));

	// RASP Agent日志文件
	public static final File RASP_AGENT_FILE = new File(RASP_DATABASE_DIRECTORY, AGENT_LOG_FILE_NAME);

	// RASP 防御模块日志文件
	public static final File RASP_MODULES_FILE = new File(RASP_DATABASE_DIRECTORY, MODULES_LOG_FILE_NAME);

	// RASP 403.html
	public static final File RASP_FORBIDDEN_FILE = new File(RASP_DIRECTORY, FORBIDDEN_FILE);

	/**
	 * RASP核心配置对象
	 */
	public static final RASPPropertiesConfiguration<RASPAgentProperties> AGENT_CONFIG;

	/**
	 * rasp.properties配置文件内容
	 */
	public static final RASPAgentProperties AGENT_PROPERTIES;

	/**
	 * RASP站点管理员配置,rasp-rules.properties文件配置内容
	 */
	public static final RASPPropertiesConfiguration<RASPRuleProperties> AGENT_RULES_CONFIG;

	/**
	 * rasp-rules.properties配置文件内容
	 */
	public static final RASPRuleProperties AGENT_RULES_PROPERTIES;

	/**
	 * RASP Agent日志
	 */
	public static final Logger AGENT_LOGGER;

	/**
	 * RASP 防御模块日志
	 */
	public static final Logger MODULES_LOGGER;

	/**
	 * RASP 应用默认配置文件
	 */
	public static final File DEFAULT_CONFIG_FILE = new File(RASP_CONFIG_DIRECTORY, DEFAULT_AGENT_APP_FILE_NAME);

	/**
	 * 容器所有应用配置文件对象
	 */
	private static final Map<Integer, RASPPropertiesConfiguration<RASPAppProperties>> APPLICATION_CONFIG_MAP =
			new ConcurrentHashMap<Integer, RASPPropertiesConfiguration<RASPAppProperties>>();

	static {
		// 初始化加载RASP核心配置文件
		AGENT_CONFIG = loadConfig(AGENT_CONFIG_FILE_NAME, RASPAgentProperties.class);

		// 解析rasp.properties配置文件内容
		AGENT_PROPERTIES = AGENT_CONFIG.getRaspProperties();

		// 初始化加载RASP规则配置文件
		AGENT_RULES_CONFIG = loadConfig(AGENT_RULES_FILE_NAME, RASPRuleProperties.class);

		// 解析rasp-rules.properties配置文件内容
		AGENT_RULES_PROPERTIES = AGENT_RULES_CONFIG.getRaspProperties();

		// 获取RASP日志输出级别
		Level logLevel = Level.toLevel(AGENT_PROPERTIES.getLogLevel());

		// 创建agent日志Logger
		AGENT_LOGGER = createRASPLogger("agent", RASP_AGENT_FILE, logLevel);

		// 创建防御模块Logger
		MODULES_LOGGER = createRASPLogger("modules", RASP_MODULES_FILE, logLevel);
	}

	/**
	 * 返回传入文件目录对象，如果目录不存在会自动创建
	 *
	 * @param dir 目录
	 * @return 目录对象
	 */
	private static File getDirectory(File dir) {
		if (dir != null && !dir.exists()) {
			if (!dir.mkdir()) {
				if (AGENT_LOGGER != null) {
					AGENT_LOGGER.error("创建文件：{}失败！", dir);
				}
			}
		}

		return dir;
	}

	/**
	 * 加载properties配置文件
	 *
	 * @param file  配置文件名称
	 * @param clazz 配置类
	 * @return PropertiesConfiguration 配置文件对象
	 */
	private static <T extends RASPProperties> RASPPropertiesConfiguration<T> loadConfig(String file, Class<T> clazz) {
		// 配置文件
		File configFile = new File(RASP_CONFIG_DIRECTORY, file);

		RASPPropertiesConfiguration<T> configuration = null;

		try {
			configuration = new RASPPropertiesConfiguration<T>(configFile, clazz);
		} catch (Exception e) {
			if (AGENT_LOGGER != null) {
				AGENT_LOGGER.error("加载" + AGENT_NAME + "配置文件[" + configFile + "]异常: ", e);
			} else {
				e.printStackTrace();
			}
		}

		return configuration;
	}

	/**
	 * 获取Web应用配置文件对象，第一次获取时会初始化Web应用日志对象
	 *
	 * @param ctx RASP上下文
	 * @return Web应用配置文件对象
	 */
	public static RASPPropertiesConfiguration<RASPAppProperties> getWebApplicationConfig(RASPHttpRequestContext ctx) {
		String contextPath = ctx.getContextPath();

		// 处理contextPath为多级目录的情况，比如：/ibm/test/
		if (countOf(contextPath, '/') > 1) {
			// 替换/ibm/test/为/ibm_test/
			contextPath = replaceFirstChar(contextPath.replace("/", "_"), '/');
		}

		int hashCode = contextPath.hashCode();

		// 获取缓存中的Web应用配置
		RASPPropertiesConfiguration<RASPAppProperties> config = APPLICATION_CONFIG_MAP.get(hashCode);

		if (config != null) {
			return config;
		}

		// 初始化RASP访问日志和攻击日志对象
		ctx.initAppLogger();

		// 获取Web应用配置
		return getWebApplicationConfig(contextPath);
	}

	/**
	 * 获取Web应用配置文件
	 *
	 * @param contextPath 标准的contextPath
	 * @return Web应用配置文件对象
	 */
	public static RASPPropertiesConfiguration<RASPAppProperties> getWebApplicationConfig(String contextPath) {
		int hashCode = contextPath.hashCode();

		// 获取缓存中的Web应用配置
		RASPPropertiesConfiguration<RASPAppProperties> config = APPLICATION_CONFIG_MAP.get(hashCode);

		if (config != null) return config;

		String filename = contextPath + ".properties";

		if (filename.startsWith("/")) {
			filename = filename.substring(1).replace("/", "_");
		}

		File configFile = new File(RASP_APP_CONFIG_DIRECTORY, filename);

		RASPPropertiesConfiguration<RASPAppProperties> configuration = null;

		try {
			if (!DEFAULT_CONFIG_FILE.exists()) {
				AGENT_LOGGER.error("{}读取配置文件：{}不存在！", AGENT_NAME, DEFAULT_CONFIG_FILE);
				throw new RuntimeException();
			}

			if (!RASP_CONFIG_DIRECTORY.canWrite()) {
				AGENT_LOGGER.error("{}无权限修改配置文件目录：{}！", AGENT_NAME, DEFAULT_CONFIG_FILE);
				throw new RuntimeException();
			}

			if (!configFile.exists()) {
				copyFile(DEFAULT_CONFIG_FILE, configFile);
			}

			// 这里应该使用相对路径（如：apps/vul-test.properties），因为父级目录是：/rasp/config/
			String configRelativePath = new File(RASP_APP_CONFIG_DIRECTORY.getName(), filename).getPath();

			configuration = loadConfig(configRelativePath, RASPAppProperties.class);
		} catch (IOException e) {
			AGENT_LOGGER.error(AGENT_NAME + "读取" + contextPath + "配置文件[" + configFile + "]异常: ", e);
		}

		APPLICATION_CONFIG_MAP.put(hashCode, configuration);

		return configuration;
	}

}
