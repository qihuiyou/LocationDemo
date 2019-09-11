package com.qhy.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.Set;

/**
 * Created by qhy on 2019/9/9.
 */
public class SharedPreUtils {

	// 文件名
	public final static String SP_NAME = "shanlin";
	private static SharedPreferences sp;

	/**
	 * 保存一个boolean值
	 * 
	 * @param context
	 * @param key
	 * @param value
	 *            默认为false
	 */
	public static void saveBoolean(Context context, String key, boolean value) {

		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		sp.edit().putBoolean(key, value).apply();
	}

	/**
	 * 保存一个整数
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveInt(Context context, String key, int value) {

		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		sp.edit().putInt(key, value).apply();
	}

	/**
	 * 保存一个字符串
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void saveString(Context context, String key, String value) {

		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		sp.edit().putString(key, value).apply();
	}



	/**
	 * 通过指定的键获取保存的 整数信息
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		return sp.getInt(key, defValue);

	}

	/**
	 * 通过指定的键获取保存的 字符串信息
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		return sp.getString(key, defValue);

	}

	/**
	 * 通过指定的键获取保存的 boolean信息
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
									 boolean defValue) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		return sp.getBoolean(key, defValue);
	}


	public static void saveSetArray(Context context, String key, Set value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		sp.edit().putStringSet(key, value).apply();

	}

	public static Set getSetArray(Context context, String key, Set value) {
		if (sp == null)
			sp = context.getSharedPreferences(SP_NAME, 0);

		return sp.getStringSet(key,value);
	}



	/**
	 * 清除掉所有已保存的数据信息
	 */
	public static void cleanAllKey() {
		sp.edit().clear().apply();
	}

	/**
	 * 清除掉已保存的指定键的数据
	 * 
	 * @param key
	 */
	public static void cleanAppointKey(String key) {

		if(!TextUtils.isEmpty(key)){
			sp.edit().remove(key).apply();
		}
	}


	public static String[] getStringArray(Context context, String key) {
		String regularEx = "#";
		String[] str = null;
		SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		String values;
		values = sp.getString(key, "");
		assert values != null;
		str = values.split(regularEx);

		return str;
	}

	/**
	 * 保存字符串数组
	 * @param context
	 * @param key
	 * @param values
     */
	public static void saveStringArray(Context context, String key, String[] values) {
		String regularEx = "#";
		String str = "";
		SharedPreferences sp = context.getSharedPreferences("data", Context.MODE_PRIVATE);
		if (values != null && values.length > 0) {
			for (String value : values) {
				str += value;
				str += regularEx;
			}
			SharedPreferences.Editor et = sp.edit();
			et.putString(key, str);
			et.apply();
		}
	}

}
