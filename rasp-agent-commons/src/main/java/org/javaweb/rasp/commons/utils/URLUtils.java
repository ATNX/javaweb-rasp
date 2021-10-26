package org.javaweb.rasp.commons.utils;

public final class URLUtils {

	/**
	 * URL地址标准化，移除多个'/'或'../'，改自URI.normalize()
	 *
	 * @param url URL地址
	 * @return 标准化后的URL地址
	 */
	public static String urlNormalize(String url) {
		if (url == null) {
			return null;
		}

		if (url.contains("\\")) {
			// 替换'\'为'/'
			url = StringUtils.replace(url, "\\", "/");
		}

		// Does this path need normalization?
		int ns = needsNormalization(url);        // Number of segments

		if (ns < 0) {
			if (url.endsWith("/")) {
				return url.substring(0, url.length() - 1);
			}

			// Nope -- just return it
			return url;
		}

		char[] path = url.toCharArray();         // Path in char-array form

		// Split path into segments
		int[] segs = new int[ns];               // Segment-index array
		split(path, segs);

		// Remove dots
		removeDots(path, segs);

		// Prevent scheme-name confusion
		maybeAddLeadingDot(path, segs);

		// Join the remaining segments and return the result
		int lastIdx = join(path, segs);

		// 移除末尾的"/"
		if (lastIdx > 0 && path[lastIdx - 1] == '/') {
			--lastIdx;
		}

		return new String(path, 0, lastIdx);
	}

	static private int join(char[] path, int[] segs) {
		// Index of last char in path
		int end = path.length - 1;

		// Index of next path char to write
		int p = 0;

		if (path[p] == '\0') {
			// Restore initial slash for absolute paths
			path[p++] = '/';
		}

		for (int seg : segs) {
			// Current segment
			int q = seg;

			if (q == -1)
				// Ignore this segment
				continue;

			if (p == q) {
				// We're already at this segment, so just skip to its end
				while ((p <= end) && (path[p] != '\0'))
					p++;

				if (p <= end) {
					// Preserve trailing slash
					path[p++] = '/';
				}
			} else if (p < q) {
				// Copy q down to p
				while ((q <= end) && (path[q] != '\0'))
					path[p++] = path[q++];

				if (q <= end) {
					// Preserve trailing slash
					path[p++] = '/';
				}
			} else
				// ASSERT false
				throw new InternalError();
		}

		return p;
	}

	private static void maybeAddLeadingDot(char[] path, int[] segs) {
		if (path[0] == '\0')
			// The path is absolute
			return;

		int ns = segs.length;

		// Index of first segment
		int f = 0;

		while (f < ns) {
			if (segs[f] >= 0)
				break;
			f++;
		}

		if ((f >= ns) || (f == 0))
			// The path is empty, or else the original first segment survived,
			// in which case we already know that no leading "." is needed
			return;

		int p = segs[f];
		while ((p < path.length) && (path[p] != ':') && (path[p] != '\0')) p++;

		if (p >= path.length || path[p] == '\0')
			// No colon in first segment, so no "." needed
			return;

		// At this point we know that the first segment is unused,
		// hence we can insert a "." segment at that position
		path[0] = '.';
		path[1] = '\0';
		segs[0] = 0;
	}

	private static void removeDots(char[] path, int[] segs) {
		int ns  = segs.length;
		int end = path.length - 1;

		for (int i = 0; i < ns; i++) {
			int dots = 0;               // Number of dots found (0, 1, or 2)

			// Find next occurrence of "." or ".."
			do {
				int p = segs[i];

				if (path[p] == '.') {
					if (p == end) {
						dots = 1;
						break;
					} else if (path[p + 1] == '\0') {
						dots = 1;
						break;
					} else if ((path[p + 1] == '.') && ((p + 1 == end) || (path[p + 2] == '\0'))) {
						dots = 2;
						break;
					}
				}

				i++;
			} while (i < ns);
			if ((i > ns) || (dots == 0))
				break;

			if (dots == 1) {
				// Remove this occurrence of "."
				segs[i] = -1;
			} else {
				// If there is a preceding non-".." segment, remove both that
				// segment and this occurrence of ".."; otherwise, leave this
				// ".." segment as-is.
				int j;

				for (j = i - 1; j >= 0; j--) {
					if (segs[j] != -1) break;
				}

				if (j >= 0) {
					int q = segs[j];
					if (!((path[q] == '.')
							&& (path[q + 1] == '.')
							&& (path[q + 2] == '\0'))) {
						segs[i] = -1;
						segs[j] = -1;
					}
				}
			}
		}
	}

	private static void split(char[] path, int[] segs) {
		int end = path.length - 1;      // Index of last char in path
		int p   = 0;                    // Index of next char in path
		int i   = 0;                    // Index of current segment

		// Skip initial slashes
		while (p <= end) {
			if (path[p] != '/') break;
			path[p] = '\0';
			p++;
		}

		while (p <= end) {

			// Note start of segment
			segs[i++] = p++;

			// Find beginning of next segment
			while (p <= end) {
				if (path[p++] != '/')
					continue;
				path[p - 1] = '\0';

				// Skip redundant slashes
				while (p <= end) {
					if (path[p] != '/') break;
					path[p++] = '\0';
				}

				break;
			}
		}

		if (i != segs.length)
			throw new InternalError();  // ASSERT
	}

	private static int needsNormalization(String path) {
		boolean normal = true;
		int     ns     = 0;                     // Number of segments
		int     end    = path.length() - 1;    // Index of last char in path
		int     p      = 0;                      // Index of next char in path

		// Skip initial slashes
		while (p <= end) {
			if (path.charAt(p) != '/') break;
			p++;
		}

		if (p > 1) normal = false;

		// Scan segments
		while (p <= end) {
			// Looking at "." or ".." ?
			if ((path.charAt(p) == '.')
					&& ((p == end)
					|| ((path.charAt(p + 1) == '/')
					|| ((path.charAt(p + 1) == '.')
					&& ((p + 1 == end)
					|| (path.charAt(p + 2) == '/')))))) {
				normal = false;
			}

			ns++;

			// Find beginning of next segment
			while (p <= end) {
				if (path.charAt(p++) != '/')
					continue;

				// Skip redundant slashes
				while (p <= end) {
					if (path.charAt(p) != '/') break;
					normal = false;
					p++;
				}

				break;
			}
		}

		return normal ? -1 : ns;
	}


	/**
	 * 获取一个标准化的ContextPath
	 *
	 * @param contextPath 路径
	 * @return path
	 */
	public static String getContextPath(String contextPath) {
		if (contextPath.equals("")) {
			return "/";
		}

		if (contextPath.charAt(0) != '/') {
			contextPath = "/" + contextPath;
		}

		return contextPath;
	}

}
