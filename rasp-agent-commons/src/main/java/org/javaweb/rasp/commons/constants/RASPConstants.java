package org.javaweb.rasp.commons.constants;

import org.javaweb.rasp.commons.loader.hooks.HookResult;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.DOTALL;
import static org.javaweb.rasp.commons.loader.hooks.HookResultType.RETURN;

/**
 * RASP 全局常量定义
 */
public class RASPConstants {

	/**
	 * 指定ASM版本号：ASM 9
	 */
	public static final int ASM_VERSION = 9 << 16;

	/**
	 * 字符串编码类型
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * Class OPCode
	 */
	public static final int ACC_INTERFACE = 0x0200;

	/**
	 * 正则表达式多行忽略大小写匹配
	 */
	public static final int MULTIPLE_LINE_CASE_INSENSITIVE = DOTALL | CASE_INSENSITIVE;

	/**
	 * 通用的白名单过滤正则表达式
	 */
	public static final Pattern WHITELIST_PATTERN = Pattern.compile("^[a-zA-Z0-9_$.]+$");

	/**
	 * 类构造方法
	 */
	public static final String CONSTRUCTOR_INIT = "<init>";

	/**
	 * Hook 默认的返回功能
	 */
	public static final HookResult<?> DEFAULT_HOOK_RESULT = new HookResult<Object>(RETURN);

	/**
	 * RASP git配置文件名称
	 */
	public static final String GIT_FILE_NAME = "git.properties";

	/**
	 * API请求参数名
	 */
	public static final String RASP_API_CONNECT_DATA = "__ConnectData__";

	/**
	 * 字符串：javassist，因为maven配置了javassist关键字重定向，所以这里不能直接写"javassist"
	 */
	public static final String JAVASSIST = new String(new byte[]{106, 97, 118, 97, 115, 115, 105, 115, 116});

	/**
	 * 默认不需要ASM处理的Java包或类名称
	 */
	public final static String DEFAULT_PROTECTED_PACKAGE_REGEXP = "" +
			"(java\\.(security|util)\\.|" +
			"java\\.lang\\.(invoke|ref|concurrent|instrument)|" +
			"java\\.lang\\.(Object|String|Shutdown|ThreadLocal|WeakPairMap\\b.*)$|" +
			"javax\\.crypto|sun\\.(security|misc|net)|" +
			"org\\.apache\\.commons\\.(io|lang|logging|configuration)\\.|" +
			"org\\.objectweb\\.asm\\.|com\\.google\\.gson\\.|" +
			"\\$\\$(FastClassBySpringCGLIB|Lambda|EnhancerBySpringCGLIB)\\$)";

	/**
	 * 拒绝访问时的拦截页面文件名
	 */
	public static final String FORBIDDEN_FILE = "403.html";

	/**
	 * jsp
	 */
	public static final String JSP_SUFFIX = "jsp";

	/**
	 * JSON类型Content-Type
	 */
	public static final String APPLICATION_JSON_VALUE = "application/json";

	/**
	 * xml类型Content-Type
	 */
	public static final String APPLICATION_XML_VALUE = "application/xml";

}