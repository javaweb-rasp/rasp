package org.javaweb.rasp.commons.context;

import java.io.IOException;

import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;
import static org.javaweb.rasp.loader.AgentConstants.AGENT_NAME;

public abstract class RASPHttpRequestContextManager {

	/**
	 * Http请求上下文
	 */
	private static final ThreadLocal<RASPHttpRequestContext> RASP_CONTEXT = new ThreadLocal<RASPHttpRequestContext>();

	/**
	 * 获取当前线程中的Http请求上下文
	 *
	 * @return RASP上下文
	 */
	public static RASPHttpRequestContext getContext() {
		return RASP_CONTEXT.get();
	}

	public static void setContext(RASPHttpRequestContext context) {
		RASP_CONTEXT.set(context);

		if (AGENT_LOGGER.isDebugEnabled()) {
			AGENT_LOGGER.debug("{}创建RASPContext 成功！请求路径：{}", AGENT_NAME, context.getServletPath());
		}
	}

	/**
	 * 清除Context
	 */
	protected static void removeContext(RASPHttpRequestContext context) {
		if (context == null) {
			return;
		}

		if (AGENT_LOGGER.isDebugEnabled()) {
			AGENT_LOGGER.debug("{}正在清除RASPContext，请求路径：{}", AGENT_NAME, context.getServletPath());
		}

		RASP_CONTEXT.remove();

		try {
			context.close();
		} catch (IOException e) {
			AGENT_LOGGER.error("{}关闭RASP上下文时异常：" + e, AGENT_NAME);
		}
	}

	/**
	 * 检测当前线程中是否存在HTTP请求
	 *
	 * @return 是否存在HTTP请求
	 */
	public static boolean hasRequestContext() {
		RASPHttpRequestContext context = getContext();

		return context != null && context.getServletRequest() != null && context.getServletResponse() != null;
	}

}
