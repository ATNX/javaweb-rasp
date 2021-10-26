package org.javaweb.rasp.commons.config;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.apache.commons.io.FileUtils.copyFile;
import static org.javaweb.rasp.commons.RASPLogger.createAgentLogger;
import static org.javaweb.rasp.commons.constants.RASPConfigConstants.FORBIDDEN_FILE;
import static org.javaweb.rasp.commons.loader.AgentConstants.*;

/**
 * RASP 配置
 * Created by yz on 2016/11/17.
 */
public class RASPConfiguration {

	// RASP 安装目录
	public static final File RASP_DIRECTORY = new File(
			RASPConfiguration.class.getProtectionDomain().getCodeSource().getLocation().getFile()
	).getParentFile();

	// RASP 适配文件
	public static final File RASP_ADAPTER_FILE = new File(RASP_DIRECTORY, ADAPTER_FILE_NAME);

	// RASP 配置目录
	public static final File RASP_CONFIG_DIRECTORY = getDirectory(new File(RASP_DIRECTORY, "config"));

	// RASP Hook目录
	public static final File RASP_HOOK_DIRECTORY = getDirectory(new File(RASP_DIRECTORY, "hooks"));

	// RASP 日志目录
	public static final File RASP_LOG_DIRECTORY = getDirectory(new File(RASP_DIRECTORY, "logs"));

	// RASP 数据目录
	public static final File RASP_DATABASE_DIRECTORY = getDirectory(new File(RASP_DIRECTORY, "database"));

	// RASP Agent日志文件
	public static final File RASP_AGENT_FILE = new File(RASP_DATABASE_DIRECTORY, AGENT_LOG_FILE_NAME);

	// RASP 防御模块日志文件
	public static final File RASP_MODULES_FILE = new File(RASP_DATABASE_DIRECTORY, MODULES_LOG_FILE_NAME);

	// RASP 403.html
	public static final File RASP_FORBIDDEN_FILE = new File(RASP_DIRECTORY, FORBIDDEN_FILE);

	/**
	 * RASP核心配置,rasp.properties文件配置内容
	 */
	public static final RASPPropertiesConfiguration AGENT_CONFIG;

	/**
	 * RASP站点管理员配置,rasp-rules.properties文件配置内容
	 */
	public static final RASPPropertiesConfiguration AGENT_RULES_CONFIG;

	/**
	 * RASP Agent日志
	 */
	public static final Logger AGENT_LOGGER;

	/**
	 * RASP 防御模块日志
	 */
	public static final Logger MODULES_LOGGER;

	/**
	 * RASP 应用默认配置文件
	 */
	public static final File DEFAULT_CONFIG_FILE = new File(RASP_CONFIG_DIRECTORY, DEFAULT_AGENT_APP_FILE_NAME);

	/**
	 * 容器所有应用配置文件对象
	 */
	private static final Map<String, RASPPropertiesConfiguration> APPLICATION_CONFIG_MAP =
			new ConcurrentHashMap<String, RASPPropertiesConfiguration>();

	static {
		// 初始化加载RASP核心配置文件
		AGENT_CONFIG = loadRASPConfig(AGENT_CONFIG_FILE_NAME);

		// 初始化加载RASP规则配置文件
		AGENT_RULES_CONFIG = loadRASPConfig(AGENT_RULES_FILE_NAME);

		// 获取RASP日志输出级别
		Level logLevel = Level.toLevel(AGENT_CONFIG.getString("log.level", "INFO"));

		// 创建agent日志Logger
		AGENT_LOGGER = createAgentLogger("agent", RASP_AGENT_FILE, logLevel);

		// 创建防御模块Logger
		MODULES_LOGGER = createAgentLogger("modules", RASP_MODULES_FILE, logLevel);
	}

	/**
	 * 返回传入文件目录对象，如果目录不存在会自动创建
	 *
	 * @param dir 目录
	 * @return 目录对象
	 */
	private static File getDirectory(File dir) {
		if (dir != null && !dir.exists()) {
			if (!dir.mkdir()) {
				if (AGENT_LOGGER != null) {
					AGENT_LOGGER.error("创建文件：{}失败！", dir);
				}
			}
		}

		return dir;
	}

	/**
	 * 加载properties配置文件
	 *
	 * @param configFileName 配置文件名称
	 * @return PropertiesConfiguration 配置文件对象
	 */
	private static RASPPropertiesConfiguration loadRASPConfig(String configFileName) {
		// 配置文件
		File configFile = new File(RASP_CONFIG_DIRECTORY, configFileName);

		RASPPropertiesConfiguration propertiesConfiguration = null;

		try {
			propertiesConfiguration = new RASPPropertiesConfiguration(configFile);
		} catch (Exception e) {
			if (AGENT_LOGGER != null) {
				AGENT_LOGGER.error("加载" + AGENT_NAME + "配置文件[" + configFile + "]异常: ", e);
			} else {
				e.printStackTrace();
			}
		}

		return propertiesConfiguration;
	}

	/**
	 * 获取应用配置文件
	 *
	 * @param applicationName 应用名称
	 * @return 应用配置文件对象
	 */
	public static RASPPropertiesConfiguration getWebApplicationConfig(String applicationName) {
		RASPPropertiesConfiguration config = APPLICATION_CONFIG_MAP.get(applicationName);

		if (config != null) return config;

		String filename   = "apps/" + applicationName + ".properties";
		File   configFile = new File(RASP_CONFIG_DIRECTORY, filename);

		RASPPropertiesConfiguration configuration = null;

		try {
			if (!DEFAULT_CONFIG_FILE.exists()) {
				AGENT_LOGGER.error("{}读取配置文件：{}不存在！", AGENT_NAME, DEFAULT_CONFIG_FILE);
				throw new RuntimeException();
			}

			if (!RASP_CONFIG_DIRECTORY.canWrite()) {
				AGENT_LOGGER.error("{}无权限修改配置文件目录：{}！", AGENT_NAME, DEFAULT_CONFIG_FILE);
				throw new RuntimeException();
			}

			if (!configFile.exists()) {
				copyFile(DEFAULT_CONFIG_FILE, configFile);
			}

			configuration = loadRASPConfig(filename);
		} catch (IOException e) {
			AGENT_LOGGER.error(AGENT_NAME + "读取" + applicationName + "配置文件[" + configFile + "]异常: ", e);
		}

		APPLICATION_CONFIG_MAP.put(applicationName, configuration);

		return configuration;
	}

}
