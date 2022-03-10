package org.javaweb.rasp.commons.cache;

import com.dslplatform.json.DslJson;
import com.dslplatform.json.JsonReader;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import static org.javaweb.rasp.commons.context.RASPHttpRequestContextManager.getContext;
import static org.javaweb.rasp.commons.context.RASPHttpRequestContextManager.hasRequestContext;
import static org.javaweb.rasp.commons.utils.JsonUtils.deserializeObject;

public class RASPSerialization {

	private static final DslJson<Object> DSL_JSON = new DslJson<Object>();

	/**
	 * 请求参数序列化
	 *
	 * @param in 输入流
	 */
	public static void serialization(InputStream in) {
		if (in == null || !hasRequestContext()) {
			return;
		}

		RASPHttpRequestContext   context          = getContext();
		RASPCachedRequest        cachedRequest    = context.getCachedRequest();
		Set<RASPCachedParameter> cachedParameters = cachedRequest.getCachedParameter();

		// 序列化JSON字符串
		if (context.isJsonRequest()) {
			jsonDecode(cachedParameters, in);
		}
	}

	private static void xmlDecode(InputStream in, Set<RASPCachedParameter> cachedParameters) {

	}

	/**
	 * JSON序列化
	 *
	 * @param cachedParameters RASP缓存的请求参数
	 * @param in               请求输入流
	 */
	private static void jsonDecode(final Set<RASPCachedParameter> cachedParameters, InputStream in) {
		try {
			JsonReader<Object> reader = DSL_JSON.newReader(in, new byte[64]);

			reader.next(new JsonReader.ReadObject<Object>() {
				@Override
				public Object read(com.dslplatform.json.JsonReader reader) throws IOException {
					deserializeObject(reader, cachedParameters);
					return null;
				}
			});
		} catch (Exception ignored) {
		}
	}

}
