package com.fp2p.helper;

import java.security.Key;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.sun.crypto.provider.SunJCE;

/**
 * Des加密工具类.
 * 
 * @author 侯骏雄
 * @since v1.0.0
 */
public final class DesHelper {
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(DesHelper.class
			.getName());

	/**
	 * 默认密钥.
	 */
	private static final String DEFAULT_KEY = "national";

	/**
	 * 密钥字节长度.
	 */
	private static final int LENGTH_OF_KEY_BYTE = 8;

	/**
	 * 十六进制0F对应十进制.
	 */
	private static final int HEX_0F = 15;

	/**
	 * 十六进制.
	 */
	private static final int HEX = 16;

	/**
	 * Des加密工具类的私有构造方法，其作用是为了防止用户显式生成工具类的实例对象.
	 * 
	 */
	private DesHelper() {
	}

	/**
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位.
	 * 
	 * @param arrBTmp
	 *            构成该字符串的字节数组
	 * @return 生成的密钥
	 */
	private static Key getKey(final byte[] arrBTmp) {
		logger.debug("进入getKey方法");
		// 创建一个空的8位字节数组（默认值为0）
		byte[] arrB = new byte[LENGTH_OF_KEY_BYTE];

		// 将原始字节数组转换为8位
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		// 生成密钥
		Key key = new SecretKeySpec(arrB, "DES");

		logger.debug("退出getKey方法");
		return key;
	}

	/**
	 * 将byte数组转换为表示16进制值的字符串 hexStr2ByteArr(String strIn) 互为可逆的转换过程.
	 * 
	 * @param arrB
	 *            需要转换的byte数组
	 * @return 转换后的字符串
	 */
	private static String byteArr2HexStr(final byte[] arrB) {
		logger.debug("进入byteArr2HexStr方法");
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数
			while (intTmp < 0) {
				intTmp = intTmp + ((Byte.MAX_VALUE + 1) * 2);
			}
			// 小于0F的数需要在前面补0
			if (intTmp <= HEX_0F) {
				sb.append("0");
			}
			sb.append(Integer.toHexString(intTmp));
		}
		logger.debug("退出byteArr2HexStr方法");
		return sb.toString();
	}

	/**
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)
	 * 互为可逆的转换过程.
	 * 
	 * @param strIn
	 *            需要转换的字符串
	 * @return 转换后的byte数组
	 */
	private static byte[] hexStr2ByteArr(final String strIn) {
		logger.debug("进入hexStr2ByteArr方法");
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;

		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, HEX);
		}
		logger.debug("退出hexStr2ByteArr方法");
		return arrOut;
	}

	/**
	 * 加密字符串.
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @param strKey
	 *            密钥
	 * @return 加密后的字符串
	 */
	public static String encrypt(final String strIn, final String strKey) {
		logger.debug("进入encrypt(String, String)方法");
		String result = null;
		Security.addProvider(new SunJCE());
		Key key = getKey(strKey.getBytes());
		try {
			Cipher encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, key);
			result = byteArr2HexStr(encryptCipher.doFinal(strIn.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出encrypt(String, String)方法");
		return result;
	}

	/**
	 * 加密字符串.
	 * 
	 * @param strIn
	 *            需加密的字符串
	 * @return 加密后的字符串
	 */
	public static String encrypt(final String strIn) {
		logger.debug("进入encrypt(String)方法");
		String result = null;
		result = encrypt(strIn, DEFAULT_KEY);
		logger.debug("退出encrypt(String)方法");
		return result;
	}

	/**
	 * 解密字符串.
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @param strKey
	 *            密钥
	 * @return 解密后的字符串
	 */
	public static String decrypt(final String strIn, final String strKey) {
		logger.debug("进入decrypt(String, String)方法");
		String result = null;
		Security.addProvider(new SunJCE());
		Key key = getKey(strKey.getBytes());

		try {
			Cipher decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, key);
			result = new String(decryptCipher.doFinal(hexStr2ByteArr(strIn)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.debug("退出decrypt(String, String)方法");
		return result;
	}

	/**
	 * 解密字符串.
	 * 
	 * @param strIn
	 *            需解密的字符串
	 * @return 解密后的字符串
	 */
	public static String decrypt(final String strIn) {
		logger.debug("进入decrypt(String)方法");
		String result = null;
		result = decrypt(strIn, DEFAULT_KEY);
		logger.debug("退出decrypt(String)方法");
		return result;
	}
}
