package org.javaweb.rasp.commons.servlet;

public interface HttpSessionProxy {

	Object __getSession();

	Class<?> __getSessionClass();

	ServletContextProxy getServletContext();

	Object getAttribute(String name);

}

