package org.javaweb.rasp.commons.utils;

import com.dslplatform.json.Nullable;
import com.dslplatform.json.NumberConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.javaweb.rasp.commons.cache.RASPCachedParameter;
import org.javaweb.rasp.commons.cache.RASPCachedRequest;
import org.javaweb.rasp.commons.cache.RASPParameterSet;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;

import java.rasp.proxy.loader.HookResult;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static org.javaweb.rasp.commons.attack.RASPParameterPosition.JSON;
import static org.javaweb.rasp.commons.constants.RASPConstants.DEFAULT_HOOK_RESULT;
import static java.rasp.proxy.loader.HookResultType.RETURN;

/**
 * Created by yz on 2017/2/20.
 *
 * @author yz
 */
public class JsonUtils {

	private static final JsonObjectTypeAdapter TYPE_ADAPTER = new JsonObjectTypeAdapter();

	public static final Gson GSON = new GsonBuilder().disableHtmlEscaping().
			registerTypeAdapter(List.class, TYPE_ADAPTER).
			registerTypeAdapter(Map.class, TYPE_ADAPTER).
			create();

	public static String toJson(Object src) {
		return GSON.toJson(src);
	}

	private static <T> T fromJson(Object object, Type typeOfT) {
		String json;

		if (object instanceof String) {
			json = (String) object;
		} else {
			json = toJson(object);
		}

		return GSON.fromJson(json, typeOfT);
	}

	public static Map<String, Object> toJsonMap(Object object) {
		if (object != null) {
			return fromJson(object, new TypeToken<Map<String, Object>>() {
			}.getType());
		}

		return new HashMap<String, Object>();
	}

	public static Set<Map<String, Object>> toJsonSetMap(Object object) {
		if (object != null) {
			return fromJson(object, new TypeToken<Set<Map<String, Object>>>() {
			}.getType());
		}

		return new HashSet<Map<String, Object>>();
	}

	/**
	 * 适配JSON序列化数据类型:https://stackoverflow.com/questions/36508323/how-can-i-prevent-gson-from-converting-integers-to-doubles
	 */
	public static final class JsonObjectTypeAdapter extends TypeAdapter<Object> {

		private final TypeAdapter<Object> delegate = new Gson().getAdapter(Object.class);

		@Override
		public Object read(JsonReader in) throws IOException {
			JsonToken token = in.peek();

			switch (token) {
				case BEGIN_ARRAY:
					List<Object> list = new ArrayList<Object>();
					in.beginArray();

					while (in.hasNext()) {
						list.add(read(in));
					}

					in.endArray();
					return list;

				case BEGIN_OBJECT:
					Map<String, Object> map = new LinkedTreeMap<String, Object>();
					in.beginObject();

					while (in.hasNext()) {
						map.put(in.nextName(), read(in));
					}

					in.endObject();
					return map;

				case STRING:
					return in.nextString();

				case NUMBER:
					Number num = in.nextDouble();

					if (Math.ceil(num.doubleValue()) == num.longValue())
						return num.longValue();
					else {
						return num.doubleValue();
					}

				case BOOLEAN:
					return in.nextBoolean();

				case NULL:
					in.nextNull();
					return null;

				default:
					throw new IllegalStateException();
			}
		}

		@Override
		public void write(JsonWriter out, Object value) throws IOException {
			if (value == null) {
				out.nullValue();

				return;
			}

			delegate.write(out, value);
		}

	}

	public static HookResult<?> deserializeObject(final com.dslplatform.json.JsonReader<Object> reader,
	                                              final RASPHttpRequestContext context) throws IOException {

		return deserializeObject(reader, context, null);
	}

	@Nullable
	public static HookResult<?> deserializeObject(final com.dslplatform.json.JsonReader<Object> reader,
	                                              final RASPHttpRequestContext context,
	                                              String key) throws IOException {

		switch (reader.last()) {
			case 'n':
				if (!reader.wasNull()) {
					throw reader.newParseErrorAt("Expecting 'null' for null constant", 0);
				}

				return DEFAULT_HOOK_RESULT;
			case 't':
				if (!reader.wasTrue()) {
					throw reader.newParseErrorAt("Expecting 'true' for true constant", 0);
				}

				return DEFAULT_HOOK_RESULT;
			case 'f':
				if (!reader.wasFalse()) {
					throw reader.newParseErrorAt("Expecting 'false' for false constant", 0);
				}

				return DEFAULT_HOOK_RESULT;
			case '"':
				RASPCachedRequest request = context.getCachedRequest();
				RASPParameterSet<RASPCachedParameter> cachedParameters = request.getCachedParameter();
				RASPCachedParameter parameter = new RASPCachedParameter(key, reader.readString(), JSON);

				return cachedParameters.cacheParameter(parameter);
			case '{':
				return deserializeMap(reader, context);
			case '[':
				deserializeList(reader, context);
				return DEFAULT_HOOK_RESULT;
			default:
				NumberConverter.deserializeNumber(reader);
				return DEFAULT_HOOK_RESULT;
		}
	}

	public static HookResult<?> deserializeList(final com.dslplatform.json.JsonReader<Object> reader,
	                                            final RASPHttpRequestContext context) throws IOException {

		if (reader.last() != '[') throw reader.newParseError("Expecting '[' for list start");
		byte nextToken = reader.getNextToken();
		if (nextToken == ']') {
			return DEFAULT_HOOK_RESULT;
		}

		HookResult<?> result = deserializeObject(reader, context);

		if (result != null && result.getRASPHookResultType() != RETURN) {
			return result;
		}

		while ((nextToken = reader.getNextToken()) == ',') {
			reader.getNextToken();
			result = deserializeObject(reader, context);

			if (result != null && result.getRASPHookResultType() != RETURN) {
				return result;
			}
		}

		if (nextToken != ']') throw reader.newParseError("Expecting ']' for list end");
		return result;
	}

	public static HookResult<?> deserializeMap(final com.dslplatform.json.JsonReader<Object> reader,
	                                           final RASPHttpRequestContext context) throws IOException {

		if (reader.last() != '{') throw reader.newParseError("Expecting '{' for map start");
		byte nextToken = reader.getNextToken();

		if (nextToken == '}') {
			return DEFAULT_HOOK_RESULT;
		}

		String        key    = reader.readKey();
		HookResult<?> result = deserializeObject(reader, context, key);

		if (result != null && result.getRASPHookResultType() != RETURN) {
			return result;
		}

		while ((nextToken = reader.getNextToken()) == ',') {
			reader.getNextToken();
			key = reader.readKey();

			result = deserializeObject(reader, context, key);

			if (result != null && result.getRASPHookResultType() != RETURN) {
				return result;
			}
		}

		if (nextToken != '}') throw reader.newParseError("Expecting '}' for map end");

		return result;
	}

}
