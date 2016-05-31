package zwj.test.Utils.SQLUtils.sqlparser.beans;

public class Column {

	/**
	 * 表名或者表别名
	 */
	private String contextName;
	
	/**
	 * 列名
	 */
	private String colName;
	
	/**
	 * 构造函数
	 * @param colName 列名称
	 */
	public Column(String colName) {
		this.colName = colName;
	}

	/**
	 * 表名或者表别名
	 * @return
	 */
	public String getContextName() {
		return contextName;
	}

	/**
	 * 表名或者表别名
	 * @param contextName
	 */
	public void setContextName(String contextName) {
		this.contextName = contextName;
	}

	/**
	 * 列名称
	 * @return
	 */
	public String getColName() {
		return colName;
	}

	/**
	 * 列名称
	 * @param colName 列名称
	 */
	public void setColName(String colName) {
		this.colName = colName;
	}
	
	
	
	
}
