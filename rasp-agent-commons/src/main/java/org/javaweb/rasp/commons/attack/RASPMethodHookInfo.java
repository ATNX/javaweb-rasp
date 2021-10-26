package org.javaweb.rasp.commons.attack;

import org.javaweb.rasp.commons.MethodHookEvent;

/**
 * RASP Hook方法信息
 * Creator: yz
 * Date: 2019/9/11
 */
public class RASPMethodHookInfo {

	/**
	 * Hook类名
	 */
	private final String thisClass;

	/**
	 * Hook方法名
	 */
	private final String methodName;

	/**
	 * Hook方法参数描述符
	 */
	private final String methodArgsDesc;

	public RASPMethodHookInfo(MethodHookEvent event) {
		// 类名
		this.thisClass = event.getThisClass();

		// 方法名
		this.methodName = event.getThisMethodName();

		// 方法描述符
		this.methodArgsDesc = event.getThisMethodArgsDesc();
	}

	public String getThisClass() {
		return thisClass;
	}

	public String getMethodName() {
		return methodName;
	}

	public String getMethodArgsDesc() {
		return methodArgsDesc;
	}

}
