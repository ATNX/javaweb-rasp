package org.javaweb.rasp.commons.cache;

import java.util.*;

import static org.javaweb.rasp.commons.attack.RASPParameterPosition.REQUEST_ATTRIBUTE;

/**
 * RASP 缓存请求对象封装,缓存请求中的各种参数信息
 * Creator: yz
 * Date: 2019-08-06
 */
public class RASPCachedRequest {

	/**
	 * 输入流
	 */
	private RASPOutputStreamCache inputStreamCache;

	/**
	 * 输出流
	 */
	private RASPOutputStreamCache outputStreamCache;

	/**
	 * 缓存序列化字符串
	 */
	private String serializeStr;

	/**
	 * 缓存SQL语句执行记录
	 */
	private final List<String> sqlQueryList = new ArrayList<String>();

	/**
	 * RASP Http的参数缓存
	 */
	private final Set<RASPCachedParameter> raspCachedParameterList = new HashSet<RASPCachedParameter>();

//	/**
//	 * 解析queryString的参数，因为JEECMS会自己解析，而不是request.getParameter导致参数丢失
//	 *
//	 * @param parameter 缓存参数
//	 */
//	public void parseQueryString(RASPCachedParameter parameter) {
//		if (parameter.getValue().length != 1) return;
//
//		String queryString = parameter.getValue()[0];
//
//		try {
//			String   urlDecode = URLDecoder.decode(queryString, "UTF-8");
//			String[] args      = StringUtils.split(urlDecode, "&");
//
//			for (String str : args) {
//				if (str.length() > 1 && str.contains("=")) {
//					String[] arg = StringUtils.split(str, "=");
//
//					cacheRequestParameter(new RASPCachedParameter(arg[0], arg[1], QUERY_STRING));
//				}
//			}
//		} catch (UnsupportedEncodingException ignored) {
//		}
//	}

	/**
	 * 缓存被调用过的Http请求参数键
	 *
	 * @param parameter 缓存参数
	 */
	public void cacheRequestParameter(RASPCachedParameter parameter) {
		raspCachedParameterList.add(parameter);
	}

	/**
	 * 缓存应用层设置的请求Attribute键名
	 *
	 * @param key   属性名称
	 * @param value 属性值
	 */
	public void cacheRequestAttribute(String key, Object value) {
		// 只接受字符串类型的属性值
		if (value instanceof String) {
			cacheRequestParameter(new RASPCachedParameter(key, (String) value, REQUEST_ATTRIBUTE));
		}
	}

	/**
	 * 缓存SQL查询语句,用于避免SQL重复验证问题
	 *
	 * @param sql SQL语句
	 */
	public void cacheSqlQuery(String sql) {
		this.sqlQueryList.add(sql);
	}

	public void cacheSerializeStr(String jsonStr) {
		this.serializeStr = jsonStr;
	}

	public boolean containsSQLQueryCache(String sql) {
		return sqlQueryList.contains(sql);
	}

	/**
	 * 组装所有来自客户端的请求参数,包含:Parameter、QueryString、Cookies、Header、Multipart、ParameterMap
	 *
	 * @return 返回缓存在RASP上下文中的所有参数
	 */
	public Set<RASPCachedParameter> getCachedParameter() {
		return raspCachedParameterList;
	}

	public String getSerializeStr() {
		return serializeStr;
	}

	public RASPOutputStreamCache getInputStreamCache() {
		return inputStreamCache;
	}

	public RASPOutputStreamCache getOutputStreamCache() {
		return outputStreamCache;
	}

	public void initInputStreamCache(RASPOutputStreamCache out) {
		this.inputStreamCache = out;
	}

	public void intOutputStreamCache(RASPOutputStreamCache out) {
		this.outputStreamCache = out;
	}

}
