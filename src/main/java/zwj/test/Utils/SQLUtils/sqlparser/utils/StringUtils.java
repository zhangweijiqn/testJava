package zwj.test.Utils.SQLUtils.sqlparser.utils;

import java.util.Calendar;

public class StringUtils {

	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}
	
	/**
	 * 随即生成一个排序号
	 * @return
	 */
	public static long randomOrder() {
		return Calendar.getInstance().getTimeInMillis();
	}
}
