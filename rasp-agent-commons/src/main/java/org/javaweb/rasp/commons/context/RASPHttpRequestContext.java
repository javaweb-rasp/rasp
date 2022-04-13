package org.javaweb.rasp.commons.context;

import org.javaweb.rasp.commons.MethodHookEvent;
import org.javaweb.rasp.commons.attack.RASPAttackInfo;
import org.javaweb.rasp.commons.cache.RASPCachedRequest;
import org.javaweb.rasp.commons.config.RASPAppProperties;
import org.javaweb.rasp.commons.config.RASPPropertiesConfiguration;
import org.javaweb.rasp.commons.servlet.HttpServletRequestProxy;
import org.javaweb.rasp.commons.servlet.HttpServletResponseProxy;
import org.slf4j.Logger;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.rasp.proxy.loader.RASPModuleType;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.lang.System.nanoTime;
import static org.javaweb.rasp.commons.config.RASPConfiguration.getWebApplicationConfig;
import static org.javaweb.rasp.commons.utils.HttpServletRequestUtils.getDocumentRootFile;
import static org.javaweb.rasp.commons.utils.HttpServletRequestUtils.getRemoteAddr;
import static org.javaweb.rasp.commons.utils.URLUtils.getStandardContextPath;

/**
 * HttpRequest上下文
 */
public abstract class RASPHttpRequestContext implements Closeable {

	/**
	 * RASP 应用配置对象
	 */
	protected final RASPPropertiesConfiguration<RASPAppProperties> applicationConfig;

	/**
	 * RASP 应用配置
	 */
	protected final RASPAppProperties appProperties;

	/**
	 * 记录当前context发生的攻击列表
	 */
	protected final Set<RASPAttackInfo> raspAttackInfoList = new LinkedHashSet<RASPAttackInfo>();

	/**
	 * request
	 */
	protected final HttpServletRequestProxy servletRequest;

	/**
	 * response
	 */
	protected final HttpServletResponseProxy servletResponse;

	/**
	 * 缓存的Servlet、Filter类实例
	 */
	protected final Object cacheClass;

	/**
	 * 静默模式
	 */
	protected final boolean silent;

	/**
	 * 网站根目录
	 */
	protected File documentRoot;

	/**
	 * 请求的文件绝对路径
	 */
	protected File requestFile;

	/**
	 * 获取最大缓存的Servlet输入输出流大小，默认10MB
	 */
	protected final int maxStreamCacheSize;

	/**
	 * RASP Servlet Adapter类加载器
	 */
	protected final ClassLoader adapterClassLoader;

	/**
	 * 缓存Http请求对象
	 */
	protected final RASPCachedRequest cachedRequest;

	/**
	 * 请求开始的纳秒
	 */
	protected final long requestStartNanoTime;

	/**
	 * Servlet路径
	 */
	protected final String servletPath;

	/**
	 * Web应用Context名称
	 */
	protected final String contextPath;

	/**
	 * 客户端IP
	 */
	protected final String requestIP;

	/**
	 * User-Agent
	 */
	protected final String userAgent;

	/**
	 * 是否需要过滤
	 */
	protected boolean mustFilter = true;

	/**
	 * 创建RASP Http请求context对象
	 *
	 * @param request            HttpRequest
	 * @param response           HttpResponse
	 * @param event              RASP 处理事件
	 * @param adapterClassLoader adapter类加载器
	 */
	public RASPHttpRequestContext(HttpServletRequestProxy request, HttpServletResponseProxy response,
	                              MethodHookEvent event, ClassLoader adapterClassLoader) {

		this.servletRequest = request;
		this.servletResponse = response;
		this.requestStartNanoTime = nanoTime();
		this.cacheClass = event.getThisObject();
		this.cachedRequest = new RASPCachedRequest(this);
		this.adapterClassLoader = adapterClassLoader;
		this.servletPath = request.getServletPath();
		this.userAgent = request.getHeader("User-Agent");
		this.contextPath = getStandardContextPath(request.getContextPath());

		// 获取Web应用配置，第一次请求的时候会比较耗时，因为初始化日志对象和配置文件
		this.applicationConfig = getWebApplicationConfig(this);
		this.appProperties = applicationConfig.getRaspProperties();
		this.requestIP = getRemoteAddr(request);
		this.silent = appProperties.isSilent();

		// 设置Servlet输入输出流大小，默认10MB
		this.maxStreamCacheSize = appProperties.getServletStreamMaxCacheSize();

		// 初始化context
		initContext();
	}

	/**
	 * 初始化context
	 */
	protected abstract void initContext();

	/**
	 * 获取RASP 包装后的request对象
	 *
	 * @return RASPHttpRequest
	 */
	public HttpServletRequestProxy getServletRequest() {
		return servletRequest;
	}

	/**
	 * 获取RASP 包装后的response对象
	 *
	 * @return RASPHttpResponse
	 */
	public HttpServletResponseProxy getServletResponse() {
		return servletResponse;
	}

