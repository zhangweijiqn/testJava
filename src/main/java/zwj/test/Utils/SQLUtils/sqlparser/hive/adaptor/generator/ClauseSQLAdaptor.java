package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于如select等子句的SQL生成
 * @author bjliuhongbin
 *
 */
public class ClauseSQLAdaptor extends AbstractDefaultSQLAdaptor {

	@Override
	protected boolean nameAsPrefix() {
		return true;
	}

	@Override
	protected boolean canChildrenEmpty() {
		return true;
	}

	@Override
	protected String childrenSeparator() {
		return ",";
	}

	@Override
	protected boolean childrenWithParenthsis() {
		return false;
	}

	@Override
	public List<Integer> acceptTypes() {
		return ACCEPT_TYPES;
	}
	
	private static List<Integer> ACCEPT_TYPES = new ArrayList<Integer>();
	static {
		ACCEPT_TYPES.add(HiveParser.TOK_SELECTDI);
		ACCEPT_TYPES.add(HiveParser.TOK_SELECT);
		ACCEPT_TYPES.add(HiveParser.TOK_SORTBY);
		ACCEPT_TYPES.add(HiveParser.TOK_CLUSTERBY);
		ACCEPT_TYPES.add(HiveParser.TOK_ORDERBY);
		ACCEPT_TYPES.add(HiveParser.TOK_DISTRIBUTEBY);
		ACCEPT_TYPES.add(HiveParser.TOK_FROM);
		ACCEPT_TYPES.add(HiveParser.TOK_WHERE);
		ACCEPT_TYPES.add(HiveParser.TOK_TABLE_OR_COL);
		ACCEPT_TYPES.add(HiveParser.TOK_HAVING);
		ACCEPT_TYPES.add(HiveParser.TOK_GROUPBY);
        ACCEPT_TYPES.add(HiveParser.TOK_EXPLAIN);
		
		ACCEPT_TYPES.add(HiveParser.TOK_INSERT_INTO);
		ACCEPT_TYPES.add(HiveParser.TOK_LIMIT);

        ACCEPT_TYPES.add(HiveParser.TOK_NULL);

	}

}
