package org.javaweb.rasp.commons.loader.hooks;

import java.util.Arrays;

public class HookEvent {

	/**
	 * Hook类示例化对象，如果是static方法该值恒为空
	 */
	private final Object thisObject;

	/**
	 * Hook类名
	 */
	private final String thisClass;

	/**
	 * Hook类方法名
	 */
	private final String thisMethodName;

	/**
	 * Hook类方法描述符
	 */
	private final String thisMethodArgsDesc;

	/**
	 * Hook类方法参数
	 */
	private final Object[] thisArgs;

	/**
	 * Hook类方法返回值，void或方法进入事件该值为null
	 */
	private final Object thisReturnValue;

	/**
	 * Hook类方法事件（方法进入、方法退出、方法异常）
	 */
	private final int thisMethodEvent;

	/**
	 * Hook回调的防御模块类名称
	 */
	private final String targetClassName;

	/**
	 * Hook HASH值
	 */
	private final int hookHash;

	public HookEvent(Object thisObject, String thisClass, String thisMethodName, String thisMethodArgsDesc,
	                 Object[] thisArgs, Object thisReturnValue, int thisMethodEvent,
	                 String targetClassName, int hookHash) {

		this.thisObject = thisObject;
		this.thisClass = thisClass;
		this.thisMethodName = thisMethodName;
		this.thisMethodArgsDesc = thisMethodArgsDesc;
		this.thisArgs = thisArgs;
		this.thisReturnValue = thisReturnValue;
		this.thisMethodEvent = thisMethodEvent;
		this.targetClassName = targetClassName;
		this.hookHash = hookHash;
	}

	public Object getThisObject() {
		return thisObject;
	}

	public String getThisClass() {
		return thisClass;
	}

	public String getThisMethodName() {
		return thisMethodName;
	}

	public String getThisMethodArgsDesc() {
		return thisMethodArgsDesc;
	}

	public Object[] getThisArgs() {
		return thisArgs;
	}

	/**
	 * 通过传入参数数组下标获取Hook方法的单个参数值
	 *
	 * @param index 索引
	 * @param <T>   参数类型
	 * @return 索引对应的类型
	 */
	public <T> T getThisArg(int index) {
		if (thisArgs.length > index) {
			return (T) thisArgs[index];
		}

		return null;
	}

	/**
	 * 获取返回值,如果方法无返回值return null
	 *
	 * @return 返回值对象
	 */
	public <T> T getThisReturnValue() {
		return (T) thisReturnValue;
	}

	public int getThisMethodEvent() {
		return thisMethodEvent;
	}

	public String getTargetClassName() {
		return targetClassName;
	}

	public int getHookHash() {
		return hookHash;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;

		if (o == null || getClass() != o.getClass()) return false;

		HookEvent that = (HookEvent) o;

		if (hookHash != that.hookHash) return false;

		// Probably incorrect - comparing Object[] arrays with Arrays.equals
		if (!Arrays.equals(thisArgs, that.thisArgs)) return false;

		if (thisReturnValue != null ? !thisReturnValue.equals(that.thisReturnValue) : that.thisReturnValue != null)
			return false;

		return thisMethodEvent == that.thisMethodEvent;
	}

	@Override
	public int hashCode() {
		int result = Arrays.hashCode(thisArgs);
		result = 31 * result + (thisReturnValue != null ? thisReturnValue.hashCode() : 0);
		result = 31 * result + thisMethodEvent;
		result = 31 * result + hookHash;

		return result;
	}

	@Override
	public String toString() {
		return "HookEvent{" +
				"thisObject=" + thisObject +
				", thisClass='" + thisClass + '\'' +
				", thisMethodName='" + thisMethodName + '\'' +
				", thisMethodArgsDesc='" + thisMethodArgsDesc + '\'' +
				", thisArgs=" + Arrays.toString(thisArgs) +
				", thisReturnValue=" + thisReturnValue +
				", thisMethodEvent=" + thisMethodEvent +
				", targetClassName='" + targetClassName + '\'' +
				", hookHash=" + hookHash +
				'}';
	}

}
