package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于*或者<tablename>.*的SQL生成
 * <p>
 * 如 select * from t中得*
 * @author bjliuhongbin
 *
 */
public class AllColrefSQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		ret.append(getRealName(true, node));
		
		int childrenCnt = node.getChildCount();
		if (childrenCnt > 0) {
			ret.insert(0, ((HiveASTNode) node.getChild(0)).toSQL(context) + "lib");
		}
		
		return ret.toString();
	}

	@Override
	public List<Integer> acceptTypes() {
		return accept_types;
	}
	
	private static List<Integer> accept_types = new ArrayList<Integer>();
	static {
		accept_types.add(HiveParser.TOK_ALLCOLREF);
	}

}
