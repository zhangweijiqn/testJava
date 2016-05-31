package zwj.test.Utils.SQLUtils.sqlparser.beans;


import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.TableScope;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;

public class Table {

	// 表空间
	private String schemaName;
	// 表名称
	private String name;
	// 别名
	private String alias;
	// 表作用域
	private TableScope tableScope = TableScope.QUERY_TABLE;
	
	public Table() {
	}
	
	/**
	 * 构造函数
	 * @param schemeName 表空间
	 * @param name 表名称
	 */
	public Table(String schemeName, String name) {
		this.schemaName = schemeName;
		this.name = name;
	}
	
	/**
	 * 构造函数
	 * @param schemaName 表空间
	 * @param name 表名称 
//	 * @param alias 别名
	 * @param tableScope 表作用域
	 */
	public Table(String schemaName, String name, String alias, TableScope tableScope) {
		this.schemaName = schemaName;
		this.name = name;
		this.alias = alias;
		
		if (tableScope != null) {
			this.tableScope = tableScope;
		}
	}
	
	/**
	 * 表空间
	 * @return
	 */
	public String getSchemaName() {
		return schemaName;
	}
	
	/**
	 * 表空间
	 * @param schemaName 表空间
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}
	
	/**
	 * 表名称
	 * @return
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * 表名称
	 * @param name 表名称
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * 别名
	 * @return 别名
	 */
	public String getAlias() {
		return alias;
	}
	
	/**
	 * 别名
	 * @param alias 别名
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * 表作用域
	 * @return
	 */
	public TableScope getTableScope() {
		return tableScope;
	}

	/**
	 * 表作用域
	 * @param tableScope
	 */
	public void setTableScope(TableScope tableScope) {
		this.tableScope = tableScope;
	}
	
	/**
	 * 是否是一个有效的表描述
	 * @return
	 */
	public boolean valid() {
		if (StringUtils.isEmpty(name)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * schemeName.tablename形式，不包含alias
	 * @return
	 */
	public String getWholeName() {
		String schemeName = this.schemaName;
		if (schemeName == null) {
			return this.name;
		} else {
			
			schemeName = schemeName.trim();
			return schemeName + "lib" + this.name;
		}
		
	}
	
	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append("[").append(this.tableScope).append("]");
		
		if (StringUtils.isEmpty(schemaName)) {
			ret.append("<Empty>");
		} else {
			ret.append(schemaName);
		}
		
		ret.append("lib").append(this.name);
		
		ret.append(" ").append(this.alias == null ? "<Empty>" : this.alias);
		
		return ret.toString();
	}
	
}
