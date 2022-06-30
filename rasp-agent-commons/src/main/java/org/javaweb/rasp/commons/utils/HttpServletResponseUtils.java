package org.javaweb.rasp.commons.utils;

import org.javaweb.rasp.commons.attack.RASPAttackInfo;
import org.javaweb.rasp.commons.cache.RASPCachedRequest;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.javaweb.rasp.commons.servlet.HttpServletRequestProxy;
import org.javaweb.rasp.commons.servlet.HttpServletResponseProxy;

import java.io.OutputStream;
import java.io.Writer;
import java.rasp.proxy.loader.RASPModuleType;

import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;
import static org.javaweb.rasp.loader.AgentConstants.AGENT_NAME;
import static org.javaweb.rasp.commons.utils.HttpServletRequestUtils.htmlSpecialChars;
import static org.javaweb.rasp.commons.utils.StringUtils.isNotEmpty;

public class HttpServletResponseUtils {

	public static void responseJson(RASPHttpRequestContext context, String text) {
		response(context, "application/json;charset=UTF-8", text);
	}

	public static void responseJson(RASPHttpRequestContext context, Object obj) {
		response(context, "application/json;charset=UTF-8", JsonUtils.toJson(obj));
	}

	public static void responseXml(RASPHttpRequestContext context, String text) {
		response(context, "text/xml;charset=UTF-8", text);
	}

	public static void responseHTML(RASPHttpRequestContext context, String text) {
		response(context, "text/html;charset=UTF-8", text);
	}

	public static void responseText(RASPHttpRequestContext context, String text) {
		response(context, "text/plain;charset=UTF-8", text);
	}

	public static void accessDenied(RASPHttpRequestContext context, RASPAttackInfo attack, String text) {
		String         attackHash = attack.getAttackHash();
		RASPModuleType moduleType = attack.getRaspModuleType();

		if (isNotEmpty(text) && moduleType != null) {
			HttpServletRequestProxy request     = context.getServletRequest();
			String                  queryString = request.getQueryString();

			String url = request.getRequestURL().toString();

			if (queryString != null) {
				url += "?" + htmlSpecialChars(queryString);
			}

			String attackInfo = "{" +
					"'agent-name': '" + AGENT_NAME + "', 'attack-name': '" + moduleType.getModuleName() + "'," +
					"'request-url': '" + url + "', 'attack-desc': '" + moduleType.getModuleDesc() + "'," +
					"'attack-hash': '" + attackHash + "'" +
					"}";

			text = text.replace("$attackInfo", attackInfo);
		}

		response(context, "text/html;charset=UTF-8", text);
	}

	public static void response(RASPHttpRequestContext context, String contentType, String text) {
		RASPCachedRequest        cachedRequest = context.getCachedRequest();
		HttpServletResponseProxy response      = context.getServletResponse();
		Object                   output        = cachedRequest.getOutput();

		try {
			if (response == null || text == null) {
				return;
			}

			response.setContentType(contentType);

			if (output == null) {
				output = response.getWriter();
			}

			// JSP out
			if (output instanceof Writer) {
				Writer out = (Writer) output;
				out.write(text);
				out.flush();
				out.close();
			} else {
				OutputStream out = (OutputStream) output;
				out.write(text.getBytes());
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			AGENT_LOGGER.error(AGENT_NAME + "返回信息[" + text + "]异常:" + e, e);
		}
	}

}
