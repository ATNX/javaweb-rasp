package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.config.RASPPropertiesConfiguration;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.javaweb.rasp.commons.servlet.HttpServletRequestProxy;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_CONFIG;
import static org.javaweb.rasp.commons.constants.RASPConfigConstants.*;

public class RASPRequestEnv {

	private final int port;

	private final String appId;

	private final String domain;

	private final String serverIP;

	private final String requestIP;

	private final String contextPath;

	private final String loggerName;

	private final File documentRoot;

	private final String rc4Key;

	private final String connectKey;

	private final RASPPropertiesConfiguration appConfig;

	public static final Set<Class<?>> API_CLASS_LIST = new HashSet<Class<?>>();

	public static final Class<?>[] ARG_TYPES = new Class[]{Map.class, RASPRequestEnv.class};

	public static final Map<String, Method> API_METHOD_MAP = new HashMap<String, Method>();

	public RASPRequestEnv(RASPHttpRequestContext context) {
		HttpServletRequestProxy request = context.getServletRequest();

		this.domain = request.getServerName();
		this.requestIP = context.getRequestIP();
		this.serverIP = request.getLocalAddr();
		this.contextPath = context.getContextPath();
		this.loggerName = context.getLoggerName();
		this.port = request.getServerPort();
		this.documentRoot = context.getDocumentRoot();
		this.appConfig = context.getApplicationConfig();
		this.appId = appConfig.getString(APP_ID, APP_ID);
		this.rc4Key = AGENT_CONFIG.getString(SYSTEM_RC4_KEY);
		this.connectKey = AGENT_CONFIG.getString(SYSTEM_CONNECT_KEY);
	}

	public String getDomain() {
		return domain;
	}

	public String getRequestIP() {
		return requestIP;
	}

	public String getServerIP() {
		return serverIP;
	}

	public String getContextPath() {
		return contextPath;
	}

	public String getLoggerName() {
		return loggerName;
	}

	public int getPort() {
		return port;
	}

	public File getDocumentRoot() {
		return documentRoot;
	}

	public RASPPropertiesConfiguration getAppConfig() {
		return appConfig;
	}

	public String getAppId() {
		return appId;
	}

	public String getRc4Key() {
		return rc4Key;
	}

	public String getConnectKey() {
		return connectKey;
	}

}
