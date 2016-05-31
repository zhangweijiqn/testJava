package zwj.test.Utils.SQLUtils.sqlparser.exception;

import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.ErrorCode;

public class SQLException extends RuntimeException {
	private ErrorCode code = ErrorCode.DEFAULT_SQL_ERROR;// 错误码

	private static final long serialVersionUID = 1093315792222517820L;

	@Deprecated
	public SQLException(String error) {
		super(error);
	}
	
	/**
	 * 构造函数
	 * @param error 错误信息
	 * @param code 错误码
	 */
	public SQLException(String error, ErrorCode code) {
		super(error);
		this.code = code;
	}
	
	@Deprecated
	public SQLException(String error, Throwable e) {
		super(error, e);
	}
	
	@Deprecated
	public SQLException(Throwable e) {
		super(e);
	}
	
	/**
	 * 构造函数
	 * @param error 错误信息
	 * @param e 异常对象
	 * @param code 错误码
	 */
	public SQLException(String error, Throwable e, ErrorCode code) {
		super(error, e);
		this.code = code;
	}
	
	/**
	 * 错误码
	 * @return 错误码
	 */
	public ErrorCode getCode() {
		return code;
	}

	/**
	 * 错误码
	 * @param code 错误码
	 */
	public void setCode(ErrorCode code) {
		this.code = code;
	}
}
