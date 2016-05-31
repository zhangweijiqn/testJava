package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理数据类型节点的SQL转换
 * @author bjliuhongbin
 *
 */
public class DataNodeSQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		if (node != null) {
			return node.toString();
		}
		return null;
	}
	
	@Override
	public List<Integer> acceptTypes() {
		return ACCEPT_TYPES;
	}
	
	/* 
	 * 本数据节点接受的节点类型
	 */
	private static List<Integer> ACCEPT_TYPES = new ArrayList<Integer>();
	static {
		ACCEPT_TYPES.add(HiveParser.BigintLiteral);
		ACCEPT_TYPES.add(HiveParser.ByteLengthLiteral);
		ACCEPT_TYPES.add(HiveParser.CharSetLiteral);
		ACCEPT_TYPES.add(HiveParser.DecimalLiteral);
		ACCEPT_TYPES.add(HiveParser.Number);
		ACCEPT_TYPES.add(HiveParser.SmallintLiteral);
		ACCEPT_TYPES.add(HiveParser.StringLiteral);
		ACCEPT_TYPES.add(HiveParser.TinyintLiteral);
		ACCEPT_TYPES.add(HiveParser.KW_TRUE);
		ACCEPT_TYPES.add(HiveParser.KW_FALSE);
		
		ACCEPT_TYPES.add(HiveParser.Identifier);
		
	}

}
