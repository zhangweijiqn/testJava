package zwj.test.Utils.SQLUtils.sqlparser.param;


import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;

/**
 * sql语句中得参数
 * @author bjliuhongbin
 */
public abstract class AbstractParam {

	private String name ;

	
	public AbstractParam(String name) {
		this.name = name;
	}
	
	/**
	 * 参数名称
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 变量取值
	 * @return
	 * @throws SQLException
	 */
	public abstract Object getValue(String params[]) throws SQLException;
	
	/**
	 * 转换成sql表达式
	 * @return
	 * @throws SQLException
	 */
	public abstract String parse2SQL(String params[]) throws SQLException;

    public abstract void check(String params[]) throws SQLException;
}
