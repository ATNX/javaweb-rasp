package org.javaweb.rasp.commons.servlet;

import java.io.IOException;

public interface ServletOutputStreamProxy {

	Object __getServletOutputStream();

	Class<?> __getServletOutputStreamClass();

	void print(String s) throws IOException;

	void println(String s) throws IOException;

	void write(int b) throws IOException;

	void write(byte[] b) throws IOException;

	void write(byte[] b, int off, int len) throws IOException;

}
