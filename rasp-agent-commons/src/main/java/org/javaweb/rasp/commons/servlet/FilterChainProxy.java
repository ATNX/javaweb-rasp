package org.javaweb.rasp.commons.servlet;

import java.io.IOException;

public interface FilterChainProxy {

	Object __getChain();

	Class<?> __getChainClass();

	void doFilter(Object request, Object response) throws IOException;

}