package org.javaweb.rasp.commons.log;

import com.google.gson.annotations.SerializedName;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.javaweb.rasp.commons.servlet.HttpServletRequestProxy;

import java.io.Serializable;
import java.util.TimeZone;

import static java.lang.System.currentTimeMillis;
import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_PROPERTIES;

public class RASPLog implements Serializable {

	@SerializedName("log_version")
	private final String logVersion;

	@SerializedName("app_id")
	private final String appId;

	@SerializedName("time")
	private final long time;

	@SerializedName("timezone")
	private final String timezone;

	/**
	 * 请求协议类型：http/https
	 */
	@SerializedName("scheme")
	private final String scheme;

	/**
	 * 请求的域名
	 */
	@SerializedName("domain")
	private final String domain;

	@SerializedName("context_path")
	private final String contextPath;

	/**
	 * 服务器端口
	 */
	@SerializedName("port")
	private final int port;

	@SerializedName("remote_ip")
	private final String remoteIp;

	@SerializedName("server_ip")
	private final String serverIp;

	/**
	 * 请求方式
	 */
	@SerializedName("request_method")
	private final String requestMethod;

	/**
	 * 请求URI
	 */
	@SerializedName("request_uri")
	private final String requestUri;

	/**
	 * 请求URL
	 */
	@SerializedName("request_url")
	private final String requestUrl;

	@SerializedName("query_string")
	private final String queryString;

	@SerializedName("document_root")
	private final String documentRoot;

	private static final String TIME_ZONE = TimeZone.getDefault().getID();

	public RASPLog(RASPHttpRequestContext context) {
		HttpServletRequestProxy request = context.getServletRequest();

		this.logVersion = AGENT_PROPERTIES.getLogVersion();
		this.appId = context.getAppProperties().getAppID();
		this.time = currentTimeMillis();
		this.timezone = TIME_ZONE;
		this.scheme = request.getScheme();
		this.domain = request.getServerName();
		this.contextPath = context.getContextPath();
		this.port = request.getServerPort();
		this.remoteIp = context.getRequestIP();
		this.serverIp = request.getLocalAddr();
		this.requestMethod = request.getMethod();
		this.requestUri = request.getRequestURI();
		this.requestUrl = request.getRequestURL().toString();
		this.documentRoot = context.getDocumentRoot().getAbsolutePath();
		this.queryString = request.getQueryString();
	}

	public String getLogVersion() {
		return logVersion;
	}

	public String getAppId() {
		return appId;
	}

	public long getTime() {
		return time;
	}

	public String getTimezone() {
		return timezone;
	}

	public String getScheme() {
		return scheme;
	}

	public String getDomain() {
		return domain;
	}

	public String getContextPath() {
		return contextPath;
	}

	public int getPort() {
		return port;
	}

	public String getRemoteIp() {
		return remoteIp;
	}

	public String getServerIp() {
		return serverIp;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public String getRequestUrl() {
		return requestUrl;
	}

	public String getQueryString() {
		return queryString;
	}

	public String getDocumentRoot() {
		return documentRoot;
	}

}
