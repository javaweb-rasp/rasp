package org.javaweb.rasp.commons.config;

import static org.javaweb.rasp.commons.constants.RASPRuleConstants.*;

public class RASPRuleProperties extends RASPProperties {

	private String[] uploadNotAllowedSuffix;

	private String[] filesystemReadRegexp;

	private String[] filesystemWriteRegexp;

	private String[] filesystemProtectedSuffix;

	private String[] ssrfProtocolTypes;

	private String[] ssrfBlackDomain;

	private String[] jsonDisableClass;

	private String fastjsonRegexp;

	private String[] xstreamDisableClass;

	private String webShellFeature;

	private String webShellConfig;

	private String expressionOgnl;

	private String[] expressionOgnlExcludedPackageNames;

	private String expressionSpel;

	private String expressionMvel2;

	private String[] deserialization;

	private String[] scannerUserAgent;

	private boolean disableCmd;

	private String[] allowedCmdClassName;

	private String[] disallowedCmdClassName;

	private boolean disableNewJsp;

	private String dataBinderDisableFieldRegexp;

	public void reloadConfig(RASPConfigMap<String, Object> configMap) {
		super.reloadConfig(configMap);

		this.uploadNotAllowedSuffix = configMap.getArray(UPLOAD_NOT_ALLOWED_SUFFIX);
		this.filesystemReadRegexp = configMap.getArray(FILESYSTEM_READ_REGEXP);
		this.filesystemWriteRegexp = configMap.getArray(FILESYSTEM_WRITE_REGEXP);
		this.filesystemProtectedSuffix = configMap.getArray(FILESYSTEM_PROTECTED_SUFFIX);
		this.ssrfProtocolTypes = configMap.getArray(SSRF_PROTOCOL_TYPES);
		this.ssrfBlackDomain = configMap.getArray(SSRF_BLACK_DOMAIN);
		this.jsonDisableClass = configMap.getArray(JSON_DISABLE_CLASS);
		this.fastjsonRegexp = configMap.getString(FASTJSON_REGEXP);
		this.xstreamDisableClass = configMap.getArray(XSTREAM_DISABLE_CLASS);
		this.webShellFeature = configMap.getString(WEBSHELL_FEATURE);
		this.webShellConfig = configMap.getString(WEBSHELL_CONFIG);
		this.expressionOgnl = configMap.getString(EXPRESSION_OGNL);
		this.expressionOgnlExcludedPackageNames = configMap.getArray(EXPRESSION_OGNL_EXCLUDED_PACKAGE_NAMES);
		this.expressionSpel = configMap.getString(EXPRESSION_SPEL);
		this.expressionMvel2 = configMap.getString(EXPRESSION_MVEL2);
		this.deserialization = configMap.getArray(DESERIALIZATION);
		this.scannerUserAgent = configMap.getArray(SCANNER_USER_AGENT);
		this.disableCmd = configMap.getBoolean(DISABLE_CMD, false);
		this.allowedCmdClassName = configMap.getArray(ALLOWED_CMD_CLASS_NAME);
		this.disallowedCmdClassName = configMap.getArray(DISALLOWED_CMD_CLASS_NAME);
		this.disableNewJsp = configMap.getBoolean(DISABLE_NEW_JSP, false);
		this.dataBinderDisableFieldRegexp = configMap.getString(DATA_BINDER_DISABLE_FIELD_REGEXP);
	}

	public String[] getUploadNotAllowedSuffix() {
		return uploadNotAllowedSuffix;
	}

	public String[] getFilesystemReadRegexp() {
		return filesystemReadRegexp;
	}

	public String[] getFilesystemWriteRegexp() {
		return filesystemWriteRegexp;
	}

	public String[] getFilesystemProtectedSuffix() {
		return filesystemProtectedSuffix;
	}

	public String[] getSsrfProtocolTypes() {
		return ssrfProtocolTypes;
	}

	public String[] getSsrfBlackDomain() {
		return ssrfBlackDomain;
	}

	public String[] getJsonDisableClass() {
		return jsonDisableClass;
	}

	public String getFastjsonRegexp() {
		return fastjsonRegexp;
	}

	public String[] getXstreamDisableClass() {
		return xstreamDisableClass;
	}

	public String getWebShellFeature() {
		return webShellFeature;
	}

	public String getWebShellConfig() {
		return webShellConfig;
	}

	public String getExpressionOgnl() {
		return expressionOgnl;
	}

	public String[] getExpressionOgnlExcludedPackageNames() {
		return expressionOgnlExcludedPackageNames;
	}

	public String getExpressionSpel() {
		return expressionSpel;
	}

	public String getExpressionMvel2() {
		return expressionMvel2;
	}

	public String[] getDeserialization() {
		return deserialization;
	}

	public String[] getScannerUserAgent() {
		return scannerUserAgent;
	}

	public boolean isDisableCmd() {
		return disableCmd;
	}

	public String[] getAllowedCmdClassName() {
		return allowedCmdClassName;
	}

	public String[] getDisallowedCmdClassName() {
		return disallowedCmdClassName;
	}

	public boolean isDisableNewJsp() {
		return disableNewJsp;
	}

	public String getDataBinderDisableFieldRegexp() {
		return dataBinderDisableFieldRegexp;
	}

}
