package org.javaweb.rasp.commons.cache;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * RASP 缓存请求对象封装,缓存请求中的各种参数信息
 * Creator: yz
 * Date: 2019-08-06
 */
public class RASPCachedRequest {

	/**
	 * 输入流
	 */
	private RASPOutputStreamCache inputStreamCache;

	/**
	 * 输出流
	 */
	private RASPOutputStreamCache outputStreamCache;

	/**
	 * 输出对象，可能是Writer或者OutputStream
	 */
	private Object output;

	/**
	 * 缓存SQL语句执行记录
	 */
	private final List<Integer> sqlHashCodes = new ArrayList<Integer>();

	/**
	 * RASP Http的参数缓存
	 */
	private final Set<RASPCachedParameter> raspCachedParameterList = new HashSet<RASPCachedParameter>();

//	/**
//	 * 解析queryString的参数，因为JEECMS会自己解析，而不是request.getParameter导致参数丢失
//	 *
//	 * @param parameter 缓存参数
//	 */
//	public void parseQueryString(RASPCachedParameter parameter) {
//		if (parameter.getValue().length != 1) return;
//
//		String queryString = parameter.getValue()[0];
//
//		try {
//			String   urlDecode = URLDecoder.decode(queryString, "UTF-8");
//			String[] args      = StringUtils.split(urlDecode, "&");
//
//			for (String str : args) {
//				if (str.length() > 1 && str.contains("=")) {
//					String[] arg = StringUtils.split(str, "=");
//
//					cacheRequestParameter(new RASPCachedParameter(arg[0], arg[1], QUERY_STRING));
//				}
//			}
//		} catch (UnsupportedEncodingException ignored) {
//		}
//	}

	/**
	 * 缓存被调用过的Http请求参数键
	 *
	 * @param parameter 缓存参数
	 */
	public void cacheRequestParameter(RASPCachedParameter parameter) {
		raspCachedParameterList.add(parameter);
	}

	/**
	 * 缓存SQL查询语句,用于避免SQL重复验证问题
	 *
	 * @param hashcode SQL hashcode
	 */
	public void cacheSqlHashCode(int hashcode) {
		this.sqlHashCodes.add(hashcode);
	}

	public boolean containsSQLQueryCache(int hashcode) {
		return sqlHashCodes.contains(hashcode);
	}

	/**
	 * 组装所有来自客户端的请求参数,包含:Parameter、QueryString、Cookies、Header、Multipart、ParameterMap
	 *
	 * @return 返回缓存在RASP上下文中的所有参数
	 */
	public Set<RASPCachedParameter> getCachedParameter() {
		return raspCachedParameterList;
	}

	public RASPOutputStreamCache getInputStreamCache() {
		return inputStreamCache;
	}

	public RASPOutputStreamCache getOutputStreamCache() {
		return outputStreamCache;
	}

	public void initInputStreamCache(RASPOutputStreamCache out) {
		this.inputStreamCache = out;
	}

	public void intOutputStreamCache(RASPOutputStreamCache out) {
		this.outputStreamCache = out;
	}

	public Object getOutput() {
		return output;
	}

	public void setOutput(Object output) {
		this.output = output;
	}

}
