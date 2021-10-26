package org.javaweb.rasp.commons.loader.hooks;

/**
 * Hook点处理结果
 * Creator: yz
 * Date: 2019-06-24
 */
public enum HookResultType {

	RETURN,// 直接返回什么都不做
	THROW, // 抛出异常
	REPLACE_OR_BLOCK // 阻断或替换值

}