	/**
	 * 获取缓存Http请求(Servlet、Filter)入口类对象
	 *
	 * @return 缓存Http请求入口的类实例
	 */
	public Object getCacheClass() {
		return cacheClass;
	}

	/**
	 * 获取当前context下存储的攻击详情
	 *
	 * @return 攻击集合
	 */
	public Set<RASPAttackInfo> getRaspAttackInfoList() {
		return raspAttackInfoList;
	}

	/**
	 * 添加攻击信息
	 *
	 * @param attack 攻击对象
	 */
	public abstract void addAttackInfo(RASPAttackInfo attack);

	/**
	 * 检查当前请求是否需要经过安全模块处理,如：非动态文件或白名单的情况下不需要加载任何安全检测模块
	 *
	 * @param moduleType 模块类型
	 * @return 返回是否需要过滤
	 */
	public abstract boolean mustFilter(RASPModuleType moduleType);

	/**
	 * 获取RASP Servlet Adapter类加载器
	 *
	 * @return RASP Servlet Adapter类加载器
	 */
	public ClassLoader getAdapterClassLoader() {
		return adapterClassLoader;
	}

	/**
	 * 检测是否是静默模式
	 *
	 * @return 是否是静默模式
	 */
	public boolean isSilent() {
		return silent;
	}

	/**
	 * 获取缓存的http请求对象
	 *
	 * @return 缓存请求对象
	 */
	public RASPCachedRequest getCachedRequest() {
		return cachedRequest;
	}

	/**
	 * 获取ServletPath
	 *
	 * @return servletPath
	 */
	public String getServletPath() {
		return servletPath;
	}

	/**
	 * 获取应用的Context名称
	 *
	 * @return Context名称
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * 获取客户端IP地址
	 *
	 * @return 客户端请求的IP地址
	 */
	public String getRequestIP() {
		return requestIP;
	}

	/**
	 * 获取User-Agent
	 *
	 * @return User-Agent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * 获取Web应用配置对象
	 *
	 * @return Web应用配置对象
	 */
	public RASPPropertiesConfiguration<RASPAppProperties> getApplicationConfig() {
		return applicationConfig;
	}

	public RASPAppProperties getAppProperties() {
		return appProperties;
	}

	/**
	 * 获取网站根目录
	 *
	 * @return 网站根目录
	 */
	public File getDocumentRoot() {
		if (documentRoot != null) {
			return documentRoot;
		}

		return documentRoot = getDocumentRootFile(this);
	}

	/**
	 * 获取请求的文件绝对路径
	 *
	 * @return 请求的文件绝对路径
	 */
	public File getRequestFile() {
		if (requestFile != null) {
			return requestFile;
		}

		return requestFile = new File(getDocumentRoot(), servletPath);
	}

	/**
	 * 获取最大缓存的流大小限制
	 *
	 * @return 最大缓存的流大小
	 */
	public int getMaxStreamCacheSize() {
		return maxStreamCacheSize;
	}

	/**
	 * 动态获取当前Web应用的Logger
	 *
	 * @param fileName     文件名
	 * @param loggerPrefix logger后缀
	 * @param fileSize     文件大小
	 * @return 当前Web应用Logger
	 */
	public abstract Logger getAppLogger(String fileName, String loggerPrefix, String fileSize);

	/**
	 * 获取RASP访问日志Logger对象
	 *
	 * @return 访问日志Logger对象
	 */
	public abstract Logger initAccessLogger();

	/**
	 * 获取RASP攻击日志Logger对象
	 *
	 * @return 攻击日志Logger对象
	 */
	public abstract Logger initAttackLogger();

	/**
	 * 初始化RASP访问日志和攻击日志对象
	 */
	public void initAppLogger() {
		// 初始化访问日志
		initAccessLogger();

		// 初始化共计日志
		initAttackLogger();
	}

	/**
	 * 返回是否是Web API请求
	 *
	 * @return 是否是Web API请求
	 */
	public abstract boolean isWebApiRequest();

	/**
	 * 返回是否是访问的jsp动态脚本文件
	 *
	 * @return 访问文件后缀是否是jsp
	 */
	public abstract boolean isJspFile();

	/**
	 * 返回是否是RASP内部API调用请求
	 *
	 * @return 是否是RASP内部API调用请求
	 */
	public abstract boolean isInternalAPIRequest();

	/**
	 * 返回是否是JSON请求
	 *
	 * @return 是否是JSON请求
	 */
	public abstract boolean isJsonRequest();

	/**
	 * 返回是否是XML请求
	 *
	 * @return 是否是JSON请求
	 */
	public abstract boolean isXmlRequest();

	/**
	 * 获取ContentLength
	 *
	 * @return ContentLength
	 */
	public abstract int getContentLength();

	/**
	 * 设置反序列化
	 */
	public abstract void setDeserializationStatus();

	/**
	 * 当前请求中是否包含反序列化行为
	 *
	 * @return 是否反序列化
	 */
	public abstract boolean isDeserialization();

	public void close() throws IOException {
		// 清除请求缓存数据
		cachedRequest.close();
	}

}
