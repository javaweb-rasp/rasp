package org.javaweb.rasp.commons.config;

import java.util.*;

import static org.javaweb.rasp.loader.AgentConstants.AGENT_NAME;
import static org.javaweb.rasp.commons.utils.StringUtils.*;

public class RASPConfigMap<K, V> extends HashMap<K, V> {

	public int getInt(K key) {
		return getInteger(key, null);
	}

	public int getInt(K key, int defaultValue) {
		return getInteger(key, defaultValue);
	}

	public int getInteger(K key, Integer defaultValue) {
		String value = getString(key);

		if (isNotEmpty(value)) {
			return Integer.parseInt(value);
		} else if (defaultValue != null) {
			return defaultValue;
		}

		throw new RuntimeException(AGENT_NAME + "读取配置属性：" + key + "不存在！");
	}

	public long getLong(K key) {
		return getLong(key, null);
	}

	public Long getLong(K key, Long defaultValue) {
		String value = getString(key);

		if (isNotEmpty(value)) {
			return Long.parseLong(value);
		} else if (defaultValue != null) {
			return defaultValue;
		}

		throw new RuntimeException(AGENT_NAME + "读取配置属性：" + key + "不存在！");
	}

	public Boolean getBoolean(K key, Boolean defaultValue) {
		String value = getString(key);

		if (isNotEmpty(value)) {
			return equalIgnoreCase("true", value);
		} else if (defaultValue != null) {
			return defaultValue;
		}

		throw new RuntimeException(AGENT_NAME + "读取配置属性：" + key + "不存在！");
	}

	public String getString(K key) {
		return getString(key, null);
	}

	public String getString(K key, String defaultValue) {
		String value = getValue(key);

		if (isNotEmpty(value)) {
			return value;
		} else if (defaultValue != null) {
			return defaultValue;
		}

		return "";
	}

	public Set<String> getSet(K key) {
		return getSet(key, new HashSet<String>());
	}

	public Set<String> getSet(K key, Set<String> defaultValue) {
		List<String> list = getList(key);

		return new HashSet<String>(list);
	}

	public String[] getArray(K key) {
		String value = getValue(key);

		if (isNotEmpty(value)) {
			return split(value, ",");
		}

		return new String[0];
	}

	/**
	 * 返回key对应的List，如果值不存在将会返回一个空的集合，切勿操作返回的空集合，因为该集合是公用的
	 *
	 * @param key 配置key
	 * @return 配置的值集合
	 */
	public List<String> getList(K key) {
		return getList(key, new ArrayList<String>());
	}

	@SuppressWarnings("unchecked")
	public List<String> getList(K key, List<String> defaultValue) {
		Object obj = get(key);

		if (obj instanceof List) {
			return (List<String>) obj;
		}

		String[] array = getArray(key);

		if (array.length == 0) {
			return defaultValue;
		}

		return Arrays.asList(array);
	}

	protected String getValue(K key) {
		Object obj = get(key);

		if (obj instanceof List) {
			return join((List) obj, ',');
		}

		return (String) obj;
	}

	public boolean valueSearch(K key, String search) {
		String value = getString(key);

		if (value == null) return false;
		if (value.equals(search)) return true;

		// 找到搜索的字符串位置
		int index = value.indexOf(search);

		if (index > -1) {
			int valLen     = value.length();
			int afterIndex = index + search.length();

			if (index == 0) {
				// 向后找到一个,或者后面没有任何字符
				return afterIndex == valLen || value.charAt(search.length()) == ',';
			} else {
				// 向前查找一个,
				if (value.charAt(index - 1) == ',') {
					// 向后找到一个,或者后面没有任何字符
					return afterIndex == valLen || value.charAt(afterIndex) == ',';
				}
			}
		}

		return false;
	}

}
