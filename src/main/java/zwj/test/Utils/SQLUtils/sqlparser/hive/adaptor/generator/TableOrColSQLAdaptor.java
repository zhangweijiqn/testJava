package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于表名或列名的SQL生成
 * @author bjliuhongbin
 *
 */
public class TableOrColSQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		int cnt = node.getChildCount();
		if (cnt == 2) {
			return ((HiveASTNode) node.getChild(0)).toSQL(context)
					+ "lib"
					+ ((HiveASTNode) node.getChild(1)).toSQL(context);
		} else {
			return ((HiveASTNode) node.getChild(0)).toSQL(context);
		}
	}

	@Override
	public List<Integer> acceptTypes() {
		return accept_types;
	}
	
	private static List<Integer> accept_types = new ArrayList<Integer>();
	static {
		accept_types.add(HiveParser.TOK_TABNAME);
	}

}
