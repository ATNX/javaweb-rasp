package org.javaweb.rasp.commons.cache;

import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.javaweb.rasp.commons.utils.IOUtils;
import org.javaweb.rasp.commons.utils.JsonUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.javaweb.rasp.commons.attack.RASPParameterPosition.JSON;
import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;
import static org.javaweb.rasp.commons.context.RASPHttpRequestContextManager.getContext;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;

public class RASPSerialization {

	/**
	 * 请求参数序列化
	 *
	 * @param in 输入流
	 */
	public static void serialization(InputStream in) {
		if (in == null) {
			return;
		}

		try {
			RASPHttpRequestContext   context          = getContext();
			RASPCachedRequest        cachedRequest    = context.getCachedRequest();
			Set<RASPCachedParameter> cachedParameters = cachedRequest.getCachedParameter();
			String                   serializeStr     = IOUtils.toString(in);

			if (serializeStr == null) {
				return;
			}

			// 缓存序列化字符串
			cachedRequest.cacheSerializeStr(serializeStr);

			// 序列化JSON字符串
			if (context.isJsonRequest()) {
				jsonDecode(serializeStr, cachedParameters);
			}
		} catch (IOException e) {
			AGENT_LOGGER.error(AGENT_NAME + " 反序列化Http请求输入流异常：" + e, e);
		}
	}

	private static void xmlDecode(String xml, Set<RASPCachedParameter> cachedParameters) {

	}

	private static void jsonDecode(String json, Set<RASPCachedParameter> cachedParameters) {
		// 快速判断是否是JSON字符串
		if ((json.contains("{") && json.contains("}")) || (json.contains("[") && json.contains("]"))) {
			try {
				Object obj = JsonUtils.toJSONObject(json);

				if (obj instanceof List) {
					List<?> jsonList = (List) obj;

					for (Object val : jsonList) {
						if (val instanceof Map) {
							cacheJSONMap((Map) val, cachedParameters);
						}
					}
				} else if (obj instanceof Map) {
					cacheJSONMap((Map) obj, cachedParameters);
				}
			} catch (Exception e) {
				AGENT_LOGGER.debug(AGENT_NAME + " 反序列化JSON：" + json + "异常：" + e, e);
			}
		}
	}

	private static void cacheJSONMap(Map jsonMap, Set<RASPCachedParameter> parameters) {
		for (Object key : jsonMap.keySet()) {
			Object value = jsonMap.get(key);

			if (value instanceof String) {
				parameters.add(new RASPCachedParameter(String.valueOf(key), (String) value, JSON));
			}
		}
	}

}
