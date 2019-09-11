package com.qhy.demo.utils;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DES {
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };


	public static String encryptDES(String encryptString, String encryptKey) throws Exception {
		// IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		SecretKeySpec key = new SecretKeySpec(encryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
		byte[] encryptedData = cipher.doFinal(encryptString.getBytes());

		return Base64.encode(encryptedData);
	}

	public static String decryptDES(String decryptString, String decryptKey) throws Exception {
		byte[] byteMi = Base64.decode(decryptString);
		IvParameterSpec zeroIv = new IvParameterSpec(iv);
		// IvParameterSpec zeroIv = new IvParameterSpec(new byte[8]);
		SecretKeySpec key = new SecretKeySpec(decryptKey.getBytes(), "DES");
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
		byte decryptedData[] = cipher.doFinal(byteMi);

		return new String(decryptedData);
	}

	private static final String nkey = "f5412eb1";

	public static String qucDesEncryptStr(String source) {
		// return source;
		String encryptedStr = "";
		try {
			DESKeySpec dks = new DESKeySpec(nkey.getBytes());

			// 创建一个密匙工厂，然后用它把DESKeySpec转换成
			// 一个SecretKey对象
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
			SecretKey secretKey = keyFactory.generateSecret(dks);

			// using DES in CBC mode
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

			// 初始化Cipher对象
			IvParameterSpec iv = new IvParameterSpec(nkey.getBytes());
			cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

			// 执行加密操作
			byte encryptedData[] = cipher.doFinal(source.getBytes());

			// 通过Base64将二进制数据变成文本
			encryptedStr = Base64.encode(encryptedData);

		} catch (Exception e) {
		}

		return encryptedStr;
	}
}
