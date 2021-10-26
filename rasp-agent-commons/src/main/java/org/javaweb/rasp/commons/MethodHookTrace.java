package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.loader.hooks.HookEvent;

import java.util.Arrays;

import static java.lang.System.nanoTime;
import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;

/**
 * Hook方法trace
 */
public class MethodHookTrace {

	/**
	 * Hook HASH值
	 */
	private final int hookHash;

	/**
	 * Hook类方法事件（方法进入、方法退出、方法异常）
	 */
	private final int thisMethodEvent;

	/**
	 * 参数HashCode
	 */
	private final int argsHashCode;

	/**
	 * 返回值HashCode
	 */
	private final int returnValueHashCode;

	/**
	 * Hook回调的防御模块类名称
	 */
	private final String targetClassName;

	/**
	 * 记录请求开始的纳秒
	 */
	private long startNanoTime;

	/**
	 * 记录请求结束时的纳秒
	 */
	private long endNanoTime;

	/**
	 * 触发Hook类HashCode
	 */
	private final int thisClassHashCode;

	public MethodHookTrace(HookEvent e, boolean isClassNameHash) {
		this.hookHash = e.getHookHash();
		this.thisMethodEvent = e.getThisMethodEvent();
		this.argsHashCode = Arrays.hashCode(e.getThisArgs());

		// 初始化Hook事件时间，Debug模式需要计算时间差
		if (AGENT_LOGGER.isDebugEnabled()) {
			this.startNanoTime = nanoTime();
		}

		if (e.getThisReturnValue() != null) {
			this.returnValueHashCode = e.getThisReturnValue().hashCode();
		} else {
			this.returnValueHashCode = 0;
		}

		this.targetClassName = e.getTargetClassName();

		// 默认不需要根据hook的类
		this.thisClassHashCode = isClassNameHash ? e.getThisClass().hashCode() : "".hashCode();
	}

	public int getHookHash() {
		return hookHash;
	}

	public int getArgsHashCode() {
		return argsHashCode;
	}

	public int getReturnValueHashCode() {
		return returnValueHashCode;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

	public int getThisMethodEvent() {
		return thisMethodEvent;
	}

	public long getStartNanoTime() {
		return startNanoTime;
	}

	public void setStartNanoTime(long startNanoTime) {
		this.startNanoTime = startNanoTime;
	}

	public long getEndNanoTime() {
		return endNanoTime;
	}

	public void setEndNanoTime(long endNanoTime) {
		this.endNanoTime = endNanoTime;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		MethodHookTrace trace = (MethodHookTrace) o;

		if (hookHash != trace.hookHash) return false;
		if (argsHashCode != trace.argsHashCode) return false;
		if (returnValueHashCode != trace.returnValueHashCode) return false;
		if (thisMethodEvent != trace.thisMethodEvent) return false;

		return thisClassHashCode == trace.thisClassHashCode;
	}

	@Override
	public int hashCode() {
		int result = hookHash;
		result = 31 * result + thisMethodEvent;
		result = 31 * result + argsHashCode;
		result = 31 * result + returnValueHashCode;
		result = 31 * result + thisClassHashCode;

		return result;
	}

}
