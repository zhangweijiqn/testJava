package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于.的sql生成
 * @author bjliuhongbin
 *
 */
public class DotSQlAdaptor extends BinaryOperatorSQLAdaptor {

	@Override
	protected boolean withSpace() {
		return false;
	}

	@Override
	public List<Integer> acceptTypes() {
		return accept_types;
	}
	
	private static List<Integer> accept_types = new ArrayList<Integer>();
	static {
		accept_types.add(HiveParser.DOT);
	}

}
