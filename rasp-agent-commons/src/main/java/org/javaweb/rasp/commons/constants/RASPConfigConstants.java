package org.javaweb.rasp.commons.constants;

import java.util.Arrays;
import java.util.List;

/**
 * RASP 配置文件键名常量
 *
 * @author yz
 * Date: 2019-07-18
 */
public class RASPConfigConstants {

	/**
	 * RASP 版本号
	 */
	public static final String VERSION = "version";

	/**
	 * 字节码编辑器类型
	 */
	public static final String BYTECODE_EDITOR = "bytecode_editor";

	/**
	 * 字符串：javassist，因为maven配置了javassist关键字重定向，所以这里不能直接写"javassist"
	 */
	public static final String JAVASSIST = new String(new byte[]{106, 97, 118, 97, 115, 115, 105, 115, 116});

	/**
	 * 本地配置同步时间间隔（秒）
	 */
	public static final String SYNC_INTERVAL = "sync.interval";

	/**
	 * 启动Agent时是否显示版本信息
	 */
	public static final String DISPLAY_VERSION = "display_version";

	/**
	 * 是否启用Servlet输入输出流Hook
	 */
	public static final String SERVLET_STREAM_HOOK = "servlet_stream";

	/**
	 * 最大缓存的Servlet输入输出流大小，默认10MB
	 */
	public static final String SERVLET_STREAM_MAX_CACHE_SIZE = "servlet_stream_max_cache_size";

	/**
	 * RASP RC4加密Key
	 */
	public static final String SYSTEM_RC4_KEY = "system.rc4_key";

	/**
	 * RASP 云端通讯KEY
	 */
	public static final String SYSTEM_CONNECT_KEY = "system.connect_key";

	/**
	 * RASP 配置文件同步接口
	 */
	public static final String SYSTEM_API_URL = "system.api_url";

	/**
	 * 设置不需要经过Agent处理的包名正则表达式
	 */
	public static final String PROTECTED_HOOK_PACKAGE_REGEXP = "protected_hook_package_regexp";

	/**
	 * 默认不需要ASM处理的Java包或类名称
	 */
	public final static String DEFAULT_PROTECTED_PACKAGE_REGEXP = "" +
			"(java\\.(security|util)\\.|" +
			"java\\.lang\\.(invoke|ref|concurrent|instrument)|" +
			"java\\.lang\\.(Object|String|Shutdown|ThreadLocal|WeakPairMap\\b.*)$|" +
			"javax\\.crypto|sun\\.(security|misc|net)|" +
			"org\\.apache\\.commons\\.(io|lang|logging|configuration)\\.|" +
			"org\\.objectweb\\.asm\\.|com\\.google\\.gson\\.|" +
			"\\$\\$(FastClassBySpringCGLIB|Lambda|EnhancerBySpringCGLIB)\\$)";

	/**
	 * 设置需要保护的目录，这个目录下的应用将不会被防御
	 */
	public static final String PROTECTED_DIRECTORY = "protected_directory";

	/**
	 * 配置允许执行本地系统命令的类正则
	 */
	public static final String ALLOWED_CMD_CLASS_NAME = "allowed_cmd_class_name";

	/**
	 * 配置不允许执行本地系统命令的类正则
	 */
	public static final String DISALLOWED_CMD_CLASS_NAME = "disallowed_cmd_class_name";

	/**
	 * 是否是静默模式: true、false
	 */
	public static final String SILENT = "silent";

	/**
	 * 是否禁止本地命令执行
	 */
	public static final String DISABLE_CMD = "disable_cmd";

	/**
	 * 是否禁止脚本引擎解析
	 */
	public static final String DISABLE_SCRIPT_ENGINE = "disable_script_engine";

	/**
	 * 开启的RASP 防御模块
	 */
	public static final String MODULES_OPEN = "modules.open";

	/**
	 * 拒绝访问时的拦截页面文件名
	 */
	public static final String FORBIDDEN_FILE = "403.html";

	/**
	 * 文件上传不允许后缀
	 */
	public static final String UPLOAD_NOT_ALLOWED_SUFFIX = "upload.not_allowed_suffix";

	/**
	 * 敏感文件后缀
	 */
	public static final String FILESYSTEM_PROTECTED_SUFFIX = "filesystem.protected.suffix";

	/**
	 * 读取文件正则表达式规则
	 */
	public static final String FILESYSTEM_READ_REGEXP = "filesystem.read.regexp";

	/**
	 * 写入文件正则表达式规则
	 */
	public static final String FILESYSTEM_WRITE_REGEXP = "filesystem.write.regexp";

	/**
	 * SSRF协议规则
	 */
	public static final String SSRF_PROTOCOL_TYPES = "ssrf.protocol.types";

	/**
	 * SSRF黑名单域名列表
	 */
	public static final String SSRF_BLACK_DOMAIN = "ssrf.black.domain";

	/**
	 * Fastjson规则
	 */
	public static final String FASTJSON_REGEXP = "fastjson.regexp";

	/**
	 * JSON序列化禁用的类
	 */
	public static final String JSON_DISABLE_CLASS = "json.disable_class";

	/**
	 * XStream禁用的类
	 */
	public static final String XSTREAM_DISABLE_CLASS = "xstream.disable_class";

	/**
	 * WebShell 特征
	 */
	public static final String WEBSHELL_FEATURE = "webshell.feature";

	/**
	 * 设置是否禁止在Agent启动后修改任何jsp/jspx类型的动态脚本文件
	 */
	public static final String DISABLE_NEW_JSP = "disable_new_jsp";

	/**
	 * Ognl表达式正则
	 */
	public static final String EXPRESSION_OGNL = "expression.Ognl";

	/**
	 * Ognl禁止调用方法包名
	 */
	public static final String EXPRESSION_OGNL_EXCLUDED_PACKAGE_NAMES = "expression.Ognl.excludedPackageName";

	/**
	 * SpEL表达式正则
	 */
	public static final String EXPRESSION_SPEL = "expression.SpEL";

	/**
	 * MVEL2表达式正则
	 */
	public static final String EXPRESSION_MVEL2 = "expression.MVEL2";

	/**
	 * Java反序列化类名检测规则
	 */
	public static final String DESERIALIZATION = "deserialization";

	/**
	 * IP黑名单列表
	 */
	public static final String IP_BLACKLIST = "ip.blacklist";

	/**
	 * 禁止访问目录规则
	 */
	public static final String URL_BLACKLIST = "url.blacklist";

	/**
	 * URL黑名单默认规则
	 */
	public static final List<String> DEFAULT_URL_BLACKLIST = Arrays.asList("/.svn,/.git".split(","));

	/**
	 * 白名单列表
	 */
	public static final String WHITELIST = "whitelist";

	/**
	 * 扫描器规则列表
	 */
	public static final String SCANNER_USER_AGENT = "scanner.user-agent";

	/**
	 * 用户指定的需要Hook处理包名或者类名
	 */
	public static final String INCLUDE_HOOK_CLASS_NAME = "include_hook_class_name";

	/**
	 * 补丁列表
	 */
	public static final String PATCH_LIST = "patch_list";

	/**
	 * 站点ID
	 */
	public static final String APP_ID = "app_id";

	/**
	 * 获取代理IP的请求头名称
	 */
	public static final String PROXY_IP_HEADER = "proxy_ip_header";

}
