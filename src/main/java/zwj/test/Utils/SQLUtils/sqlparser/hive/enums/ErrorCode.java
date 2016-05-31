package zwj.test.Utils.SQLUtils.sqlparser.hive.enums;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ErrorCode {
	DEFAULT_SQL_ERROR(0x000000, "SQL syntax error"),
	ERROR_DATE_PATTERN(0x000000 + 0x000090, "Date Pattern Error");
	
	private int code;
	private String mean;
	
	private ErrorCode(int code, String mean) {
		this.code = code;
		this.mean = mean;
	}
	
	/**
	 * 错误码
	 * @return
	 */
	public int getCode() {
		return this.code;
	}
	
	/**
	 * 含义
	 * @return
	 */
	public String getMean() {
		return this.mean;
	}
	
	final static Map<Integer, ErrorCode> finder = new ConcurrentHashMap<Integer, ErrorCode>();
	static {
		ErrorCode errorCodes[] = ErrorCode.values();
		for (ErrorCode errorCode : errorCodes) {
			finder.put(errorCode.code, errorCode);
		}
	}
	
	/**
	 * 根据errorcode返回错误枚举
	 * @param code 错误码
	 * @return
	 */
	public static ErrorCode valueOf(int code) {
		return finder.get(code);
	}
}
