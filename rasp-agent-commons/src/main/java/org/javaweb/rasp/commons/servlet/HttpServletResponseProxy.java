package org.javaweb.rasp.commons.servlet;

import java.util.Collection;

public interface HttpServletResponseProxy extends ServletResponseProxy {

	Object __getResponse();

	Class<?> __getResponseClass();

	void setHeader(String name, String value);

	void setStatus(int sc, String sm);

	int getStatus();

	void setStatus(int sc);

	String getHeader(String name);

	Collection<String> getHeaderNames();

	String getCharacterEncoding();

}
