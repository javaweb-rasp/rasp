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
	 * WebShell 特征（旧）
	 */
	public static final String WEBSHELL_FEATURE = "webshell.feature";

	/**
	 * WebShell 特征（新）
	 */
	public static final String WEBSHELL_CONFIG = "webshell.config";

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

	/**
	 * 设置禁止被DataBinder加载的field正则表达式
	 */
	public static final String DATA_BINDER_DISABLE_FIELD_REGEXP = "data_binder_disable_field_regexp";

}
