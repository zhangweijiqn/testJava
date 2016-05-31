package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor;


import zwj.test.Utils.SQLUtils.sqlparser.beans.JoinCondition;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.condition.AbstractConditionAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.condition.ConditionProvider;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.condition.QueryConditionAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义条件注入器
 * @author bjliuhongbin
 *
 */
public class ConditionFiller {
	private List<JoinCondition> conditions = new ArrayList<JoinCondition>();

	/**
	 * 构造函数
	 */
	public ConditionFiller() {
		super();
	}
	
	/**
	 * 向node中注入自定义条件
	 * @param context 上下文(上下文中可以找到ConditionProvider，用于提供对自定义条件的检索)
	 * @param node 被注入的节点
	 * 
	 */
	public void fillConditions(HiveSQLContext context, HiveASTNode node) throws SQLException {
		AbstractConditionAdaptor adaptor = findAdaptor(node.getType());
		if (adaptor == null) {
			return ;
		}
		
		ConditionProvider provider = new ConditionProvider(conditions.toArray(new JoinCondition[conditions.size()]));
		adaptor.fillConditions(context, node, provider);
	}
	
	/**
	 * 获取当前自定义条件容器
	 * @return
	 */
	public List<JoinCondition> getConditions() {
		return conditions;
	}
	
	/**
	 * 获取节点类型适配的条件适配器
	 * @param type 节点类型
	 * @return
	 */
	private AbstractConditionAdaptor findAdaptor(int type) {
		return adaptorFinder.get(type);
	}
	
	/**
	 * 获取所有有效的适配器
	 * @return
	 */
	private static AbstractConditionAdaptor[] getAllAdaptors() {
		return new AbstractConditionAdaptor[] {
				new QueryConditionAdaptor()
		};
	}
	
	private static Map<Integer, AbstractConditionAdaptor> adaptorFinder = new HashMap<Integer, AbstractConditionAdaptor>();
	static {
		AbstractConditionAdaptor[] adaptors = getAllAdaptors();
		if (adaptors != null) {
			for (AbstractConditionAdaptor adaptor : adaptors) {
				Integer types[] = adaptor.acceptTypes();
				for (Integer type : types) {
					adaptorFinder.put(type, adaptor);
				}
			}
		}
	}
	
}
