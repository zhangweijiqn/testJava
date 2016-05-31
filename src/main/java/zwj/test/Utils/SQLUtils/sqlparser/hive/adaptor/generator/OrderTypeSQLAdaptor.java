package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于orderItem的sql生成
 * <p>
 * 如：order by col1 asc中的col1 asc
 * @author bjliuhongbin
 *
 */
public class OrderTypeSQLAdaptor extends AbstractDefaultSQLAdaptor {

	@Override
	protected boolean nameAsPrefix() {
		return false;
	}

	@Override
	protected boolean canChildrenEmpty() {
		return false;
	}

	@Override
	protected String childrenSeparator() {
		return "";
	}

	@Override
	protected boolean childrenWithParenthsis() {
		return false;
	}

	@Override
	public List<Integer> acceptTypes() {
		return accept_types;
	}
	
	private static List<Integer> accept_types = new ArrayList<Integer>();
	static {
		accept_types.add(HiveParser.TOK_TABSORTCOLNAMEASC);
		accept_types.add(HiveParser.TOK_TABSORTCOLNAMEDESC);
	}

}
