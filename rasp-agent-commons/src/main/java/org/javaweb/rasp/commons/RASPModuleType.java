package org.javaweb.rasp.commons;

/**
 * RASP 安全模块枚举类
 * Creator: yz
 * Date: 2019-07-31
 */
public class RASPModuleType {

	/**
	 * 消息编码
	 */
	private final String moduleName;

	/**
	 * 消息内容
	 */
	private final String moduleDesc;

	/**
	 * 是否打印调用链
	 */
	private boolean printTrace = true;

	public RASPModuleType(String moduleName, String moduleDesc) {
		this.moduleName = moduleName;
		this.moduleDesc = moduleDesc;
	}

	public RASPModuleType(String moduleName, String moduleDesc, boolean printTrace) {
		this.moduleName = moduleName;
		this.moduleDesc = moduleDesc;
		this.printTrace = printTrace;
	}

	/**
	 * 获取状态码
	 *
	 * @return 模块名称
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * 获取消息内容
	 *
	 * @return 模块描述
	 */
	public String getModuleDesc() {
		return moduleDesc;
	}

	/**
	 * 是否打印调用链
	 *
	 * @return 是否打印调用链
	 */
	public boolean isPrintTrace() {
		return printTrace;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		RASPModuleType that = (RASPModuleType) o;

		return moduleName.equals(that.moduleName);
	}

	@Override
	public int hashCode() {
		return moduleName.hashCode();
	}

	@Override
	public String toString() {
		return "RASPModuleType{" +
				"moduleName='" + moduleName + '\'' +
				", moduleDesc='" + moduleDesc + '\'' +
				", printTrace=" + printTrace +
				'}';
	}

}
