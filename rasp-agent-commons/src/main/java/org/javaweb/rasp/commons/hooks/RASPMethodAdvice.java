package org.javaweb.rasp.commons.hooks;

import org.javaweb.rasp.commons.MethodHookEvent;
import org.javaweb.rasp.commons.loader.hooks.HookResult;

import static org.javaweb.rasp.commons.constants.RASPConstants.DEFAULT_HOOK_RESULT;

/**
 * RASP Hook方法增强
 * Creator: yz
 * Date: 2019-07-08
 */
public class RASPMethodAdvice {

	/**
	 * Hook方法进入调用此方法
	 *
	 * @return RASP Hook处理结果
	 */
	public HookResult<?> onMethodEnter(MethodHookEvent event) {
		return DEFAULT_HOOK_RESULT;
	}

	/**
	 * Hook方法退出后回调此方法
	 *
	 * @return RASP Hook处理结果
	 */
	public HookResult<?> onMethodExit(MethodHookEvent event) {
		return DEFAULT_HOOK_RESULT;
	}

	/**
	 * Hook方法抛出异常退出后回调此方法
	 *
	 * @return RASP Hook处理结果
	 */
	public HookResult<?> onMethodThrow(MethodHookEvent event) {
		return DEFAULT_HOOK_RESULT;
	}

}
