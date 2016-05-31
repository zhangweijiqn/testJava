package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于function（*）系列语句的sql生成
 * @author bjliuhongbin
 *
 */
public class FunctionStarSQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		String functionName = ((HiveASTNode) node.getChild(0)).toSQL(context);
		return functionName + "(*)";
	}

	@Override
	public List<Integer> acceptTypes() {
		return accept_types;
	}
	
	private static List<Integer> accept_types = new ArrayList<Integer>();
	static {
		accept_types.add(HiveParser.TOK_FUNCTIONSTAR);
	}

}
