package org.javaweb.rasp.commons.hooks;

import java.lang.annotation.*;

/**
 * RASP 类方法Hook注解
 * Creator: yz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RASPMethodHook {

	/**
	 * 需要Hook的类名
	 */
	String className() default "";

	/**
	 * 需要Hook的类
	 */
	Class<?> onClass() default void.class;

	/**
	 * 需要Hook的父类类名数组，父类Hook时类名不允许使用正则表达式
	 *
	 * @return Hook的父类
	 */
	String[] superClasses() default "java.lang.Object";

	/**
	 * 是否必须依赖Http请求，默认除了容器的请求入口Hook以外其他的Hook点应当都会依赖请求
	 *
	 * @return 是否忽略JDK内置的API
	 */
	boolean requireRequest() default true;

	/**
	 * 是否是Servlet请求入口，只有在Hook Servlet/Filter时才需要修改该值
	 *
	 * @return 是否是Servlet请求入口
	 */
	boolean requestEntry() default false;

	/**
	 * 是否忽略JDK内置的API类Hook
	 *
	 * @return 是否忽略JDK内置的API
	 */
	boolean ignoreJDKInternalClass() default false;

	/**
	 * 是否忽略父类自身，如果使用了父类Hook，配置该选项可设置是否将父类也同时Hook了
	 *
	 * @return 是否忽略父类自身Hook
	 */
	boolean ignoreSuperClassSelf() default false;

	/**
	 * 类名是否使用正则表达式匹配，父类Hook时类名不允许使用正则表达式
	 */
	boolean classNameRegexp() default false;

	/**
	 * 需要Hook的方法名
	 */
	String methodName();

	/**
	 * 方法名是否使用正则表达式匹配
	 */
	boolean methodNameRegexp() default false;

	/**
	 * 需要Hook的方法参数名数组
	 */
	String[] methodArgs() default {};

	/**
	 * 需要Hook的方法名描述符,如果配置此项需要使用方法描述符方式写,
	 * 如:Ljava/lang/String;来表示接收个字符串类型参数
	 *
	 * @return 方法描述符
	 */
	String methodArgsDesc() default "";

	/**
	 * 需要Hook的方法名参数类型,需要保持和原方法的顺序一致
	 */
	Class[] onMethodArgsDesc() default {};

	/**
	 * 方法名是否使用正则表达式描述符
	 */
	boolean methodDescRegexp() default false;

	/**
	 * 设置Hook调用时生成的hashcode中是否需要添加类名条件
	 */
	boolean classNameHash() default false;

}
