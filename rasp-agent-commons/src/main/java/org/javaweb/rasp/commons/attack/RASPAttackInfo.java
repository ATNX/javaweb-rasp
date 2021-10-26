package org.javaweb.rasp.commons.attack;

import org.javaweb.rasp.commons.MethodHookEvent;
import org.javaweb.rasp.commons.RASPModuleType;

import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_PACKAGE_PREFIX;

/**
 * Web攻击详情信息
 * Created by yz on 2017/2/1.
 *
 * @author yz
 */
public class RASPAttackInfo {

	/**
	 * 攻击类型
	 */
	private transient RASPModuleType raspModuleType;

	/**
	 * 攻击类型,如: SQL注入文件上传
	 */
	private final String type;

	/**
	 * 攻击参数
	 */
	private final String parameter;

	/**
	 * 攻击的具体参数值
	 */
	private final String[] values;

	/**
	 * 发现攻击的具体位置,如: HEADER
	 */
	private final RASPParameterPosition position;

	/**
	 * RASP Hook方法信息
	 */
	private final RASPMethodHookInfo methodHookInfo;

	/**
	 * 是否阻断请求
	 */
	private final boolean blockRequest;

	/**
	 * Hook调用链
	 */
	private String traceElements;

	private static final String HOOK_PROXY_CLASS_NAME = AGENT_PACKAGE_PREFIX + "loader.hooks.HookProxy";

	public RASPAttackInfo(RASPModuleType raspModuleType, String parameter, String[] values,
	                      RASPParameterPosition position, MethodHookEvent event, boolean blockRequest) {

		this.raspModuleType = raspModuleType;
		this.type = raspModuleType.getModuleName();
		this.parameter = parameter;
		this.values = values;
		this.position = position;
		this.methodHookInfo = new RASPMethodHookInfo(event);
		this.blockRequest = blockRequest;
		this.initTraceElements();
	}

	public RASPAttackInfo(RASPModuleType raspModuleType, String parameter, String value,
	                      RASPParameterPosition position, MethodHookEvent event, boolean blockRequest) {

		this.raspModuleType = raspModuleType;
		this.type = raspModuleType.getModuleName();
		this.parameter = parameter;
		this.values = new String[]{value};
		this.position = position;
		this.methodHookInfo = new RASPMethodHookInfo(event);
		this.blockRequest = blockRequest;
		this.initTraceElements();
	}

	private void initTraceElements() {
		// 检测当前防御模块是否需要打印调用链
		if (!raspModuleType.isPrintTrace()) {
			return;
		}

		StringBuilder       sb       = new StringBuilder();
		StackTraceElement[] elements = Thread.currentThread().getStackTrace();

		boolean traceBegin = false;

		// 移除无用信息，从org.javaweb.rasp.loader.hooks.RASPHookProxy之前的调用链开始输出
		for (int i = 1; i < elements.length; i++) {
			StackTraceElement traceElement   = elements[i];
			String            traceClassName = traceElement.getClassName();

			if (traceBegin) {
				sb.append(traceClassName).append(" (")
						.append(traceElement.getFileName())
						.append(":")
						.append(traceElement.getLineNumber()).append(")")
						.append("\r\n");
			}

			if (traceClassName.equals(HOOK_PROXY_CLASS_NAME)) {
				traceBegin = true;
			}
		}

		this.traceElements = sb.toString();
	}

	/**
	 * 获取攻击类型
	 *
	 * @return 攻击类型
	 */
	public RASPModuleType getRaspModuleType() {
		return raspModuleType;
	}

	/**
	 * 设置攻击类型
	 *
	 * @param type 攻击类型
	 */
	public void setRaspModuleType(RASPModuleType type) {
		this.raspModuleType = type;
	}

	public String getType() {
		return type;
	}

	/**
	 * 获取攻击参数
	 *
	 * @return 攻击参数
	 */
	public String getParameter() {
		return parameter;
	}

	/**
	 * 获取攻击参数值
	 *
	 * @return 攻击参数值
	 */
	public String[] getValues() {
		return values;
	}

	/**
	 * 获取攻击发生的位置
	 *
	 * @return 攻击发生位置
	 */
	public RASPParameterPosition getPosition() {
		return position;
	}

	/**
	 * RASP获取方法Hook信息
	 *
	 * @return Hook信息
	 */
	public RASPMethodHookInfo getMethodHookInfo() {
		return methodHookInfo;
	}

	public boolean isBlockRequest() {
		return blockRequest;
	}

	public String getTraceElements() {
		return traceElements;
	}

}
