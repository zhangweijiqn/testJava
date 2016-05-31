package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.condition;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;

/**
 * 用于向SQL中注入自定义条件
 * @author bjliuhongbin
 *
 */
public abstract class AbstractConditionAdaptor {

	/**
	 * 向node中注入自定义条件
	 * @param context 上下文
	 * @param node 被注入的节点
	 * @param provider 自定义条件提供器
	 */
	public abstract void fillConditions(HiveSQLContext context, HiveASTNode node, ConditionProvider provider) throws SQLException;
	
	/**
	 * 适用的节点类型
	 * @return
	 */
	public abstract Integer[] acceptTypes();
}
