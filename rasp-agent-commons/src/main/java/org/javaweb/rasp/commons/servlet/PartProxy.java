package org.javaweb.rasp.commons.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

public interface PartProxy {

	Object __getPart();

	Class<?> __getPartClass();

	InputStream getInputStream() throws IOException;

	String getContentType();

	String getName();

	String getSubmittedFileName();

	long getSize();

	void write(String fileName) throws IOException;

	void delete() throws IOException;

	String getHeader(String name);

	Collection<String> getHeaders(String name);

	Collection<String> getHeaderNames();

}
