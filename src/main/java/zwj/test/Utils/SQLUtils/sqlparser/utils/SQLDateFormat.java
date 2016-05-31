package zwj.test.Utils.SQLUtils.sqlparser.utils;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.ErrorCode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SQLDateFormat {

	private final static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
	
	/**
	 *  格式化日期（yyyyMMdd）
	 * @param date 日期
	 * @return
	 */
	public static String format(Date date) {
		return format.format(date);
	}
	
	/**
	 * 将yyyyMMdd格式的字符串转换成日期对象
	 * @param sDate yyyyMMdd格式的字符串
	 * @return
	 * @throws SQLException 当日期格式不正确的时候抛出异常
	 */
	public static Date parse(String sDate) throws SQLException {
		try {
			return format.parse(sDate);
		} catch (ParseException e) {
			throw new SQLException("日期格式不符合yyyyMMdd的规范", e, ErrorCode.ERROR_DATE_PATTERN);
		}
	}

    public static String format(Date date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }
}
