package org.javaweb.rasp.commons.hooks;

import org.javaweb.rasp.commons.RASPModuleType;

import static java.lang.String.format;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;

/**
 * Creator: yz
 * Date: 2019-07-31
 */
public class RASPHookException extends Exception {

	private static final String EXCEPTION_DESC = AGENT_NAME + "检测到恶意攻击类型:[%s]，" +
			"您的请求可能包含了恶意攻击行为,请勿尝试非法攻击!";

	public RASPHookException(RASPModuleType modulesType) {
		super(format(EXCEPTION_DESC, modulesType.getModuleDesc()));
	}

	public RASPHookException(String type) {
		super(format(EXCEPTION_DESC, type));
	}

}
