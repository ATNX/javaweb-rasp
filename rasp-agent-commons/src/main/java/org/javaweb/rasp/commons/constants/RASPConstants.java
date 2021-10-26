package org.javaweb.rasp.commons.constants;

import org.javaweb.rasp.commons.loader.hooks.HookResult;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.DOTALL;
import static org.javaweb.rasp.commons.loader.hooks.HookResultType.RETURN;

/**
 * RASP 全局常量定义
 */
public class RASPConstants {

	/**
	 * 指定ASM版本号：ASM 9
	 */
	public static final int ASM_VERSION = 9 << 16;

	/**
	 * Class OPCode
	 */
	public static final int ACC_INTERFACE = 0x0200;

	/**
	 * 正则表达式多行忽略大小写匹配
	 */
	public static final int MULTIPLE_LINE_CASE_INSENSITIVE = DOTALL | CASE_INSENSITIVE;

	/**
	 * 通用的白名单过滤正则表达式
	 */
	public static final Pattern WHITELIST_PATTERN = Pattern.compile("^[a-zA-Z0-9_$.]+$");

	/**
	 * 类构造方法
	 */
	public static final String CONSTRUCTOR_INIT = "<init>";

	/**
	 * Hook 默认的返回功能
	 */
	public static final HookResult<?> DEFAULT_HOOK_RESULT = new HookResult<Object>(RETURN);

}