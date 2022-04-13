package org.javaweb.rasp.commons.cache;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonReader;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;

import java.io.IOException;
import java.rasp.proxy.loader.HookResult;

import static org.javaweb.rasp.commons.constants.RASPConstants.DEFAULT_HOOK_RESULT;
import static org.javaweb.rasp.commons.utils.JsonUtils.deserializeObject;
import static org.javaweb.rasp.commons.utils.IOUtils.closeQuietly;

public class RASPSerialization {

	private static final DslJson<Object> DSL_JSON = new DslJson<Object>();

	public static HookResult<?> serialization(RASPHttpRequestContext context, RASPByteArrayInputStream in) {
		try {
			if (in == null || in.available() == 0) {
				return DEFAULT_HOOK_RESULT;
			}

			if (context.isJsonRequest()) {
				return jsonDecode(context, in);
			}

			// TODO: 暂时不处理xml反序列化
			if (context.isXmlRequest()) {
				return xmlDecode(context, in);
			}
		} catch (Exception ignored) {
		}

		return DEFAULT_HOOK_RESULT;
	}

	private static HookResult<?> xmlDecode(final RASPHttpRequestContext context, final RASPByteArrayInputStream in) {

		try {
			context.getCachedRequest().cacheRequestXMLData(in);
		} finally {
			if (in != null)
				closeQuietly(in);
		}

		return DEFAULT_HOOK_RESULT;
	}

	/**
	 * JSON序列化
	 *
	 * @param context RASP上下文
	 * @param in      请求输入流
	 */
	private static HookResult<?> jsonDecode(final RASPHttpRequestContext context,
	                                        RASPByteArrayInputStream in) throws IOException {

		try {
			return (HookResult<?>) DSL_JSON.newReader(in, new byte[64]).next(new JsonReader.ReadObject<Object>() {
				@Override
				public Object read(JsonReader reader) throws IOException {
					return deserializeObject(reader, context);
				}
			});
		} finally {
			closeQuietly(in);
		}
	}

}
