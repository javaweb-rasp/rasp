package org.javaweb.rasp.commons.config;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.javaweb.rasp.commons.constants.RASPConstants.DEFAULT_ENCODING;
import static org.javaweb.rasp.commons.utils.FileUtils.readLines;
import static org.javaweb.rasp.commons.utils.FileUtils.writeStringToFile;

public class RASPPropertiesConfiguration<T extends RASPProperties> {

	/**
	 * 配置文件的最后修改时间，默认和被删除后为0
	 */
	private long lastModified;

	/**
	 * 配置文件
	 */
	private final File configFile;

	private final T raspProperties;

	public RASPPropertiesConfiguration(File configFile, Class<T> configClass) throws Exception {
		this.configFile = configFile;
		this.raspProperties = configClass.newInstance();

		raspProperties.reloadConfig(readProperties(configFile));
	}

	public File getConfigFile() {
		return configFile;
	}

	public T getRaspProperties() {
		return raspProperties;
	}

	public String getConfigName() {
		String appConfigName = configFile.getName();

		return appConfigName.substring(0, appConfigName.lastIndexOf("."));
	}

	/**
	 * 修改配置文件属性
	 *
	 * @param configMap 需要修改的Map
	 * @throws IOException 修改文件时异常
	 */
	public synchronized void setProperty(Map<String, String> configMap) throws IOException {
		// 修改属性
		writeProperties(configFile, configMap);

		raspProperties.reloadConfig(readProperties(configFile));
	}

	/**
	 * 使用非标准的方式解析Properties文件配置，仅能应用于RASP
	 *
	 * @param configFile 配置文件路径
	 * @return 解析后的配置文件Map
	 * @throws IOException IO异常
	 */
	public static synchronized RASPConfigMap<String, Object> readProperties(File configFile) throws IOException {
		RASPConfigMap<String, Object> configMap = new RASPConfigMap<String, Object>();
		List<String>                  lines     = readLines(configFile, DEFAULT_ENCODING);

		for (String line : lines) {
			if (line.length() > 0) {
				char[] chars    = line.toCharArray();
				int    valIndex = 0;

				// 忽略被"#"或"!"注释的行
				if ('#' == chars[0] || '!' == chars[0]) {
					continue;
				}

				// 查找"="或":"所在的位置，用于切分key/value
				for (char chr : chars) {
					if (chr == '=' || chr == ':') {
						break;
					}

					valIndex++;
				}

				// 解析参数名称和参数值
				if (valIndex > 0 && valIndex != chars.length) {
					String key = new String(chars, 0, valIndex).trim();

					// 替换参数值中的"\"，将两个"\\"替换成一个"\"
					String value = loadConvert(new String(chars, valIndex + 1, chars.length - valIndex - 1)).trim();

					if (key.length() > 0) {
						configMap.put(key, value);
					}
				}
			}
		}

		return configMap;
	}

	/**
	 * 使用非标准的方式修改Properties文件配置，仅能应用于RASP
	 *
	 * @param configFile 配置文件路径
	 * @param map        需要修改的key/value集合
	 * @throws IOException IO异常
	 */
	public static synchronized void writeProperties(File configFile, Map<String, String> map) throws IOException {
		if (map.isEmpty()) {
			return;
		}

		StringBuilder sb    = new StringBuilder();
		List<String>  lines = readLines(configFile, DEFAULT_ENCODING);

		for (String line : lines) {
			if (line.length() > 0) {
				char[] chars      = line.toCharArray();
				int    valueIndex = 0;

				for (char chr : chars) {
					if ('#' == chars[0] || '!' == chars[0]) {
						sb.append(line).append("\n");
						break;
					}

					if (chr == '=' || chr == ':') {
						break;
					}

					valueIndex++;
				}

				if (valueIndex > 0 && valueIndex != chars.length) {
					String key   = new String(chars, 0, valueIndex).trim();
					String value = new String(chars, valueIndex + 1, chars.length - valueIndex - 1).trim();

					if (key.length() > 0 && map.containsKey(key)) {
						value = saveConvert(map.get(key));
					}

					sb.append(key).append("=").append(value).append("\n");
				}
			}
		}

		if (sb.length() > 0) {
			writeStringToFile(configFile, sb.toString());
		} else {
			throw new RuntimeException("配置文件：" + configFile.getAbsolutePath() + "不能修改为空！");
		}
	}

	/**
	 * 读取properties的时候需要转义属性值
	 *
	 * @param str 属性值
	 * @return 解析之后的属性值
	 */
	private static String loadConvert(String str) {
		if (str == null || "".equals(str)) return str;

		return str.replace("\\\\", "\\");
	}

	/**
	 * 保存properties的时候转义属性值
	 *
	 * @param str 属性值
	 * @return 转义后的属性值
	 */
	private static String saveConvert(String str) {
		if (str == null || "".equals(str)) return "";

		return str.replace("\\", "\\\\");
	}

}