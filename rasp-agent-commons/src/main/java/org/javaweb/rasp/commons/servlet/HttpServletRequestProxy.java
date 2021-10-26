package org.javaweb.rasp.commons.servlet;

import java.io.File;
import java.util.Enumeration;

/**
 * 定一部分需要代理的HttpServletRequest方法.
 *
 * @author yz
 */
public interface HttpServletRequestProxy extends ServletRequestProxy {

	Object __getRequest();

	Class<?> __getRequestClass();

	File getDocumentRoot();

	HttpSessionProxy getSession(boolean create);

	HttpSessionProxy getSession();

	CookieProxy[] getCookies();

	String getHeader(String name);

	Enumeration<String> getHeaderNames();

	String getMethod();

	String getContextPath();

	String getQueryString();

	String getRequestURI();

	StringBuffer getRequestURL();

	String getServletPath();

}
