package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于join系列语句的SQL生成
 * @author bjliuhongbin
 *
 */
public class JoinSQLAdaptor extends AbstractSQLAdaptor {

	@SuppressWarnings("deprecation")
	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		int cnt = node.getChildCount();
        if (cnt >= 2) {
            String joinTag = getRealName(false, node);
            HiveASTNode joinNode = (HiveASTNode) node.getChild(0);
            ret.append(joinNode.toSQL(context));

            HiveASTNode onTableNode = (HiveASTNode) node.getChild(1);
            ret.append(" ").append(joinTag).append(" ").append(onTableNode.toSQL(context));

            if (cnt == 3) {
                HiveASTNode onCondition = (HiveASTNode) node.getChild(2);
                ret.append(" ON (").append(onCondition.toSQL(context)).append(")");
            }

        } else {
            throw new SQLException("join statement syntax error");
        }

		return ret.toString();
	}

	@Override
	public List<Integer> acceptTypes() {
		return accept_types;
	}
	
	private static List<Integer> accept_types = new ArrayList<Integer>();
	static {
		accept_types.add(HiveParser.TOK_JOIN);
		accept_types.add(HiveParser.TOK_LEFTOUTERJOIN);
		accept_types.add(HiveParser.TOK_RIGHTOUTERJOIN);
		accept_types.add(HiveParser.TOK_FULLOUTERJOIN);
		accept_types.add(HiveParser.TOK_LEFTSEMIJOIN);
		accept_types.add(HiveParser.TOK_CROSSJOIN);
	}

}
