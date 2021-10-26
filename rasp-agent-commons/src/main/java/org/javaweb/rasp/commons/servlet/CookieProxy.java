package org.javaweb.rasp.commons.servlet;

public interface CookieProxy {

	Class<?> __getCookieClass();

	String getName();

	String getValue();

	void setValue(String newValue);
	
}
