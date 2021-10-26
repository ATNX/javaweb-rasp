package org.javaweb.rasp.commons.utils;

import java.io.UnsupportedEncodingException;

public class RC4Utils {

	/**
	 * RC4解密
	 *
	 * @param data 加密内容
	 * @param key  加密key
	 * @return
	 */
	public static String decryptionRC4(byte[] data, String key) {
		if (data == null || key == null) {
			return null;
		}

		try {
			return new String(RC4Base(data, key), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * RC4加密
	 *
	 * @param data 需加密的内容
	 * @param key  加密KEY
	 * @return
	 */
	public static byte[] encryptionRC4Byte(String data, String key) {
		if (data == null || key == null) {
			return null;
		}

		byte[] bytes = null;

		try {
			bytes = data.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		if (bytes == null) {
			return null;
		}

		return RC4Base(bytes, key);
	}

	private static byte[] initKey(String aKey) {
		byte[] bytes = null;

		try {
			bytes = aKey.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] state = new byte[256];
		for (int i = 0; i < 256; i++) {
			state[i] = ((byte) i);
		}

		int index1 = 0;
		int index2 = 0;

		if (bytes == null || bytes.length == 0) {
			return null;
		}

		for (int i = 0; i < 256; i++) {
			index2 = (bytes[index1] & 0xFF) + (state[i] & 0xFF) + index2 & 0xFF;
			byte tmp = state[i];
			state[i] = state[index2];
			state[index2] = tmp;
			index1 = (index1 + 1) % bytes.length;
		}

		return state;
	}

	private static byte[] RC4Base(byte[] input, String mKkey) {
		int    x   = 0;
		int    y   = 0;
		byte[] key = initKey(mKkey);

		byte[] result = new byte[input.length];

		for (int i = 0; i < input.length; i++) {
			x = x + 1 & 0xFF;
			y = (key[x] & 0xFF) + y & 0xFF;

			byte tmp = key[x];
			key[x] = key[y];
			key[y] = tmp;
			int xorIndex = (key[x] & 0xFF) + (key[y] & 0xFF) & 0xFF;
			result[i] = ((byte) (input[i] ^ key[xorIndex]));
		}

		return result;
	}

}