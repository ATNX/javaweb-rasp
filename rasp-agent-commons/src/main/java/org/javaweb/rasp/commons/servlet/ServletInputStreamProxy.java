package org.javaweb.rasp.commons.servlet;

import java.io.IOException;

public interface ServletInputStreamProxy {

	Object __getServletInputStream();

	Class<?> __getServletInputStreamClass();

	int read() throws IOException;

	int read(byte[] b) throws IOException;

	int read(byte[] b, int off, int len) throws IOException;

	int readLine(byte[] b, int off, int len) throws IOException;

	boolean isFinished();

	boolean isReady();

}
