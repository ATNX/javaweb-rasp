package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.context.RASPHttpRequestContext;

/**
 * Http请求安全验证
 * Created by yz on 2017/1/18.
 *
 * @author yz
 */
public interface RASPHttpRequestFilter {

	/**
	 * 参数过滤
	 *
	 * @param context RASP上下文
	 * @param event   Hook事件
	 * @return 过滤结果
	 */
	boolean filter(RASPHttpRequestContext context, MethodHookEvent event);

	/**
	 * 获取模块名称
	 *
	 * @return 模块名称
	 */
	RASPModuleType getModuleName();

}
