package org.javaweb.rasp.commons.utils;

import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.javaweb.rasp.commons.servlet.HttpServletRequestProxy;
import org.javaweb.rasp.commons.servlet.HttpServletResponseProxy;

import java.io.File;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_PROPERTIES;
import static org.javaweb.rasp.commons.utils.IPV4Utils.*;

/**
 * Created by yz on 2017/1/17.
 *
 * @author yz
 */
public class HttpServletRequestUtils {

	private static final String TMP_DIR = System.getProperty("java.io.tmpdir");

	/**
	 * 缓存Web应用ContextPath
	 */
	private static final Map<Integer, File> DOCUMENT_ROOT_MAP = new ConcurrentHashMap<Integer, File>();

	/**
	 * 获取web目录,Weblogic 默认以war包部署的时候不能用getRealPath,xxx.getResource("/")获取
	 * 的是当前应用所在的类路径，截取到WEB-INF之后的路径就是当前应用的web根目录了
	 *
	 * @param context RASP上下文
	 * @return Web根目录
	 */
	public static File getDocumentRootFile(RASPHttpRequestContext context) {
		String                  contextPath  = context.getContextPath();
		HttpServletRequestProxy request      = context.getServletRequest();
		int                     contextHash  = contextPath.hashCode();
		File                    documentRoot = DOCUMENT_ROOT_MAP.get(contextHash);

		if (documentRoot != null) {
			return documentRoot;
		}

		String webRoot = request.getRealPath("/");

		// 排除SpringBoot默认使用的/tmp/目录
		if (webRoot != null && !"".equals(webRoot) && isTempDir(webRoot)) {
			return new File(webRoot);
		}

		try {
			// 处理Servlet 3+，无法获取getRealPath的情况，如：Weblogic
			URL resource = request.getServletContext().getResource("/");

			if (resource != null && isTempDir(resource.getFile())) return new File(resource.getFile());
		} catch (Throwable t) {
			try {
				// Servlet 2.x，request.getSession()可能会导致shiro的session无法获取，
				URL resource = request.getSession().getServletContext().getResource("/");

				if (resource != null && isTempDir(resource.getFile())) return new File(resource.getFile());
			} catch (Throwable ignored) {
			}
		}

		documentRoot = getWebRoot(request.getClass().getClassLoader());

		// 缓存Web应用路径
		DOCUMENT_ROOT_MAP.put(contextHash, documentRoot);

		return documentRoot;
	}

	public static boolean isTempDir(String dir) {
		return !dir.startsWith(TMP_DIR);
	}

	public static File getWebRoot(ClassLoader loader) {
		String webRoot = null;

		try {
			URL resource = loader.getResource("/");

			// getResource("/")可能会获取不到Resource
			if (resource == null) {
				resource = loader.getResource("");
			}

			if (resource != null) {
				if ("jar".equals(resource.getProtocol())) {
					webRoot = resource.getPath();

					webRoot = webRoot.substring(webRoot.indexOf(":") + 1, webRoot.lastIndexOf("!/"));
				} else {
					webRoot = resource.getPath();
				}
			}

			if (webRoot != null) {
				return new File(webRoot).getParentFile();
			}
		} catch (Exception ignored) {
		}

		// 如果上面的方法仍无法获取Web目录，以防万一返回一个当前文件路径
		return new File("").getAbsoluteFile();
	}

	/**
	 * 如果经过nginx反向代理后可能会获取到一个本地的IP地址如:127.0.0.1、192.168.1.100
	 * 配置nginx把客户端真实IP地址放到nginx请求头中的x-real-ip或x-forwarded-for的值
	 *
	 * @param request 请求对象
	 * @return 获取客户端IP
	 */
	public static String getRemoteAddr(HttpServletRequestProxy request) {
		String ip = request.getRemoteAddr();

		// 如果IP地址为空或者IP是本机、内网地址，需要解析请求头中的IP
		if (isLanIP(ip)) {
			String ipKey   = AGENT_PROPERTIES.getProxyIpHeader();
			String proxyIP = request.getHeader(ipKey);

			if (proxyIP != null) {
				if (textToNumericFormatV4(proxyIP) != null) return proxyIP;
				if (textToNumericFormatV6(proxyIP) != null) return proxyIP;
			}
		}

		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**
	 * 获取Http请求头对象
	 *
	 * @param request 请求对象
	 * @return 请求头Map
	 */
	public static Map<String, String> getRequestHeaderMap(HttpServletRequestProxy request) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Enumeration<String> e   = request.getHeaderNames();

		while (e.hasMoreElements()) {
			String name = e.nextElement();
			map.put(name, request.getHeader(name));
		}

		return map;
	}

	/**
	 * 获取Http请求头对象
	 *
	 * @param response 响应对象
	 * @return 响应头Map
	 */
	public static Map<String, String> getResponseHeaderMap(HttpServletResponseProxy response) {
		Map<String, String> map     = new LinkedHashMap<String, String>();
		Collection<String>  headers = response.getHeaderNames();

		if (headers != null) {
			for (String name : headers) {
				map.put(name, response.getHeader(name));
			}
		}

		return map;
	}

	/**
	 * 实现htmlSpecialChars函数把一些预定义的字符转换为HTML实体编码
	 *
	 * @param content 输入的字符串内容
	 * @return HTML实体化转义后的字符串
	 */
	public static String htmlSpecialChars(String content) {
		if (content == null) {
			return null;
		}

		char[]        charArray = content.toCharArray();
		StringBuilder sb        = new StringBuilder();

		for (char c : charArray) {
			switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&#039;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				default:
					sb.append(c);
					break;
			}
		}

		return sb.toString();
	}

}
