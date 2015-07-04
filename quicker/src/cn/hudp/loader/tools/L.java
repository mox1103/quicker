package cn.hudp.loader.tools;

import android.util.Log;

/**
 * Log统一管理类
 * 
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015年6月30日
 */
public class L {
	public static boolean isDebug = true;
	private static final String TAG = "quicker";

	private L() {
		/* 不允许实例化 */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	public static void setIsDebug(boolean mIsDebug) {
		isDebug = mIsDebug;
	}

	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	// 下面是传入自定义tag的函数
	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}
}
