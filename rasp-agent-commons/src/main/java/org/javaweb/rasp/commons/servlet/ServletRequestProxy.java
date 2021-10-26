package org.javaweb.rasp.commons.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;

/**
 * 定一部分需要代理的ServletRequest方法.
 *
 * @author yz
 */
public interface ServletRequestProxy {

	Object getAttribute(String name);

	void setAttribute(String name, Object o);

	String getContentType();

	ServletInputStreamProxy getInputStream() throws IOException;

	String getParameter(String name);

	String[] getParameterValues(String name);

	Map<String, String[]> getParameterMap();

	String getServerName();

	int getServerPort();

	BufferedReader getReader() throws IOException;

	String getRemoteAddr();

	String getRealPath(String path);

	String getLocalAddr();

}
