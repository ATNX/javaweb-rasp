package org.javaweb.rasp.commons.utils;

import java.text.DecimalFormat;

public class NumberUtils extends org.apache.commons.lang3.math.NumberUtils {

	/**
	 * 浮点类数字格式化，去小数点
	 *
	 * @param val    值
	 * @param format 格式
	 * @return 格式化后的数字
	 */
	public static String format(double val, String format) {
		DecimalFormat numFormat = new DecimalFormat(format);
		return numFormat.format(val);
	}

	public static boolean isNum(String str) {
		return isNumber(str);
	}

	/**
	 * 格式化纳秒时间差
	 *
	 * @param diff 纳秒时间差
	 * @return 纳秒时间格式化
	 */
	public static String nanoTimeDiffFormat(double diff) {
		return format(diff / 1000 / 1000, "0.00");
	}

	/**
	 * 格式化纳秒时间差
	 *
	 * @param start 开始时间
	 * @param end   结束时间
	 * @return 格式化纳秒时间差
	 */
	public static String nanoTimeDiffFormat(double start, double end) {
		return nanoTimeDiffFormat(end - start);
	}

}
