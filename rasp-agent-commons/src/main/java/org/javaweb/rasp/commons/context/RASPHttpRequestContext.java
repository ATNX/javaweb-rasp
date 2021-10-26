package org.javaweb.rasp.commons.context;

import org.javaweb.rasp.commons.attack.RASPAttackInfo;
import org.javaweb.rasp.commons.cache.RASPCachedRequest;
import org.javaweb.rasp.commons.MethodHookEvent;
import org.javaweb.rasp.commons.MethodHookTrace;
import org.javaweb.rasp.commons.RASPModuleType;
import org.javaweb.rasp.commons.config.RASPPropertiesConfiguration;
import org.javaweb.rasp.commons.servlet.HttpServletRequestProxy;
import org.javaweb.rasp.commons.servlet.HttpServletResponseProxy;
import org.slf4j.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.nanoTime;
import static org.javaweb.rasp.commons.config.RASPConfiguration.getWebApplicationConfig;
import static org.javaweb.rasp.commons.constants.RASPConfigConstants.*;
import static org.javaweb.rasp.commons.utils.FileUtils.getFileSuffix;
import static org.javaweb.rasp.commons.utils.HttpServletRequestUtils.getRemoteAddr;
import static org.javaweb.rasp.commons.loader.AgentConstants.*;

/**
 * HttpRequest上下文
 */
public abstract class RASPHttpRequestContext {

	/**
	 * RASP 应用配置对象
	 */
	protected final RASPPropertiesConfiguration applicationConfig;

	/**
	 * 记录当前context发生的攻击列表
	 */
	protected final Set<RASPAttackInfo> raspAttackInfoList = new LinkedHashSet<RASPAttackInfo>();

	/**
	 * RASP 当前开放的防御模块
	 */
	protected final List<String> openModules;

	/**
	 * request
	 */
	protected final HttpServletRequestProxy servletRequest;

	/**
	 * response
	 */
	protected final HttpServletResponseProxy servletResponse;

	/**
	 * 缓存的Servlet、Filter类实例
	 */
	protected final Object cacheClass;

	/**
	 * 静默模式
	 */
	protected final boolean silent;

	/**
	 * 网站根目录
	 */
	protected final File documentRoot;

	/**
	 * 获取最大缓存的Servlet输入输出流大小，默认10MB
	 */
	protected final int maxStreamCacheSize;

	/**
	 * RASP Servlet Adapter类加载器
	 */
	protected final ClassLoader adapterClassLoader;

	/**
	 * 缓存Http请求对象
	 */
	protected final RASPCachedRequest cachedRequest;

	/**
	 * 请求开始的纳秒
	 */
	protected final long requestStartNanoTime;

	/**
	 * Servlet路径
	 */
	protected final String servletPath;

	/**
	 * Web应用Context名称
	 */
	protected final String contextPath;

	/**
	 * 请求的文件绝对路径
	 */
	private File requestFile;

	/**
	 * 请求的文件后缀
	 */
	protected final String requestFileSuffix;

	/**
	 * 客户端IP
	 */
	protected final String requestIP;

	/**
	 * User-Agent
	 */
	protected final String userAgent;

	/**
	 * Logger名称
	 */
	protected final String loggerName;

	/**
	 * 是否需要过滤
	 */
	protected boolean mustFilter = true;

	/**
	 * 记录所有的事件处理时间
	 */
	protected final Set<MethodHookTrace> methodHookTraces = new HashSet<MethodHookTrace>();

	/**
	 * 创建RASP Http请求context对象
	 *
	 * @param request            HttpRequest
	 * @param response           HttpResponse
	 * @param event              RASP 处理事件
	 * @param adapterClassLoader adapter类加载器
	 */
	public RASPHttpRequestContext(HttpServletRequestProxy request, HttpServletResponseProxy response,
	                              MethodHookEvent event, ClassLoader adapterClassLoader) {

		this.servletRequest = request;
		this.servletResponse = response;
		this.cacheClass = event.getThisObject();
		this.cachedRequest = new RASPCachedRequest();
		this.adapterClassLoader = adapterClassLoader;
		this.requestStartNanoTime = nanoTime();
		this.documentRoot = request.getDocumentRoot();
		this.servletPath = request.getServletPath();
		this.contextPath = request.getContextPath();
		this.userAgent = request.getHeader("User-Agent");

		// logger名称，ROOT的context是""，所以还原成ROOT
		this.loggerName = "".equals(contextPath) || "/".equals(contextPath) ? "ROOT" : contextPath;

		// 获取Web应用配置，第一次请求的时候会比较耗时，因为初始化配置文件
		this.applicationConfig = getWebApplicationConfig(loggerName);
		this.requestIP = getRemoteAddr(request, applicationConfig);

		// 转换成contains性能更高的HashSet
		this.openModules = applicationConfig.getList(MODULES_OPEN);
		this.silent = applicationConfig.getBoolean(SILENT, false);

		// 设置Servlet输入输出流大小，默认10MB
		this.maxStreamCacheSize = getApplicationConfig().getInt(SERVLET_STREAM_MAX_CACHE_SIZE, 10);

		// 设置请求文件后缀
		this.requestFileSuffix = getFileSuffix(servletPath).toLowerCase();

		// 初始化context
		initContext();
	}

	/**
	 * 初始化context
	 */
	protected abstract void initContext();

	/**
	 * 获取RASP 包装后的request对象
	 *
	 * @return RASPHttpRequest
	 */
	public HttpServletRequestProxy getServletRequest() {
		return servletRequest;
	}

