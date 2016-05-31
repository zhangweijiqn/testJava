package zwj.test.Utils.SQLUtils.sqlparser.beans;

import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class JoinCondition {

	/**
	 * join主表
	 */
	private Table mainTable;
	
	/**
	 * join主表中用于关联的列名
	 */
	private String mainField;
	
	/**
	 * join表
	 */
	private Table joinTable;
	
	/**
	 * join表中用于关联的字段名
	 */
	private String joinField;
	
	/**
	 * join的条件表达式，相互之间都是and关系
	 */
	private List<String> filterExps = new ArrayList<String>();

	/**
	 * 构造函数
	 * @param mainTable join主表
	 * @param mainField join主表中用于关联的列名
	 * @param joinTable join表
	 * @param joinField join表中用于关联的字段名
	 */
	public JoinCondition(Table mainTable, String mainField, Table joinTable,
			String joinField) {
		this.mainTable = mainTable;
		this.mainField = mainField;
		this.joinTable = joinTable;
		this.joinField = joinField;
	}

	/**
	 * join主表
	 * @return
	 */
	public Table getMainTable() {
		return mainTable;
	}

	/**
	 * join主表
	 * @param mainTable join主表
	 */
	public void setMainTable(Table mainTable) {
		this.mainTable = mainTable;
	}

	/**
	 * join主表中用于关联的列名
	 * @return
	 */
	public String getMainField() {
		return mainField;
	}

	/**
	 * join主表中用于关联的列名
	 * @param mainField join主表中用于关联的列名
	 */
	public void setMainField(String mainField) {
		this.mainField = mainField;
	}

	/**
	 * join表
	 * @return
	 */
	public Table getJoinTable() {
		return joinTable;
	}

	/**
	 * join表
	 * @param joinTable join表
	 */
	public void setJoinTable(Table joinTable) {
		this.joinTable = joinTable;
	}

	/**
	 * join表中用于关联的字段名
	 * @return
	 */
	public String getJoinField() {
		return joinField;
	}

	/**
	 * join表中用于关联的字段名
	 * @param joinField join表中用于关联的字段名
	 */
	public void setJoinField(String joinField) {
		this.joinField = joinField;
	}
	
	/**
	 * 过滤条件（过滤条件之间都是and关系）
	 * @return
	 */
	public List<String> getFilterExps() {
		return filterExps;
	}
	
	/**
	 * 添加过滤条件（过滤条件之间都是and关系）
	 */
	public void addFilterExps(String...filterexps) {
		if (filterexps == null) {
			return ;
		}
		
		for (String exp : filterexps) {
			if (!this.filterExps.contains(exp)) {
				this.filterExps.add(exp);
			}
		}
	}
	
	/**
	 * 判断当前自定义条件是否为一个有效的条件
	 * <p>
	 * mainTable/mainField/joinTable/joinField 任意一个为空则无效
	 * @return
	 */
	public boolean valid() {
		if (this.mainTable == null || !this.mainTable.valid()) {
			return false;
		}
		
		if (StringUtils.isEmpty(this.mainField)) {
			return false;
		}
		
		if (this.joinTable == null || !this.joinTable.valid()) {
			return false;
		}
		
		if (StringUtils.isEmpty(this.joinField)) {
			return false;
		}
		
		return true;
	}

	@Override
	public String toString() {
		StringBuffer ret = new StringBuffer();
		ret.append(this.getMainTable().toString()).append("\r\n");
		ret.append(this.getMainField()).append("\r\n");
		ret.append(this.getJoinTable().toString()).append("\r\n");
		ret.append(this.getJoinField()).append("\r\n");
		
		return ret.toString();
	}
}
