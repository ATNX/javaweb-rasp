package org.javaweb.rasp.commons.utils;

import org.javaweb.rasp.commons.config.RASPPropertiesConfiguration;
import org.javaweb.rasp.commons.servlet.HttpServletRequestProxy;
import org.javaweb.rasp.commons.servlet.HttpServletResponseProxy;

import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.javaweb.rasp.commons.constants.RASPConfigConstants.PROXY_IP_HEADER;
import static org.javaweb.rasp.commons.utils.IPV4Utils.*;

/**
 * Created by yz on 2017/1/17.
 *
 * @author yz
 */
public class HttpServletRequestUtils {

	/**
	 * 如果经过nginx反向代理后可能会获取到一个本地的IP地址如:127.0.0.1、192.168.1.100
	 * 配置nginx把客户端真实IP地址放到nginx请求头中的x-real-ip或x-forwarded-for的值
	 *
	 * @param request 请求对象
	 * @param config  Web应用配置
	 * @return 获取客户端IP
	 */
	public static String getRemoteAddr(HttpServletRequestProxy request, RASPPropertiesConfiguration config) {
		String ip = request.getRemoteAddr();

		// 如果IP地址为空或者IP是本机、内网地址，需要解析请求头中的IP
		if (isLanIP(ip)) {
			String ipKey   = config.getString(PROXY_IP_HEADER, "x-forwarded-for");
			String proxyIP = request.getHeader(ipKey);

			if (proxyIP != null) {
				if (textToNumericFormatV4(proxyIP) != null) return proxyIP;
				if (textToNumericFormatV6(proxyIP) != null) return proxyIP;
			}
		}

		return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
	}

	/**
	 * 获取Http请求头对象
	 *
	 * @param request 请求对象
	 * @return 请求头Map
	 */
	public static Map<String, String> getRequestHeaderMap(HttpServletRequestProxy request) {
		Map<String, String> map = new LinkedHashMap<String, String>();
		Enumeration<String> e   = request.getHeaderNames();

		while (e.hasMoreElements()) {
			String name = e.nextElement();
			map.put(name, request.getHeader(name));
		}

		return map;
	}

	/**
	 * 获取Http请求头对象
	 *
	 * @param response 响应对象
	 * @return 响应头Map
	 */
	public static Map<String, String> getResponseHeaderMap(HttpServletResponseProxy response) {
		Map<String, String> map     = new LinkedHashMap<String, String>();
		Collection<String>  headers = response.getHeaderNames();

		if (headers != null) {
			for (String name : headers) {
				map.put(name, response.getHeader(name));
			}
		}

		return map;
	}

	/**
	 * 实现htmlSpecialChars函数把一些预定义的字符转换为HTML实体编码
	 *
	 * @param content 输入的字符串内容
	 * @return HTML实体化转义后的字符串
	 */
	public static String htmlSpecialChars(String content) {
		if (content == null) {
			return null;
		}

		char[]        charArray = content.toCharArray();
		StringBuilder sb        = new StringBuilder();

		for (char c : charArray) {
			switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\'':
					sb.append("&#039;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				default:
					sb.append(c);
					break;
			}
		}

		return sb.toString();
	}

}
