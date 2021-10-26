package org.javaweb.rasp.commons;

import org.javaweb.rasp.commons.cache.RASPCachedParameter;
import org.javaweb.rasp.commons.context.RASPHttpRequestContext;
import org.javaweb.rasp.commons.loader.hooks.HookResult;

/**
 * Creator: yz
 * Date: 2019-08-01
 */
public abstract class RASPParameterFilter {

	public abstract HookResult<String[]> filter(RASPCachedParameter parameter,
	                                            RASPHttpRequestContext context, MethodHookEvent event);

	public abstract RASPModuleType getModuleName();

}
