package cn.hudp.tools;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast管理类 (默认相同Toast只Toast一次
 * 
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015年6月30日
 */
public class T {
	private static CharSequence oldMsg;
	protected static Toast toast = null;
	private static long oneTime = 0;
	private static long twoTime = 0;

	private T() {
		/* 不允许实例化 */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	/**
	 * Toast核心方法 避免相同Toast内容重复出现
	 * 
	 * @param str
	 * @param isShort
	 */
	public static void showToast(Context mContext, CharSequence message, boolean isShort) {
		if (null == message || null == mContext)
			return;
		if (toast == null) {
			toast = Toast.makeText(mContext, message, isShort ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
			toast.show();
			oneTime = System.currentTimeMillis();
		} else {
			twoTime = System.currentTimeMillis();
			if (message.equals(oldMsg)) {
				if (twoTime - oneTime > Toast.LENGTH_SHORT) {
					toast.show();
				}
			} else {
				oldMsg = message;
				toast.setText(message);
				toast.show();
			}
		}
		oneTime = twoTime;
	}

	public static void showToast(Context mContext, int resId, boolean isShort) {
		showToast(mContext, mContext.getResources().getString(resId), isShort);
	}

	public static void showShort(Context mContext, CharSequence message) {
		showToast(mContext, message, true);
	}

	public static void showShort(Context mContext, int resId) {
		showToast(mContext, resId, true);
	}

	public static void showLong(Context mContext, CharSequence message) {
		showToast(mContext, message, false);
	}

	public static void showLong(Context mContext, int resId) {
		showToast(mContext, resId, false);
	}
}
