package org.javaweb.rasp.commons.config;

import org.javaweb.rasp.commons.utils.FileUtils;
import org.javaweb.rasp.commons.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.io.FileUtils.readLines;
import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;
import static org.javaweb.rasp.commons.config.RASPConfiguration.DEFAULT_CONFIG_FILE;
import static org.javaweb.rasp.commons.utils.StringUtils.isEmpty;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;

public class RASPPropertiesConfiguration {

	/**
	 * 空集合
	 */
	private final List<String> emptyList = new ArrayList<String>();

	/**
	 * 配置文件的最后修改时间，默认和被删除后为0
	 */
	private long lastModified;

	/**
	 * 配置文件
	 */
	private final File configFile;

	/**
	 * 缓存配置的map对象
	 */
	private final Map<String, Object> configMap = new HashMap<String, Object>();

	public RASPPropertiesConfiguration(File configFile) {
		this.configFile = configFile;

		// 加载配置文件
		reloadConfig();
	}

	public File getConfigFile() {
		return configFile;
	}

	public int getInt(String key) {
		return getInteger(key, null);
	}

	public int getInt(String key, int defaultValue) {
		return getInteger(key, defaultValue);
	}

	public int getInteger(String key, Integer defaultValue) {
		String value = getString(key);

		if (StringUtils.isNotEmpty(value)) {
			return Integer.parseInt(value);
		} else if (defaultValue != null) {
			return defaultValue;
		}

		throw new RuntimeException(AGENT_NAME + "读取配置属性：" + key + "不存在！");
	}

	public long getLong(String key) {
		return getLong(key, null);
	}

	public Long getLong(String key, Long defaultValue) {
		String value = getString(key);

		if (StringUtils.isNotEmpty(value)) {
			return Long.parseLong(value);
		} else if (defaultValue != null) {
			return defaultValue;
		}

		throw new RuntimeException(AGENT_NAME + "读取配置属性：" + key + "不存在！");
	}

	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = getString(key);

		if (StringUtils.isNotEmpty(value)) {
			return StringUtils.equalIgnoreCase("true", value);
		} else if (defaultValue != null) {
			return defaultValue;
		}

		throw new RuntimeException(AGENT_NAME + "读取配置属性：" + key + "不存在！");
	}

	public String getString(String key) {
		return getString(key, null);
	}

	public String getString(String key, String defaultValue) {
		String value = getValue(key);

		if (StringUtils.isNotEmpty(value)) {
			return value;
		} else if (defaultValue != null) {
			return defaultValue;
		}

		return "";
	}

	/**
	 * 返回key对应的List，如果值不存在将会返回一个空的集合，切勿操作返回的空集合，因为该集合是公用的
	 *
	 * @param key 配置key
	 * @return 配置的值集合
	 */
	public List<String> getList(String key) {
		return getList(key, emptyList);
	}

	@SuppressWarnings("unchecked")
	public List<String> getList(String key, List<String> defaultValue) {
		Object obj = configMap.get(key);

		if (obj instanceof List) {
			return (List<String>) obj;
		}

		List<String> list  = new ArrayList<String>();
		String       value = getValue(key);

		if (StringUtils.isNotEmpty(value)) {
			String[] strings = StringUtils.split(value, ",");

			synchronized (RASPPropertiesConfiguration.class) {
				// 修改缓存数据类型为List，下一次获取时候就不再需要split了
				List<String> valList = Arrays.asList(strings);
				configMap.put(key, valList);

				return valList;
			}
		} else if (defaultValue != null) {
			return defaultValue;
		}

		return list;
	}

	protected String getValue(String key) {
		Object obj = configMap.get(key);

		if (obj instanceof List) {
			return StringUtils.join((List) obj, ",");
		}

		return (String) obj;
	}

	public boolean valueSearch(String key, String search) {
		String value = getString(key);

		if (isEmpty(value)) return false;
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

	/**
	 * 重新加载配置文件，只在初始化和配置文件发生了修改的情况下才会触发reload
	 */
	public void reloadConfig() {
		if (!configFile.exists() || configFile.length() == 0) {
			AGENT_LOGGER.error("{}检测到配置文件：{}丢失!", AGENT_NAME, configFile);

			// 自动还原apps目录被删除的配置文件
			if (configFile.getParentFile().getName().equalsIgnoreCase("apps")) {
				restoreWebAppConfig();
			} else {
				throw new RuntimeException(AGENT_NAME + "无法读取配置文件：" + configFile);
			}
		}

		long modified = configFile.lastModified();

		if (lastModified == modified) {
			return;
		}

		// 修改配置文件最后修改时间
		this.lastModified = modified;

		try {
			this.configMap.putAll(readProperties(configFile));
		} catch (Exception e) {
			AGENT_LOGGER.error(AGENT_NAME + "加载配置文件：" + configFile + "异常：" + e, e);
		}
	}

	/**
	 * 恢复已被删除的Web应用配置文件
	 */
	private void restoreWebAppConfig() {
		try {
			FileUtils.copyFile(DEFAULT_CONFIG_FILE, configFile);

			Map<String, String> dataMap = new HashMap<String, String>();

			// 复写内存中的配置到文件中
			for (String key : configMap.keySet()) {
				Object value = configMap.get(key);

				if (value instanceof List) {
					dataMap.put(key, StringUtils.join((List) value, ","));
				} else {
					dataMap.put(key, (String) value);
				}
			}

			setProperty(dataMap);

			AGENT_LOGGER.error("{}已还原默认配置文件：{}", AGENT_NAME, configFile);
		} catch (IOException e) {
			AGENT_LOGGER.error(AGENT_NAME + "读取配置文件[" + configFile.getAbsolutePath() + "]异常: ", e);
		}
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

		// 刷新缓存配置
		reloadConfig();
	}

	/**
	 * 使用非标准的方式解析Properties文件配置，仅能应用于RASP
	 *
	 * @param configFile 配置文件路径
	 * @return 解析后的配置文件Map
	 * @throws IOException IO异常
	 */
	public static synchronized Map<String, String> readProperties(File configFile) throws IOException {
		Map<String, String> configMap = new ConcurrentHashMap<String, String>();
		List<String>        lines     = readLines(configFile, "UTF-8");

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
		List<String>  lines = readLines(configFile, "UTF-8");

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
			FileUtils.writeStringToFile(configFile, sb.toString());
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