	/**
	 * 获取RASP 包装后的response对象
	 *
	 * @return RASPHttpResponse
	 */
	public HttpServletResponseProxy getServletResponse() {
		return servletResponse;
	}

	/**
	 * 获取缓存Http请求(Servlet、Filter)入口类对象
	 *
	 * @return 缓存Http请求入口的类实例
	 */
	public Object getCacheClass() {
		return cacheClass;
	}

	/**
	 * 获取当前context下存储的攻击详情
	 *
	 * @return 攻击集合
	 */
	public Set<RASPAttackInfo> getRaspAttackInfoList() {
		return raspAttackInfoList;
	}

	/**
	 * 添加攻击信息
	 *
	 * @param attack 攻击对象
	 */
	public abstract void addAttackInfo(RASPAttackInfo attack);

	/**
	 * 检查当前请求是否需要经过安全模块处理,如：非动态文件或白名单的情况下不需要加载任何安全检测模块
	 *
	 * @param moduleName 模块名称
	 * @return 返回是否需要过滤
	 */
	public abstract boolean mustFilter(String moduleName);

	/**
	 * 检查当前请求是否需要经过安全模块处理,如：非动态文件或白名单的情况下不需要加载任何安全检测模块
	 *
	 * @param moduleType 模块类型
	 * @return 返回是否需要过滤
	 */
	public abstract boolean mustFilter(RASPModuleType moduleType);

	/**
	 * 获取RASP Servlet Adapter类加载器
	 *
	 * @return RASP Servlet Adapter类加载器
	 */
	public ClassLoader getAdapterClassLoader() {
		return adapterClassLoader;
	}

	/**
	 * 检测是否是静默模式
	 *
	 * @return 是否是静默模式
	 */
	public boolean isSilent() {
		return silent;
	}

	/**
	 * 获取缓存的http请求对象
	 *
	 * @return 缓存请求对象
	 */
	public RASPCachedRequest getCachedRequest() {
		return cachedRequest;
	}

	/**
	 * 获取请求开始的纳秒时间
	 *
	 * @return nano
	 */
	public long getRequestStartNanoTime() {
		return requestStartNanoTime;
	}

	/**
	 * 获取ServletPath
	 *
	 * @return servletPath
	 */
	public String getServletPath() {
		return servletPath;
	}

	/**
	 * 获取应用的Context名称
	 *
	 * @return Context名称
	 */
	public String getContextPath() {
		return contextPath;
	}

	/**
	 * 获取请求的文件绝对路径
	 *
	 * @return 请求的文件绝对路径
	 */
	public File getRequestFile() {
		if (requestFile != null) {
			return requestFile;
		}

		return requestFile = new File(documentRoot, servletPath);
	}

	/**
	 * 获取请求文件后缀
	 *
	 * @return 请求文件后缀
	 */
	public String getRequestFileSuffix() {
		return requestFileSuffix;
	}

	/**
	 * 获取客户端IP地址
	 *
	 * @return 客户端请求的IP地址
	 */
	public String getRequestIP() {
		return requestIP;
	}

	/**
	 * 获取User-Agent
	 *
	 * @return User-Agent
	 */
	public String getUserAgent() {
		return userAgent;
	}

	/**
	 * 获取Logger名字
	 *
	 * @return loggerName
	 */
	public String getLoggerName() {
		return loggerName;
	}

	/**
	 * 获取Web应用配置对象
	 *
	 * @return Web应用配置对象
	 */
	public RASPPropertiesConfiguration getApplicationConfig() {
		return applicationConfig;
	}

	/**
	 * 获取网站根目录
	 *
	 * @return 网站根目录
	 */
	public File getDocumentRoot() {
		return documentRoot;
	}

	/**
	 * 获取最大缓存的流大小限制
	 *
	 * @return 最大缓存的流大小
	 */
	public int getMaxStreamCacheSize() {
		return maxStreamCacheSize;
	}

	/**
	 * 获取当前上下文中的所有的Hook事件
	 *
	 * @return Hook事件列表
	 */
	public Set<MethodHookTrace> getMethodHookTraces() {
		return methodHookTraces;
	}

	/**
	 * 动态获取当前Web应用的Logger
	 *
	 * @param fileName     文件名
	 * @param loggerPrefix logger后缀
	 * @param fileSize     文件大小
	 * @return 当前Web应用Logger
	 */
	public abstract Logger getAppLogger(String fileName, String loggerPrefix, String fileSize);

	/**
	 * 获取RASP访问日志Logger对象
	 *
	 * @return 访问日志Logger对象
	 */
	public Logger initAccessLogger() {
		return getAppLogger(ACCESS_LOG_FILE_NAME, ACCESS_LOGGER_PREFIX, "10mb");
	}

	/**
	 * 获取RASP攻击日志Logger对象
	 *
	 * @return 攻击日志Logger对象
	 */
	public Logger initAttackLogger() {
		return getAppLogger(ATTACK_LOG_FILE_NAME, ATTACK_LOGGER_PREFIX, "200kb");
	}

	/**
	 * 返回是否是Web API请求
	 *
	 * @return 是否是Web API请求
	 */
	public abstract boolean isWebAPIRequest();

	/**
	 * 返回是否是JSON请求
	 *
	 * @return 是否是JSON请求
	 */
	public abstract boolean isJsonRequest();

	/**
	 * 返回是否是XML请求
	 *
	 * @return 是否是JSON请求
	 */
	public abstract boolean isXmlRequest();

	/**
	 * 设置反序列化
	 */
	public abstract void setDeserializationStatus();

	/**
	 * 当前请求中是否包含反序列化行为
	 *
	 * @return 是否反序列化
	 */
	public abstract boolean isDeserialization();

}
