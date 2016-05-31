package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于PARTITION子句的sql解析
 * @author bjliuhongbin
 *
 */
public class PartitionSQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		int type = node.getType();
		switch (type) {
		case HiveParser.TOK_PARTSPEC:
			return generatSQLForPartspecL(context, node);
		case HiveParser.TOK_PARTVAL:
			return generatSQLForPartval(context, node);
		default:
			return "";
		}
	}
	
	// TOK_PARTSPEC
	private String generatSQLForPartspecL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		String name = getPartitionName();
		
		int childrenCnt = node.getChildCount();
		for (int i = 0; i < childrenCnt; i++) {
			HiveASTNode child = (HiveASTNode) node.getChild(i);
			if (ret.length() > 0) {
				ret.append(",");
			}
			
			String childSQL = child.toSQL(context);
			if (!StringUtils.isEmpty(childSQL)) {
				ret.append(childSQL);
			}
		}
		ret.insert(0, name + "(");
		ret.append(")");
		
		return ret.toString();
	}
	
	// TOK_PARTVAL
	private String generatSQLForPartval(HiveSQLContext context, HiveASTNode node) {
		
		int childrenCnt = node.getChildCount();
		if (childrenCnt == 1) {
			return ((HiveASTNode) node.getChild(0)).toSQL(context);
		} else if (childrenCnt == 2) {
			HiveASTNode left = (HiveASTNode) node.getChild(0);
			HiveASTNode right = (HiveASTNode) node.getChild(1);
			
			return left.toSQL(context) + " = " + right.toSQL(context);
		} 
		
		return "";
	}

	@Override
	public List<Integer> acceptTypes() {
		return ACCEPT_TYPES;
	}
	
	public String getPartitionName() {
		return "PARTITION";
	}
	
	private static List<Integer> ACCEPT_TYPES = new ArrayList<Integer>();
	static {
		ACCEPT_TYPES.add(HiveParser.TOK_PARTSPEC);
		ACCEPT_TYPES.add(HiveParser.TOK_PARTVAL);
	}
}
