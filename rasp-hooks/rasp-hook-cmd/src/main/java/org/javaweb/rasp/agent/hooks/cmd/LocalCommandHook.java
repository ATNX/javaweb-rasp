package org.javaweb.rasp.agent.hooks.cmd;

import org.javaweb.rasp.commons.MethodHookEvent;
import org.javaweb.rasp.commons.RASPModuleType;
import org.javaweb.rasp.commons.hooks.RASPClassHook;
import org.javaweb.rasp.commons.hooks.RASPMethodAdvice;
import org.javaweb.rasp.commons.hooks.RASPMethodHook;
import org.javaweb.rasp.commons.loader.hooks.HookResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.javaweb.rasp.agent.hooks.cmd.handler.LocalCommandHookHandler.processCommand;
import static org.javaweb.rasp.commons.RASPLogger.moduleErrorLog;
import static org.javaweb.rasp.commons.constants.RASPConstants.DEFAULT_HOOK_RESULT;
import static org.javaweb.rasp.commons.utils.ReflectionUtils.invokeFieldProxy;

/**
 * RASP防御本地系统命令执行示例
 */
public class LocalCommandHook implements RASPClassHook {

	public static final RASPModuleType cmdType = new RASPModuleType("cmd", "本地命令执行");

	/**
	 * Hook ProcessBuilder类的start方法
	 */
	@RASPMethodHook(className = "java.lang.ProcessBuilder", methodName = "start", requireRequest = false)
	public static class ProcessBuilderHook extends RASPMethodAdvice {

		@Override
		public HookResult<?> onMethodEnter(MethodHookEvent event) {
			Object obj = event.getThisObject();

			try {
				// 获取ProcessBuilder类的command变量值
				List<String> commandList = new ArrayList<String>();
				List<String> command     = invokeFieldProxy(obj, "command");

				if (command != null) {
					commandList.addAll(command);
				}

				// 获取ProcessBuilder类的environment变量
				Map<String, String> environment = invokeFieldProxy(obj, "environment");

				if (environment != null) {
					for (String key : environment.keySet()) {
						commandList.add(key);
						commandList.add(environment.get(key));
					}
				}

				// 调用processCommand方法，检测执行的本地命令合法性
				return processCommand(commandList, event);
			} catch (Exception e) {
				moduleErrorLog(cmdType, e);
			}

			return DEFAULT_HOOK_RESULT;
		}

	}

}