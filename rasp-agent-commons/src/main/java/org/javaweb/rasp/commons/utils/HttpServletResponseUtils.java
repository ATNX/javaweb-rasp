package org.javaweb.rasp.commons.utils;

import org.apache.commons.io.IOUtils;
import org.javaweb.rasp.commons.RASPModuleType;
import org.javaweb.rasp.commons.servlet.HttpServletResponseProxy;

import java.io.PrintWriter;

import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;

public class HttpServletResponseUtils {

	public static void responseJson(HttpServletResponseProxy response, String text) {
		response(response, "application/json;charset=UTF-8", text);
	}

	public static void responseJson(HttpServletResponseProxy response, Object obj) {
		response(response, "application/json;charset=UTF-8", JsonUtils.toJson(obj));
	}

	public static void responseXml(HttpServletResponseProxy response, String text) {
		response(response, "text/xml;charset=UTF-8", text);
	}

	public static void responseHTML(HttpServletResponseProxy response, String text) {
		response(response, "text/html;charset=UTF-8", text);
	}

	public static void responseText(HttpServletResponseProxy response, String text) {
		response(response, "text/plain;charset=UTF-8", text);
	}

	public static void accessDenied(HttpServletResponseProxy response, RASPModuleType moduleType, String text) {
		if (StringUtils.isNotEmpty(text) && moduleType != null) {
			text = StringUtils.replaceAll(
					text, new String[]{"${attack.name}", "${agent.name}", "${attack.desc}", "攻击攻击"},
					new String[]{moduleType.getModuleName(), AGENT_NAME, moduleType.getModuleDesc(), "攻击"}
			);
		}

		response(response, "text/html;charset=UTF-8", text);
	}

	public static void response(HttpServletResponseProxy response, String contentType, String text) {
		if (response != null && text != null) {
			response.setContentType(contentType);
			PrintWriter out = null;

			try {
				out = response.getWriter();
				out.write(text);
				out.flush();
			} catch (Exception e) {
				AGENT_LOGGER.error(AGENT_NAME + "返回信息[" + text + "]异常:" + e, e);
			} finally {
				IOUtils.closeQuietly(out);
			}
		}
	}

	public static String getResponseEncoding(HttpServletResponseProxy response) {
		String encoding = response.getCharacterEncoding();

		return encoding != null ? encoding : "UTF-8";
	}

}
