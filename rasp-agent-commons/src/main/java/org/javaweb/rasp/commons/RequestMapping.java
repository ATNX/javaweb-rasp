package org.javaweb.rasp.commons;

import java.lang.annotation.*;

/**
 * RequestMapping 请求映射 必须定义在Controller类有效
 * Created by yz on 2017/1/19.
 *
 * @author yz
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

	/**
	 * API请求中的action
	 */
	String action() default "";

}
