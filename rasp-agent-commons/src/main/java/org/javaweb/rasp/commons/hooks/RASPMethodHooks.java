package org.javaweb.rasp.commons.hooks;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RASPMethodHooks {

	RASPMethodHook[] value() default {};

}
