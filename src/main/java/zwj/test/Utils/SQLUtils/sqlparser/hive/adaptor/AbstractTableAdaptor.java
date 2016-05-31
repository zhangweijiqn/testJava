package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor;

import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;

import java.util.List;

/**
 * 表名查找器
 * @author bjliuhongbin
 */
public abstract class AbstractTableAdaptor {

	/**:
	 * 查找表名
	 * @param context 上下文
	 * @return
	 * @throws SQLException
	 */
	public abstract void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException;
	
}
