package org.javaweb.rasp.commons.constants;

public class RASPRuleConstants {

	/**
	 * 文件上传不允许后缀
	 */
	public static final String UPLOAD_NOT_ALLOWED_SUFFIX = "upload.not_allowed_suffix";

	/**
	 * 读取文件正则表达式规则
	 */
	public static final String FILESYSTEM_READ_REGEXP = "filesystem.read.regexp";

	/**
	 * 写入文件正则表达式规则
	 */
	public static final String FILESYSTEM_WRITE_REGEXP = "filesystem.write.regexp";

	/**
	 * 敏感文件后缀
	 */
	public static final String FILESYSTEM_PROTECTED_SUFFIX = "filesystem.protected.suffix";

	/**
	 * SSRF协议规则
	 */
	public static final String SSRF_PROTOCOL_TYPES = "ssrf.protocol.types";

	/**
	 * SSRF黑名单域名列表
	 */
	public static final String SSRF_BLACK_DOMAIN = "ssrf.black.domain";

	/**
	 * JSON序列化禁用的类
	 */
	public static final String JSON_DISABLE_CLASS = "json.disable_class";

	/**
	 * Fastjson规则
	 */
	public static final String FASTJSON_REGEXP = "fastjson.regexp";

	/**
	 * XStream禁用的类
	 */
	public static final String XSTREAM_DISABLE_CLASS = "xstream.disable_class";

	/**
	 * WebShell 特征
	 */
	public static final String WEBSHELL_FEATURE = "webshell.feature";

	/**
	 * Ognl表达式正则
	 */
	public static final String EXPRESSION_OGNL = "expression.Ognl";

	/**
	 * Ognl禁止调用方法包名
	 */
	public static final String EXPRESSION_OGNL_EXCLUDED_PACKAGE_NAMES = "expression.Ognl.excludedPackageName";

	/**
	 * SpEL表达式正则
	 */
	public static final String EXPRESSION_SPEL = "expression.SpEL";

	/**
	 * MVEL2表达式正则
	 */
	public static final String EXPRESSION_MVEL2 = "expression.MVEL2";

	/**
	 * Java反序列化类名检测规则
	 */
	public static final String DESERIALIZATION = "deserialization";

	/**
	 * 扫描器规则列表
	 */
	public static final String SCANNER_USER_AGENT = "scanner.user-agent";

	/**
	 * 是否禁止本地命令执行
	 */
	public static final String DISABLE_CMD = "disable_cmd";

	/**
	 * 是否禁止脚本引擎解析
	 */
	public static final String DISABLE_SCRIPT_ENGINE = "disable_script_engine";

	/**
	 * 配置允许执行本地系统命令的类正则
	 */
	public static final String ALLOWED_CMD_CLASS_NAME = "allowed_cmd_class_name";

	/**
	 * 配置不允许执行本地系统命令的类正则
	 */
	public static final String DISALLOWED_CMD_CLASS_NAME = "disallowed_cmd_class_name";

	/**
	 * 设置是否禁止在Agent启动后修改任何jsp/jspx类型的动态脚本文件
	 */
	public static final String DISABLE_NEW_JSP = "disable_new_jsp";

}
