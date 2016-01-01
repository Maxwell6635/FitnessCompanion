package com.choicemmed.a30.command;

public class AnayizeUtil {

	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(低位在前，高位在后)的顺序。 和bytesToInt（）配套使用
	 * 
	 * @param value
	 *            要转换的int值
	 * @return byte数组
	 */
	public static byte[] intToBytes(int value) {
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

	/**
	 * 将int数值转换为占四个字节的byte数组，本方法适用于(高位在前，低位在后)的顺序。 和bytesToInt2（）配套使用
	 */
	public static byte[] intToBytes2(int value) {
		byte[] src = new byte[4];
		src[0] = (byte) ((value >> 24) & 0xFF);
		src[1] = (byte) ((value >> 16) & 0xFF);
		src[2] = (byte) ((value >> 8) & 0xFF);
		src[3] = (byte) (value & 0xFF);
		return src;
	}

	// 十六进制转byte
	public static String bytes2HexString(byte[] a) {

		int len = a.length;
		byte[] b = new byte[len];
		for (int k = 0; k < len; k++) {
			b[k] = a[a.length - 1 - k];
		}

		String ret = "";
		for (int i = 0; i < len; i++) {
			// String hex = Integer .toHexString(b[ i ]);
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret += hex.toUpperCase();
		}

		return ret;
	}

	/**
	 * 小端操作 int----〉hex
	 * 
	 * @param value
	 * @return
	 */
	public static String int2HexString(int value) {
		String hex = String.format("%08X", ((value >> 24) & 0xFF)
				+ (((value >> 16) & 0xFF) << 8) + (((value >> 8) & 0xFF) << 16)
				+ ((value & 0xFF) << 24));

		return hex;
	}

	public static byte[] getHexBytes(String cmd) {
		int len = cmd.length() / 2;
		char[] chars = cmd.toCharArray();
		String[] hexStr = new String[len];
		byte[] bytes = new byte[len];
		for (int i = 0, j = 0; j < len; i += 2, j++) {
			hexStr[j] = "" + chars[i] + chars[i + 1];
			bytes[j] = (byte) Integer.parseInt(hexStr[j], 16);
		}
		return bytes;
	}

	public static byte[] getBytesIncludeVerifySum(String cmd) {
		byte[] value = getHexBytes(cmd);
		byte verifySum = 0;
		for (int i = 2; i < value.length; i++) {
			verifySum += value[i];
		}
		byte[] values = new byte[value.length + 1];
		for (int i = 0; i < value.length; i++) {
			values[i] = value[i];
		}
		values[value.length] = verifySum;
		return values;
	}

}
