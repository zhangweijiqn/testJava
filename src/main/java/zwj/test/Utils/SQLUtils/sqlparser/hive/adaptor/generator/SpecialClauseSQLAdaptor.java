package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于如TOK_TMP_FILE等子句的处理
 * @author bjliuhongbin
 *
 */
public class SpecialClauseSQLAdaptor extends AbstractDefaultSQLAdaptor {

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
		return " ";
	}

	@Override
	protected boolean childrenWithParenthsis() {
		return false;
	}

	@Override
	public List<Integer> acceptTypes() {
		return ACCEPT_TYPES;
	}
	
	/* 
	 * 虚拟节点 
	 */
	private static List<Integer> ACCEPT_TYPES = new  ArrayList<Integer>();
	static {
		ACCEPT_TYPES.add(HiveParser.TOK_QUERY);
		ACCEPT_TYPES.add(HiveParser.TOK_INSERT);
		ACCEPT_TYPES.add(HiveParser.TOK_SELEXPR);
		
		ACCEPT_TYPES.add(HiveParser.TOK_ISNULL);
		ACCEPT_TYPES.add(HiveParser.TOK_ISNOTNULL);
		ACCEPT_TYPES.add(HiveParser.KW_IN);

		ACCEPT_TYPES.add(HiveParser.TOK_TMP_FILE);
		ACCEPT_TYPES.add(HiveParser.TOK_TABREF);

        ACCEPT_TYPES.add(HiveParser.TOK_TABTYPE);

        ACCEPT_TYPES.add(HiveParser.TOK_WINDOWSPEC);
        ACCEPT_TYPES.add(HiveParser.TOK_PARTITIONINGSPEC);
	}

}
