package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于处理没有子句就不显示的场景
 * @author bjliuhongbin
 *
 */
public class ClauseNeedChildrenSQLAdaptor extends AbstractDefaultSQLAdaptor {

	@Override
	protected boolean nameAsPrefix() {
		return true;
	}

	@Override
	protected boolean canChildrenEmpty() {
		return false;
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
		return CLAUSE_NEEDCHILDREN_TYPES;
	}
	
	/* 
	 * 虚拟节点 
	 */
	private static List<Integer> CLAUSE_NEEDCHILDREN_TYPES = new  ArrayList<Integer>();
	static {
		CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_DIR);
		CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_DESTINATION);
		CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_TAB);
        CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_TABALIAS);
        CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_USER);
        CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_ROLE);
        CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_CREATETABLE);
		//CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_ISNULL);
	}

}
