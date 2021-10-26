package org.javaweb.rasp.commons;

import java.lang.annotation.*;

/**
 * API控制器注解
 * Created by yz on 2017/1/19.
 *
 * @author yz
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Controller {

	/**
	 * 控制器名称 请求RASP API需要指定控制器名称
	 */
	String name() default "";

}
