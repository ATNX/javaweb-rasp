package org.javaweb.rasp.commons.utils;

/**
 * Created by yz on 2016-05-27.
 */
public class ClassUtils {

	/**
	 * 获取用于ASM调用的类名称
	 *
	 * @param clazz 类对象
	 * @return ASM格式的Java类名称
	 */
	public static String toAsmClassName(Class<?> clazz) {
		return clazz.getName().replace(".", "/");
	}

	/**
	 * 获取用于ASM调用的类名称
	 *
	 * @param className 类名
	 * @return ASM格式的Java类名称
	 */
	public static String toAsmClassName(String className) {
		return className.replace(".", "/");
	}

	/**
	 * 转换成Java内部命名方式
	 *
	 * @param className 类名
	 * @return Java类格式的类名称
	 */
	public static String toJavaName(String className) {
		return className != null ? className.replace("/", ".") : null;
	}

	/**
	 * 获取参数描述符,接收参数类型的全类名，如果是参数是数组类型的那么直接在类型后面加上一对"[]"就可以了,
	 * 如"java.lang.String[]",如果是基础类型直接写就行了，如参数类型是int,那么直接传入:"int"就行了。
	 * 需要特别注意的是类名一定不能写错,"[]"也一定不能加错，否则无法正常匹配。
	 *
	 * @param classes 参数类型
	 * @return 方法描述符
	 */
	public static String getDescriptor(final String... classes) {
		StringBuilder sb = new StringBuilder();

		for (String name : classes) {
			// 统计数组[]出现次数
			int length = name.split("\\[]", -1).length;

			for (int i = 0; i < length - 1; i++) {
				sb.append("[");
			}

			// 移除所有[]
			String className = ClassUtils.toAsmClassName(name.replace("[]", ""));

			if (Byte.TYPE.getName().equals(className)) {
				sb.append('B');
			} else if (Boolean.TYPE.getName().equals(className)) {
				sb.append('Z');
			} else if (Short.TYPE.getName().equals(className)) {
				sb.append('S');
			} else if (Character.TYPE.getName().equals(className)) {
				sb.append('C');
			} else if (Integer.TYPE.getName().equals(className)) {
				sb.append('I');
			} else if (Long.TYPE.getName().equals(className)) {
				sb.append('J');
			} else if (Double.TYPE.getName().equals(className)) {
				sb.append('D');
			} else if (Float.TYPE.getName().equals(className)) {
				sb.append('F');
			} else if (Void.TYPE.getName().equals(className)) {
				sb.append('V');
			} else {
				sb.append("L").append(className).append(";");
			}
		}

		return sb.toString();
	}

	/**
	 * Appends the descriptor of the given class to the given string builder.
	 *
	 * @param clazz         the class whose descriptor must be computed.
	 * @param stringBuilder the string builder to which the descriptor must be appended.
	 */
	private static void appendDescriptor(final Class<?> clazz, final StringBuilder stringBuilder) {
		Class<?> currentClass = clazz;

		while (currentClass.isArray()) {
			stringBuilder.append('[');
			currentClass = currentClass.getComponentType();
		}

		if (currentClass.isPrimitive()) {
			char descriptor;
			if (currentClass == Integer.TYPE) {
				descriptor = 'I';
			} else if (currentClass == Void.TYPE) {
				descriptor = 'V';
			} else if (currentClass == Boolean.TYPE) {
				descriptor = 'Z';
			} else if (currentClass == Byte.TYPE) {
				descriptor = 'B';
			} else if (currentClass == Character.TYPE) {
				descriptor = 'C';
			} else if (currentClass == Short.TYPE) {
				descriptor = 'S';
			} else if (currentClass == Double.TYPE) {
				descriptor = 'D';
			} else if (currentClass == Float.TYPE) {
				descriptor = 'F';
			} else if (currentClass == Long.TYPE) {
				descriptor = 'J';
			} else {
				throw new AssertionError();
			}
			stringBuilder.append(descriptor);
		} else {
			stringBuilder.append('L').append(toAsmClassName(currentClass.getName())).append(';');
		}
	}

	/**
	 * 获取类类型描述符
	 *
	 * @param classes 参数类型
	 * @return 方法描述符
	 */
	public static String getDescriptor(final Class<?>... classes) {
		StringBuilder buf = new StringBuilder();

		for (Class<?> clazz : classes) {
			appendDescriptor(clazz, buf);
		}

		return buf.toString();
	}

}
