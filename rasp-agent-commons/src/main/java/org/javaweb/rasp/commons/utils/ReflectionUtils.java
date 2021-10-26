package org.javaweb.rasp.commons.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.javaweb.rasp.commons.config.RASPConfiguration.AGENT_LOGGER;
import static org.javaweb.rasp.commons.loader.AgentConstants.AGENT_NAME;

public final class ReflectionUtils {

	public static final Class<?>[] EMPTY_ARGS = new Class<?>[0];

	public static final Class<?>[] BOOLEAN_CLASS_ARG = new Class[]{boolean.class};

	public static final Class<?>[] STRING_CLASS_ARG = new Class[]{String.class};

	public static final Class<?>[] STRING_STRING_CLASS_ARG = new Class[]{String.class, String.class};

	public static final Class<?>[] INT_STRING_CLASS_ARG = new Class[]{int.class, String.class};

	public static final Class<?>[] STRING_OBJECT_CLASS_ARG = new Class[]{String.class, Object.class};

	public static final Class<?>[] INT_CLASS_ARG = new Class[]{int.class};

	public static final Class<?>[] BYTE_ARRAY_CLASS_ARG = new Class[]{byte[].class};

	public static final Class<?>[] STREAM_CLASS_ARG = new Class[]{byte[].class, int.class, int.class};

	private static final Map<Integer, Map<String, Method>> CACHE_CLASS_METHOD_MAP =
			new ConcurrentHashMap<Integer, Map<String, Method>>();

	private static final Map<Integer, Map<String, Field>> CACHE_CLASS_FIELD_MAP =
			new ConcurrentHashMap<Integer, Map<String, Field>>();

	private static String getMethodDescriptor(String method, Class<?>... classes) {
		return method + Arrays.toString(classes);
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... argTypes) throws NoSuchMethodException {
		int                 hashCode   = System.identityHashCode(clazz);
		Map<String, Method> methodMap  = CACHE_CLASS_METHOD_MAP.get(hashCode);
		String              methodDesc = getMethodDescriptor(name, argTypes);

		if (methodMap != null) {
			Method method = methodMap.get(methodDesc);

			if (method != null) {
				return methodMap.get(methodDesc);
			}
		} else {
			methodMap = new HashMap<String, Method>();
		}

		Method method;

		try {
			method = clazz.getMethod(name, argTypes);
		} catch (NoSuchMethodException e) {
			method = clazz.getDeclaredMethod(name, argTypes);
		}

		method.setAccessible(true);

		methodMap.put(methodDesc, method);

		// 缓存类方法
		CACHE_CLASS_METHOD_MAP.put(hashCode, methodMap);

		return method;
	}

	public static Object invokeMethod(Object instance, String name, Class<?>[] argTypes, Object... args)
			throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

		Method method = getMethod(instance.getClass(), name, argTypes);

		return method.invoke(instance, args);
	}

	public static <T> T invokeProxyMethod(Object instance, String name) {
		return invokeProxyMethod(instance, name, EMPTY_ARGS);
	}

	public static <T> T invokeProxyMethod(Object instance, String name, Class<?>[] argTypes, Object... args) {
		return invokeMethodProxy(instance, name, argTypes, args);
	}

	public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
		int                hashCode = System.identityHashCode(clazz);
		Map<String, Field> fieldMap = CACHE_CLASS_FIELD_MAP.get(hashCode);

		if (fieldMap != null) {
			Field method = fieldMap.get(name);

			if (method != null) {
				return fieldMap.get(name);
			}
		} else {
			fieldMap = new HashMap<String, Field>();
		}

		Field field;

		try {
			field = clazz.getField(name);
		} catch (NoSuchFieldException e) {
			field = clazz.getDeclaredField(name);
		}

		field.setAccessible(true);

		fieldMap.put(name, field);

		// 缓存类方法
		CACHE_CLASS_FIELD_MAP.put(hashCode, fieldMap);

		return field;
	}

	public static Object invokeField(Object instance, String name)
			throws NoSuchFieldException, IllegalAccessException {

		Field field = getField(instance.getClass(), name);

		return field.get(instance);
	}

	public static <T> T invokeMethodProxy(Object instance, String name, Class<?>[] argTypes, Object... args) {
		try {
			return (T) invokeMethod(instance, name, argTypes, args);
		} catch (Exception e) {
			AGENT_LOGGER.debug(AGENT_NAME + "调用Proxy：" + instance.getClass().getName() + "，方法异常：" + e, e);
			return null;
		}
	}

	public static <T> T invokeFieldProxy(Object instance, String name) {
		try {
			return (T) invokeField(instance, name);
		} catch (Exception e) {
			AGENT_LOGGER.debug(AGENT_NAME + "调用Proxy：" + instance.getClass().getName() + "，成员变量异常：" + e, e);
			return null;
		}
	}

}
