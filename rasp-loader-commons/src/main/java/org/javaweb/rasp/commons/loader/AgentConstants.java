package org.javaweb.rasp.commons.loader;

/**
 * Creator: yz
 * Date: 2020-04-05
 */
public class AgentConstants {

	/**
	 * 定义Agent名称
	 */
	public static final String AGENT_NAME = "RASP";

	/**
	 * 定义Agent文件名称前缀
	 */
	public static final String AGENT_FILE_PREFIX_NAME = "rasp";

	/**
	 * 定义Agent包名前缀
	 */
	public static final String AGENT_PACKAGE_PREFIX = "org.javaweb.rasp.";

	/**
	 * 访问日志前缀
	 */
	public static final String ACCESS_LOGGER_PREFIX = "access_log_";

	/**
	 * 攻击日志前缀
	 */
	public static final String ATTACK_LOGGER_PREFIX = "attack_log_";

	/**
	 * 访问日志
	 */
	public static final String ACCESS_LOG = "access";

	/**
	 * 攻击日志
	 */
	public static final String ATTACK_LOG = "attack";

	/**
	 * 定义Agent loader文件名称
	 */
	public static final String AGENT_LOADER_FILE_NAME = AGENT_FILE_PREFIX_NAME + "-loader.jar";

	/**
	 * 定义Agent jar文件名称
	 */
	public static final String AGENT_FILE_NAME = AGENT_FILE_PREFIX_NAME + "-agent.jar";

	/**
	 * 定义RASP 请求适配jar名称
	 */
	public static final String ADAPTER_FILE_NAME = AGENT_FILE_PREFIX_NAME + "-servlet.jar";

	/**
	 * RASP Runtime日志文件名
	 */
	public static final String AGENT_LOG_FILE_NAME = AGENT_FILE_PREFIX_NAME + "-agent.log";

	/**
	 * RASP 防御模块日志文件名
	 */
	public static final String MODULES_LOG_FILE_NAME = AGENT_FILE_PREFIX_NAME + "-modules.log";

	/**
	 * 攻击日志文件名
	 */
	public static final String ATTACK_LOG_FILE_NAME = AGENT_FILE_PREFIX_NAME + "-attack.log";

	/**
	 * 访问日志文件名
	 */
	public static final String ACCESS_LOG_FILE_NAME = AGENT_FILE_PREFIX_NAME + "-access.log";

	/**
	 * 定义Agent Banner文件名称
	 */
	public static final String BANNER_FILE_NAME = "banner.txt";

	/**
	 * RASP 应用默认配置文件
	 */
	public static final String DEFAULT_AGENT_APP_FILE_NAME = "default-" + AGENT_FILE_PREFIX_NAME + "-app.properties";

	/**
	 * RASP核心配置文件名称
	 */
	public static final String AGENT_CONFIG_FILE_NAME = AGENT_FILE_PREFIX_NAME + ".properties";

	/**
	 * RASP 规则文件名称
	 */
	public static final String AGENT_RULES_FILE_NAME = AGENT_FILE_PREFIX_NAME + "-rules.properties";

}
