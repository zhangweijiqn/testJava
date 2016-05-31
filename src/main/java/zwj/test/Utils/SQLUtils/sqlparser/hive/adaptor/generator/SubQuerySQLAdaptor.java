package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于子查询语句的SQL生成
 * @author bjliuhongbin
 *
 */
public class SubQuerySQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		ret.append(" (").append(((HiveASTNode) node.getChild(0)).toSQL(context)).append(") ");
		
		if (node.getChildCount() > 1) {
			ret.append(((HiveASTNode) node.getChild(1)).toSQL(context));
		}
		
		return ret.toString();
	}

	@Override
	public List<Integer> acceptTypes() {
		return accept_types;
	}
	
	private static List<Integer> accept_types = new ArrayList<Integer>();
	static {
		accept_types.add(HiveParser.TOK_SUBQUERY);
	}

}
