package org.javaweb.rasp.commons.utils;

import java.util.Collection;

public class ArrayUtils extends org.apache.commons.lang3.ArrayUtils {

	public static boolean isEmpty(Collection<?> collection) {
		return collection == null || collection.isEmpty();
	}

}
