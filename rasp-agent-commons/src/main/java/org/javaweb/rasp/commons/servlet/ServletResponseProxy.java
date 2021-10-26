package org.javaweb.rasp.commons.servlet;

import java.io.IOException;
import java.io.PrintWriter;

public interface ServletResponseProxy {

	String getContentType();

	void setContentType(String type);

	ServletOutputStreamProxy getOutputStream() throws IOException;

	PrintWriter getWriter() throws IOException;

	boolean isCommitted();

	void reset();

}