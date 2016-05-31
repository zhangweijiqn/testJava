package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理Limit语句
 * Created by bjliuhongbin on 14-9-25.
 */
public class LimitSQLAdapter extends AbstractSQLAdaptor {
    @Override
    public String generatSQL(HiveSQLContext context, HiveASTNode node) throws SQLException {

        context.setHasLimit(true);

        boolean overwrite = context.isOverWriteLimit();
        int limitNum = context.getLimitNum();
        int oldLimit = Integer.valueOf(node.getChild(0).getText().trim());


        if (overwrite && limitNum > 0 && limitNum < oldLimit) {
            return " limit " + limitNum;
        } else {
            return " limit " + node.getChild(0).getText();
        }
    }

    @Override
    public List<Integer> acceptTypes() {
        return ACCEPT_TYPES;
    }

    /*
     * 虚拟节点
     */
    private static List<Integer> ACCEPT_TYPES = new ArrayList<Integer>();
    static {
        ACCEPT_TYPES.add(HiveParser.TOK_LIMIT);
    }
}
