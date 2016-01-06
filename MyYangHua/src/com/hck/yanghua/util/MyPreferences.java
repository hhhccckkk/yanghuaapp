package com.hck.yanghua.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class MyPreferences {
	private static SharedPreferences preference = null;
	private static Editor editor;

	public MyPreferences(Context context) {
		preference = context.getSharedPreferences("mypreference",
				Activity.MODE_PRIVATE);
	}

	public static void saveBoolean(String key, boolean boolen) {
		if (key == null || "".equals(key)) {
			Log.e("hck", "MyPreferences saveBoolen null");
			return;
		}
		if (preference != null) {
			editor = preference.edit();
			editor.putBoolean(key, boolen);
			editor.commit();
		}

	}

	public static void saveString(String key, String data) {
		if (key == null || "".equals(key) || data == null || "".equals(data)) {
			Log.e("hck", "MyPreferences saveString null");
			return;
		}
		if (preference != null) {
			editor = preference.edit();
			editor.putString(key, data);
			editor.commit();
		}
	}

	public static void saveInt(String key, int data) {
		if (key == null || "".equals(key)) {
			Log.e("hck", "MyPreferences saveInt null");
			return;
		}
		if (preference != null) {
			editor = preference.edit();
			editor.putInt(key, data);
			editor.commit();
		}

	}

	public static void saveFloat(String key, Float data) {
		if (key == null || "".equals(key)) {
			Log.e("hck", "MyPreferences saveInt null");
			return;
		}
		if (preference != null) {
			editor = preference.edit();
			editor.putFloat(key, data);
			editor.commit();
		}

	}

	public static void saveLong(String key, Long data) {
		if (key == null || "".equals(key)) {
			Log.e("hck", "MyPreferences saveInt null");
			return;
		}
		if (preference != null) {
			editor = preference.edit();
			editor.putLong(key, data);
			editor.commit();
		}

	}

	public static boolean getBoolean(String key, boolean defValue) {
		if (preference != null) {
			return preference.getBoolean(key, defValue);
		}
		return false;
	}

	public static String getString(String key, String defValue) {
		if (preference != null) {
			return preference.getString(key, defValue);
		}
		return defValue;
	}

	public static int getInt(String key, int defValue) {
		if (preference != null) {
			return preference.getInt(key, defValue);
		}
		return 0;
	}

	public static Float getFloat(String key, Float defValue) {
		if (preference != null) {
			return preference.getFloat(key, defValue);
		}
		return defValue;
	}

	public static Long getLong(String key, Long defValue) {
		if (preference != null) {
			return preference.getLong(key, defValue);
		}
		return defValue;
	}

}
