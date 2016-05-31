package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.condition;

import zwj.test.Utils.SQLUtils.sqlparser.beans.JoinCondition;
import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;

import java.util.HashMap;
import java.util.Map;

/**
 * 用于提供自定义条件相关操作
 * @author bjliuhongbin
 *
 */
public class ConditionProvider {

	private Map<String, JoinCondition> conditionFinder = new HashMap<String, JoinCondition>();
	
	/**
	 * 构造函数
	 * @param conditions 自定义条件列表
	 */
	public ConditionProvider(JoinCondition[] conditions) {
		prepare(conditions);
	}
	
	private void prepare(JoinCondition[] conditions) {
		if (conditions == null) {
			return ;
		}
		
		for (JoinCondition joinCondition : conditions) {
			if (joinCondition == null || !joinCondition.valid()) {
				continue;
			}
			
			Table mainTable = joinCondition.getMainTable();
			String wholeName = mainTable.getWholeName();
			conditionFinder.put(wholeName, joinCondition);
		}
	}
	
	/**
	 * 获取需要为table注入的条件
	 * @param table 表对象
	 * @return
	 */
	public JoinCondition getCondition(Table table) {
		if (table == null) {
			return null;
		}
		
		String wholeName = table.getWholeName();
		return conditionFinder.get(wholeName);
	}
}
