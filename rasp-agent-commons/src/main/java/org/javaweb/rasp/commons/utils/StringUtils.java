package org.javaweb.rasp.commons.utils;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils extends org.apache.commons.lang3.StringUtils {

	private static final String[] EMPTY_STRING_ARRAY = {};

	/**
	 * 获取JDK文件默认编码
	 */
	private static final String DEFAULT_ENCODING = System.getProperty("file.encoding");

	public static String replaceAll(String text, String[] searchList, String[] replacementList) {
		return replaceEachRepeatedly(text, searchList, replacementList);
	}

	public static String replace(String text, String searchList, String replacementList) {
		return org.apache.commons.lang3.StringUtils.replace(text, searchList, replacementList);
	}

	public static boolean startWithIgnoreCase(String str, String prefix) {
		return startsWithIgnoreCase(str, prefix);
	}

	public static boolean equalIgnoreCase(String str, String prefix) {
		return equalsIgnoreCase(str, prefix);
	}

	public static boolean endWithIgnoreCase(String str, String prefix) {
		return endsWithIgnoreCase(str, prefix);
	}

	public static boolean isNotEmpty(Object obj) {
		if (obj == null) {
			return false;
		}

		if (obj instanceof String) {
			return !"".equals(obj);
		}

		return true;
	}

	public static boolean isEmpty(String str) {
		return !isNotEmpty(str);
	}

	/**
	 * unicode转字符串
	 *
	 * @param content unicode字符串
	 * @return 转码后的字符串
	 */
	public static String ascii2Native(String content) {
		if (StringUtils.isNotEmpty(content)) {
			List<String> asciiList = new ArrayList<String>();

			Matcher matcher = Pattern.compile("\\\\u[0-9a-fA-F]{4}").matcher(content);
			while (matcher.find()) {
				asciiList.add(matcher.group());
			}

			for (int i = 0, j = 2; i < asciiList.size(); i++) {
				String code = asciiList.get(i).substring(j, j + 4);
				char   chr  = (char) Integer.parseInt(code, 16);
				content = StringUtils.replace(content, asciiList.get(i), String.valueOf(chr));
			}
		}

		return content;
	}

	/**
	 * Test whether the given string matches the given substring
	 * at the given index.
	 *
	 * @param str       the original string (or StringBuilder)
	 * @param index     the index in the original string to start matching against
	 * @param substring the substring to match at the given index
	 */
	public static boolean substringMatch(String str, int index, CharSequence substring) {
		for (int j = 0; j < substring.length(); j++) {
			int i = index + j;

			if (i >= str.length() || str.charAt(i) != substring.charAt(j)) {
				return false;
			}
		}

		return true;
	}

	public static String charset(String str) {
		try {
			if (!DEFAULT_ENCODING.equals("UTF-8")) {
				return new String(str.getBytes(), DEFAULT_ENCODING);
			}

			return str;
		} catch (UnsupportedEncodingException ignored) {
		}

		return str;
	}

	public static void println(String str) {
		System.out.println(charset(str));
	}

	public static String hex2String(String hexString) {

		if (StringUtils.isNotEmpty(hexString)) {
			List<String> hexList = new ArrayList<String>();

			Matcher matcher = Pattern.compile("\\\\x[0-9a-z]{2}").matcher(hexString);
			while (matcher.find()) {
				hexList.add(matcher.group());
			}

			for (int i = 0, j = 2; i < hexList.size(); i++) {
				String code = hexList.get(i).substring(j, j + 2);
				char   chr  = (char) Integer.parseInt(code, 16);
				hexString = hexString.replace(hexList.get(i), String.valueOf(chr));
			}
		}

		return hexString;
	}


	/**
	 * Check whether the given {@code String} contains actual <em>text</em>.
	 * <p>More specifically, this method returns {@code true} if the
	 * {@code String} is not {@code null}, its length is greater than 0,
	 * and it contains at least one non-whitespace character.
	 *
	 * @param str the {@code String} to check (may be {@code null})
	 * @return {@code true} if the {@code String} is not {@code null}, its
	 * length is greater than 0, and it does not contain whitespace only
	 * @see Character#isWhitespace
	 */
	public static boolean hasText(String str) {
		return (str != null && !str.isEmpty() && containsText(str));
	}

	private static boolean containsText(CharSequence str) {
		int strLen = str.length();
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}
		return false;
	}

	public static boolean containsTexts(CharSequence str, CharSequence... array) {
		for (CharSequence sequence : array) {
			if (contains(str, sequence)) return true;
		}

		return false;
	}

	/**
	 * Tokenize the given {@code String} into a {@code String} array via a
	 * {@link StringTokenizer}.
	 * <p>Trims tokens and omits empty tokens.
	 * <p>The given {@code delimiters} string can consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using .
	 *
	 * @param str        the {@code String} to tokenize (potentially {@code null} or empty)
	 * @param delimiters the delimiter characters, assembled as a {@code String}
	 *                   (each of the characters is individually considered as a delimiter)
	 * @return an array of the tokens
	 * @see StringTokenizer
	 * @see String#trim()
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters) {
		return tokenizeToStringArray(str, delimiters, true, true);
	}

	/**
	 * Tokenize the given {@code String} into a {@code String} array via a
	 * {@link StringTokenizer}.
	 * <p>The given {@code delimiters} string can consist of any number of
	 * delimiter characters. Each of those characters can be used to separate
	 * tokens. A delimiter is always a single character; for multi-character
	 * delimiters, consider using .
	 *
	 * @param str               the {@code String} to tokenize (potentially {@code null} or empty)
	 * @param delimiters        the delimiter characters, assembled as a {@code String}
	 *                          (each of the characters is individually considered as a delimiter)
	 * @param trimTokens        trim the tokens via {@link String#trim()}
	 * @param ignoreEmptyTokens omit empty tokens from the result array
	 *                          (only applies to tokens that are empty after trimming; StringTokenizer
	 *                          will not consider subsequent delimiters as token in the first place).
	 * @return an array of the tokens
	 * @see StringTokenizer
	 * @see String#trim()
	 */
	public static String[] tokenizeToStringArray(String str, String delimiters,
	                                             boolean trimTokens, boolean ignoreEmptyTokens) {

		if (str == null) {
			return EMPTY_STRING_ARRAY;
		}

		StringTokenizer st     = new StringTokenizer(str, delimiters);
		List<String>    tokens = new ArrayList<String>();
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if (trimTokens) {
				token = token.trim();
			}
			if (!ignoreEmptyTokens || token.length() > 0) {
				tokens.add(token);
			}
		}

		return toStringArray(tokens);
	}


	/**
	 * Copy the given {@link Collection} into a {@code String} array.
	 * <p>The {@code Collection} must contain {@code String} elements only.
	 *
	 * @param collection the {@code Collection} to copy
	 *                   (potentially {@code null} or empty)
	 * @return the resulting {@code String} array
	 */
	public static String[] toStringArray(Collection<String> collection) {
		return (!ArrayUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
	}

	/**
	 * Copy the given {@link Enumeration} into a {@code String} array.
	 * <p>The {@code Enumeration} must contain {@code String} elements only.
	 *
	 * @param enumeration the {@code Enumeration} to copy
	 *                    (potentially {@code null} or empty)
	 * @return the resulting {@code String} array
	 */
	public static String[] toStringArray(Enumeration<String> enumeration) {
		return (enumeration != null ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY);
	}

	public static boolean containsIgnoreCase(String str, String searchStr) {
		return org.apache.commons.lang3.StringUtils.containsIgnoreCase(str, searchStr);
	}

	public static boolean startWith(String str, String prefix) {
		return startsWith(str, prefix);
	}

}