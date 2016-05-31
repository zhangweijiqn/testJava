package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于双目运算符的SQL生成
 * @author bjliuhongbin
 *
 */
public class BinaryOperatorSQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		StringBuffer ret = new StringBuffer();

		if (node.getChildCount() != 2) {
			return "";
		}
		
		boolean withSpace = withSpace();
		
		HiveASTNode leftNode = (HiveASTNode) node.getChild(0);
		boolean leftNeedParenthesis = comparePriority(node, leftNode) > 0 ;
		if (leftNeedParenthesis) {
			ret.append("(");
			ret.append(leftNode.toSQL(context));
			ret.append(")");
		} else {
			ret.append(leftNode.toSQL(context));
		}
		
		ret.append(withSpace ? " " : "");
		ret.append(getRealName(false, node));
		ret.append(withSpace ? " " : "");
		
		HiveASTNode rightNode = (HiveASTNode) node.getChild(1);
		boolean rightNeedParenthesis = comparePriority(node, rightNode) > 0 ;
		if (rightNeedParenthesis) {
			ret.append("(");
			ret.append(rightNode.toSQL(context));
			ret.append(")");
		} else {
			ret.append(rightNode.toSQL(context));
		}
		
		return ret.toString();
	}
	
	@Override
	public List<Integer> acceptTypes() {
		return BINARYOPERATOR_TYPES;
	}
	
	/**
	 * 是否在操作符两旁追加空格
	 * @return
	 */
	protected boolean withSpace() {
		return true;
	}
	
	/* 
	 * 二目操作符
	 */
	private static List<Integer> BINARYOPERATOR_TYPES = new  ArrayList<Integer>();
	static {
		// 关系符
		BINARYOPERATOR_TYPES.add(HiveParser.EQUAL);// =
		BINARYOPERATOR_TYPES.add(HiveParser.EQUAL_NS);// <=>
		BINARYOPERATOR_TYPES.add(HiveParser.GREATERTHAN);// >
		BINARYOPERATOR_TYPES.add(HiveParser.GREATERTHANOREQUALTO);// >=
		BINARYOPERATOR_TYPES.add(HiveParser.NOTEQUAL);// <>
		BINARYOPERATOR_TYPES.add(HiveParser.LESSTHAN);// <
		BINARYOPERATOR_TYPES.add(HiveParser.LESSTHANOREQUALTO);// <=
		BINARYOPERATOR_TYPES.add(HiveParser.KW_LIKE);// LIKE
		BINARYOPERATOR_TYPES.add(HiveParser.KW_RLIKE);// RLIKE
		BINARYOPERATOR_TYPES.add(HiveParser.KW_REGEXP);// REGEXP
		
		// 运算符
		BINARYOPERATOR_TYPES.add(HiveParser.PLUS);// +
		BINARYOPERATOR_TYPES.add(HiveParser.MINUS);// -
		BINARYOPERATOR_TYPES.add(HiveParser.STAR);// *
		BINARYOPERATOR_TYPES.add(HiveParser.DIVIDE);// /
		BINARYOPERATOR_TYPES.add(HiveParser.MOD);// %
		BINARYOPERATOR_TYPES.add(HiveParser.AMPERSAND);// &
		BINARYOPERATOR_TYPES.add(HiveParser.BITWISEOR);// |
		BINARYOPERATOR_TYPES.add(HiveParser.BITWISEXOR);// ^
		
		BINARYOPERATOR_TYPES.add(HiveParser.KW_AND);// AND
		BINARYOPERATOR_TYPES.add(HiveParser.KW_OR);// OR
		
		
		BINARYOPERATOR_TYPES.add(HiveParser.TOK_UNION);
//		BINARYOPERATOR_TYPES.add(HiveParser.DOT);// .
	}
}
