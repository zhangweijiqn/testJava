package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于单目运算符的SQL生成
 * @author bjliuhongbin
 *
 */
public class UnaryOperatorSQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		String name = getRealName(false, node);
		ret.append(name);
		ret.append("(").append(((HiveASTNode) node.getChild(0)).toSQL(context)).append(")");
		
		return ret.toString();
	}

	@Override
	public List<Integer> acceptTypes() {
		return UNARY_OPERATOR_TYPES;
	}
	
	/*
	 * 单目操作符
	 */
	private static List<Integer> UNARY_OPERATOR_TYPES = new  ArrayList<Integer>();
	static {
		UNARY_OPERATOR_TYPES.add(HiveParser.TILDE);// ~
		UNARY_OPERATOR_TYPES.add(HiveParser.KW_NOT);
	}

}
