package cn.hudp.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本验证辅助类
 * 
 * @author HuDP
 * @email mox113@foxmail.com
 * @date 2015年7月3日
 */
public class TextUtils {
	/** 判断手机格式是否正确 */
	public static boolean isMobileNO(String mobiles) {
		if (isNull(mobiles))
			return false;
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	/** 判断email格式是否正确 */
	public static boolean isEmail(String email) {
		if (isNull(email))
			return false;
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/** 判断是否全是数字 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/** 判断文本是是否相等 (若都为空 返回false) */
	public static boolean isTextEqual(String strOne, String strTwo) {
		if (strOne != null && strTwo != null) {
			if (strOne.equals(strTwo)) {
				return true;
			}
		}
		return false;
	}

	/** 判断文本是否为空 */
	public static boolean isNull(String str) {
		if (str == null || isTextEqual(str.trim(), null) || isTextEqual(str.trim(), "")) {
			return true;
		}

		return false;
	}

}
