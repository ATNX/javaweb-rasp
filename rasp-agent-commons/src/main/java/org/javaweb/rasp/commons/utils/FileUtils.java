package org.javaweb.rasp.commons.utils;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static java.io.File.*;

public class FileUtils extends org.apache.commons.io.FileUtils {

	/**
	 * 判断当前系统是否是windows系统
	 */
	public static boolean IS_WINDOWS = File.separatorChar == '\\';

	/**
	 * 获取文件后缀
	 *
	 * @param fileName 文件名
	 * @return 文件后缀
	 */
	public static String getFileSuffix(String fileName) {
		return fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".") + 1) : "";
	}

	/**
	 * 按行写入文件
	 *
	 * @param file   文件对象
	 * @param lines  每行数据
	 * @param append 是否以追加模式写入
	 * @throws IOException 写入文件时IO异常
	 */
	public static void writeLine(File file, Collection<?> lines, boolean append) throws IOException {
		writeLines(file, lines, append);
	}

	/**
	 * 获取文件访问权限值:是否可读(4)、可写(2)、可执行(1)
	 *
	 * @param file 文件对象
	 * @return 文件权限
	 */
	public static int getFilePerms(File file) {
		return (file.canRead() ? 4 : 0) + (file.canWrite() ? 2 : 0) + (file.canExecute() ? 1 : 0);
	}

	public static long[] getMemoryInfo() {
		Runtime runtime         = Runtime.getRuntime();
		long    freeMemory      = runtime.freeMemory();
		long    totalMemory     = runtime.totalMemory();
		long    usedMemory      = totalMemory - freeMemory;
		long    maxMemory       = runtime.maxMemory();
		long    availableMemory = maxMemory - totalMemory + freeMemory;

		return new long[]{freeMemory, totalMemory, usedMemory, maxMemory, availableMemory};
	}

	/* A normal Unix pathname contains no duplicate slashes and does not end
       with a slash.  It may be the empty string. */
	/* Normalize the given pathname, whose length is len, starting at the given
	   offset; everything before this offset is already normal. */
	private static String unixNormalize(String pathname, int len, int off) {
		if (len == 0) return pathname;
		int n = len;
		while ((n > 0) && (pathname.charAt(n - 1) == '/')) n--;
		if (n == 0) return "/";

		StringBuilder sb = new StringBuilder(pathname.length());
		if (off > 0) sb.append(pathname, 0, off);
		char prevChar = 0;

		for (int i = off; i < n; i++) {
			char c = pathname.charAt(i);
			if ((prevChar == '/') && (c == '/')) continue;
			sb.append(c);
			prevChar = c;
		}

		return sb.toString();
	}

	/* Check that the given pathname is normal.  If not, invoke the real
	   normalizer on the part of the pathname that requires normalization.
	   This way we iterate through the whole pathname string only once. */
	public static String unixNormalize(String pathname) {
		int  n        = pathname.length();
		char prevChar = 0;

		for (int i = 0; i < n; i++) {
			char c = pathname.charAt(i);

			if ((prevChar == '/') && (c == '/'))
				return unixNormalize(pathname, n, i - 1);
			prevChar = c;
		}

		if (prevChar == '/') return unixNormalize(pathname, n, n - 1);

		return pathname;
	}

	/* A normal Win32 pathname contains no duplicate slashes, except possibly
       for a UNC prefix, and does not end with a slash.  It may be the empty
       string.  Normalized Win32 pathnames have the convenient property that
       the length of the prefix almost uniquely identifies the type of the path
       and whether it is absolute or relative:

           0  relative to both drive and directory
           1  drive-relative (begins with '\\')
           2  absolute UNC (if first char is '\\'),
                else directory-relative (has form "z:foo")
           3  absolute local pathname (begins with "z:\\")
     */

	private static int winNormalizePrefix(String path, int len, StringBuffer sb) {
		int src = 0;
		while ((src < len) && isSlash(path.charAt(src))) src++;
		char c;

		if ((len - src >= 2) && isLetter(c = path.charAt(src)) && path.charAt(src + 1) == ':') {
            /* Remove leading slashes if followed by drive specifier.
               This hack is necessary to support file URLs containing drive
               specifiers (e.g., "file://c:/path").  As a side effect,
               "/c:/path" can be used as an alternative to "c:/path". */
			sb.append(c);
			sb.append(':');
			src += 2;
		} else {
			src = 0;
			if ((len >= 2) && isSlash(path.charAt(0)) && isSlash(path.charAt(1))) {
                /* UNC pathname: Retain first slash; leave src pointed at
                   second slash so that further slashes will be collapsed
                   into the second slash.  The result will be a pathname
                   beginning with "\\\\" followed (most likely) by a host
                   name. */
				src = 1;
				sb.append(separator);
			}
		}

		return src;
	}

	/* Normalize the given pathname, whose length is len, starting at the given
	   offset; everything before this offset is already normal. */
	private static String winNormalize(String path, int len, int off) {
		if (len == 0) return path;
		if (off < 3) off = 0;   /* Avoid fencepost cases with UNC pathnames */
		int          src;
		char         slash = separatorChar;
		StringBuffer sb    = new StringBuffer(len);

		if (off == 0) {
			/* Complete normalization, including prefix */
			src = winNormalizePrefix(path, len, sb);
		} else {
			/* Partial normalization */
			src = off;
			sb.append(path, 0, off);
		}

        /* Remove redundant slashes from the remainder of the path, forcing all
           slashes into the preferred slash */
		while (src < len) {
			char c = path.charAt(src++);

			if (isSlash(c)) {
				while ((src < len) && isSlash(path.charAt(src))) src++;

				if (src == len) {
					/* Check for trailing separator */
					int sn = sb.length();

					if ((sn == 2) && (sb.charAt(1) == ':')) {
						/* "z:\\" */
						sb.append(slash);
						break;
					}

					if (sn == 0) {
						/* "\\" */
						sb.append(slash);
						break;
					}
					if ((sn == 1) && (isSlash(sb.charAt(0)))) {
                        /* "\\\\" is not collapsed to "\\" because "\\\\" marks
                           the beginning of a UNC pathname.  Even though it is
                           not, by itself, a valid UNC pathname, we leave it as
                           is in order to be consistent with the win32 APIs,
                           which treat this case as an invalid UNC pathname
                           rather than as an alias for the root directory of
                           the current drive. */
						sb.append(slash);
						break;
					}

                    /* Path does not denote a root directory, so do not append
                       trailing slash */
					break;
				} else {
					sb.append(slash);
				}
			} else {
				sb.append(c);
			}
		}

		return sb.toString();
	}

	/* Check that the given pathname is normal.  If not, invoke the real
	   normalizer on the part of the pathname that requires normalization.
	   This way we iterate through the whole pathname string only once. */
	public static String winNormalize(String path) {
		int  n     = path.length();
		char slash = separatorChar;
		char prev  = 0;

		for (int i = 0; i < n; i++) {
			char c = path.charAt(i);

			if (c == pathSeparatorChar)
				return winNormalize(path, n, (prev == slash) ? i - 1 : i);
			if ((c == slash) && (prev == slash) && (i > 1))
				return winNormalize(path, n, i - 1);
			if ((c == ':') && (i > 1))
				return winNormalize(path, n, 0);

			prev = c;
		}

		if (prev == slash) return winNormalize(path, n, n - 1);
		return path;
	}

	/**
	 * 输出格式化后的路径，移除多余的"/"但不会移除"../"
	 *
	 * @param path 文件路径
	 * @return 标准文件路径
	 */
	public static String normalize(String path) {
		if (path == null) {
			return null;
		}

		// Windows和Unix使用不一样的路径处理方式
		return IS_WINDOWS ? winNormalize(path) : unixNormalize(path);
	}

	public static boolean isSlash(char c) {
		return (c == '\\') || (c == '/');
	}

	public static boolean isLetter(char c) {
		return ((c >= 'a') && (c <= 'z')) || ((c >= 'A') && (c <= 'Z'));
	}

}
