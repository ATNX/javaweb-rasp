package org.javaweb.rasp.commons.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class IPV4Utils {

	private static final int INADDR4SZ = 4;

	private static final int INADDR16SZ = 16;

	private static final int INT16SZ = 2;

	/*
	 * Converts IPv4 address in its textual presentation form
	 * into its numeric binary form.
	 *
	 * @param src a String representing an IPv4 address in standard format
	 * @return a byte array representing the IPv4 numeric address
	 */
	@SuppressWarnings("fallthrough")
	public static byte[] textToNumericFormatV4(String src) {
		if (src == null || !src.contains(".")) {
			return null;
		}

		byte[] res = new byte[INADDR4SZ];

		long    tmpValue = 0;
		int     currByte = 0;
		boolean newOctet = true;

		int len = src.length();

		if (len > 15) {
			return null;
		}

		/*
		 * When only one part is given, the value is stored directly in
		 * the network address without any byte rearrangement.
		 *
		 * When a two part address is supplied, the last part is
		 * interpreted as a 24-bit quantity and placed in the right
		 * most three bytes of the network address. This makes the
		 * two part address format convenient for specifying Class A
		 * network addresses as net.host.
		 *
		 * When a three part address is specified, the last part is
		 * interpreted as a 16-bit quantity and placed in the right
		 * most two bytes of the network address. This makes the
		 * three part address format convenient for specifying
		 * Class B net- work addresses as 128.net.host.
		 *
		 * When four parts are specified, each is interpreted as a
		 * byte of data and assigned, from left to right, to the
		 * four bytes of an IPv4 address.
		 *
		 * We determine and parse the leading parts, if any, as single
		 * byte values in one pass directly into the resulting byte[],
		 * then the remainder is treated as a 8-to-32-bit entity and
		 * translated into the remaining bytes in the array.
		 */
		for (int i = 0; i < len; i++) {
			char c = src.charAt(i);
			if (c == '.') {
				if (newOctet || tmpValue < 0 || tmpValue > 0xff || currByte == 3) {
					return null;
				}
				res[currByte++] = (byte) (tmpValue & 0xff);
				tmpValue = 0;
				newOctet = true;
			} else {
				int digit = Character.digit(c, 10);
				if (digit < 0) {
					return null;
				}
				tmpValue *= 10;
				tmpValue += digit;
				newOctet = false;
			}
		}
		if (newOctet || tmpValue < 0 || tmpValue >= (1L << ((4 - currByte) * 8))) {
			return null;
		}
		switch (currByte) {
			case 0:
				res[0] = (byte) ((tmpValue >> 24) & 0xff);
			case 1:
				res[1] = (byte) ((tmpValue >> 16) & 0xff);
			case 2:
				res[2] = (byte) ((tmpValue >> 8) & 0xff);
			case 3:
				res[3] = (byte) ((tmpValue) & 0xff);
		}
		return res;
	}

	/*
	 * Convert IPv6 presentation level address to network order binary form.
	 * credit:
	 *  Converted from C code from Solaris 8 (inet_pton)
	 *
	 * Any component of the string following a per-cent % is ignored.
	 *
	 * @param src a String representing an IPv6 address in textual format
	 * @return a byte array representing the IPv6 numeric address
	 */
	public static byte[] textToNumericFormatV6(String src) {
		// Shortest valid string is "::", hence at least 2 chars
		if (src == null || src.length() < 2 || !src.contains(":")) {
			return null;
		}

		int     colonp;
		char    ch;
		boolean saw_xdigit;
		int     val;
		char[]  srcb = src.toCharArray();
		byte[]  dst  = new byte[INADDR16SZ];

		int srcb_length = srcb.length;
		int pc          = src.indexOf('%');
		if (pc == srcb_length - 1) {
			return null;
		}

		if (pc != -1) {
			srcb_length = pc;
		}

		colonp = -1;
		int i = 0, j = 0;
		/* Leading :: requires some special handling. */
		if (srcb[i] == ':')
			if (srcb[++i] != ':')
				return null;
		int curtok = i;
		saw_xdigit = false;
		val = 0;
		while (i < srcb_length) {
			ch = srcb[i++];
			int chval = Character.digit(ch, 16);
			if (chval != -1) {
				val <<= 4;
				val |= chval;
				if (val > 0xffff)
					return null;
				saw_xdigit = true;
				continue;
			}
			if (ch == ':') {
				curtok = i;
				if (!saw_xdigit) {
					if (colonp != -1)
						return null;
					colonp = j;
					continue;
				} else if (i == srcb_length) {
					return null;
				}
				if (j + INT16SZ > INADDR16SZ)
					return null;
				dst[j++] = (byte) ((val >> 8) & 0xff);
				dst[j++] = (byte) (val & 0xff);
				saw_xdigit = false;
				val = 0;
				continue;
			}
			if (ch == '.' && ((j + INADDR4SZ) <= INADDR16SZ)) {
				String ia4 = src.substring(curtok, srcb_length);
				/* check this IPv4 address has 3 dots, ie. A.B.C.D */
				int dot_count = 0, index = 0;
				while ((index = ia4.indexOf('.', index)) != -1) {
					dot_count++;
					index++;
				}
				if (dot_count != 3) {
					return null;
				}
				byte[] v4addr = textToNumericFormatV4(ia4);
				if (v4addr == null) {
					return null;
				}
				for (int k = 0; k < INADDR4SZ; k++) {
					dst[j++] = v4addr[k];
				}
				saw_xdigit = false;
				break;  /* '\0' was seen by inet_pton4(). */
			}
			return null;
		}
		if (saw_xdigit) {
			if (j + INT16SZ > INADDR16SZ)
				return null;
			dst[j++] = (byte) ((val >> 8) & 0xff);
			dst[j++] = (byte) (val & 0xff);
		}

		if (colonp != -1) {
			int n = j - colonp;

			if (j == INADDR16SZ)
				return null;
			for (i = 1; i <= n; i++) {
				dst[INADDR16SZ - i] = dst[colonp + n - i];
				dst[colonp + n - i] = 0;
			}
			j = INADDR16SZ;
		}
		if (j != INADDR16SZ)
			return null;
		byte[] newdst = convertFromIPv4MappedAddress(dst);
		if (newdst != null) {
			return newdst;
		} else {
			return dst;
		}
	}

	/*
	 * Convert IPv4-Mapped address to IPv4 address. Both input and
	 * returned value are in network order binary form.
	 *
	 * @param src a String representing an IPv4-Mapped address in textual format
	 * @return a byte array representing the IPv4 numeric address
	 */
	public static byte[] convertFromIPv4MappedAddress(byte[] addr) {
		if (isIPv4MappedAddress(addr)) {
			byte[] newAddr = new byte[INADDR4SZ];
			System.arraycopy(addr, 12, newAddr, 0, INADDR4SZ);
			return newAddr;
		}

		return null;
	}

	/**
	 * Utility routine to check if the InetAddress is an
	 * IPv4 mapped IPv6 address.
	 *
	 * @return a <code>boolean</code> indicating if the InetAddress is
	 * an IPv4 mapped IPv6 address; or false if address is IPv4 address.
	 */
	private static boolean isIPv4MappedAddress(byte[] addr) {
		if (addr.length < INADDR16SZ) {
			return false;
		}

		return (addr[0] == 0x00) && (addr[1] == 0x00) &&
				(addr[2] == 0x00) && (addr[3] == 0x00) &&
				(addr[4] == 0x00) && (addr[5] == 0x00) &&
				(addr[6] == 0x00) && (addr[7] == 0x00) &&
				(addr[8] == 0x00) && (addr[9] == 0x00) &&
				(addr[10] == (byte) 0xff) &&
				(addr[11] == (byte) 0xff);
	}

	public static long ipV4ToLong(byte[] addr) {
		int address = addr[3] & 0xFF;
		address |= ((addr[2] << 8) & 0xFF00);
		address |= ((addr[1] << 16) & 0xFF0000);
		address |= ((addr[0] << 24) & 0xFF000000);

		return address;
	}


	public static boolean isIPV4SiteLocalAddress(long address) {
		// refer to RFC 1918
		// 10/8 prefix
		// 172.16/12 prefix
		// 192.168/16 prefix
		return (((address >>> 24) & 0xFF) == 10)
				|| ((((address >>> 24) & 0xFF) == 172)
				&& (((address >>> 16) & 0xF0) == 16))
				|| ((((address >>> 24) & 0xFF) == 192)
				&& (((address >>> 16) & 0xFF) == 168));
	}

	public static boolean isIPV6SiteLocalAddress(byte[] ipaddress) {
		return (ipaddress[0] & 0xff) == 0xfe && (ipaddress[1] & 0xc0) == 0xc0;
	}

	/**
	 * 检查IP地址是否是局域网或本机地址
	 *
	 * @param ip IP地址
	 * @return 是否是LAN IP
	 */
	public static boolean isLanIP(String ip) {
		if (ip == null) return false;

		// 排除本地IP地址
		if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) return true;

		if (ip.contains(".")) {
			byte[] ipAddress = textToNumericFormatV4(ip);

			if (ipAddress != null) {
				return isIPV4SiteLocalAddress(ipV4ToLong(ipAddress));
			}
		} else if (ip.contains(":")) {
			byte[] ipAddress = textToNumericFormatV6(ip);

			if (ipAddress != null) {
				return isIPV6SiteLocalAddress(ipAddress);
			}
		}

		return false;
	}

	/**
	 * 获取本地IP地址 主机名无法ping通的情况下可以用此方法获取本地IP地址。
	 *
	 * @return InetAddress
	 * @throws UnknownHostException 获取主机异常
	 */
	public static InetAddress getLocalHostLANAddress() throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
				NetworkInterface iface = (NetworkInterface) ifaces.nextElement();

				for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
					InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();

					if (!inetAddr.isLoopbackAddress()) {
						if (inetAddr.isSiteLocalAddress()) {
							return inetAddr;
						} else if (candidateAddress == null) {
							candidateAddress = inetAddr;
						}
					}
				}
			}

			if (candidateAddress != null) {
				return candidateAddress;
			}

			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException("The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}

			return jdkSuppliedAddress;
		} catch (Exception e) {
			UnknownHostException unknownHostException = new UnknownHostException("Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}

}