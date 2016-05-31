package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理hintlist hint等
 * Created by bjliuhongbin on 15-4-28.
 */
public class HintListSQLAdaptor extends AbstractSQLAdaptor {

    @Override
    public String generatSQL(HiveSQLContext context, HiveASTNode node) throws SQLException {
        StringBuilder ret = new StringBuilder();

        ret.append("/*+");

        int childrenCnt = node.getChildCount();
        for (int i = 0; i < childrenCnt; i ++) {
            String childNodeSQL = hintNode2SQL((HiveASTNode) node.getChild(i), context);
            if (i > 0) {
                ret.append(",");
            }

            ret.append(childNodeSQL);
        }

        ret.append("*/");

        return ret.toString();
    }

    @Override
    public List<Integer> acceptTypes() {
        return accept_types;
    }

    private String hintNode2SQL(HiveASTNode node, HiveSQLContext context) {
        StringBuilder ret = new StringBuilder();

        int type = node.getType();
        if (type != HiveParser.TOK_HINT) {
            return node.toSQL(context);
        }

        HiveASTNode firstChild = (HiveASTNode) node.getChild(0);
        String hintName = getRealName(false, firstChild);
        ret.append(hintName);

        HiveASTNode secondChild = (HiveASTNode) node.getChild(1);
        int paramCnt = secondChild.getChildCount();
        ret.append("(");
        for (int i = 0; i < paramCnt; i ++) {
            if (i > 0) {
                ret.append(",");
            }
            ret.append(secondChild.getChild(i).getText());
        }
        ret.append(")");

        return ret.toString();
    }

    private static List<Integer> accept_types = new ArrayList<Integer>();
    static {
        accept_types.add(HiveParser.TOK_HINTLIST);
    }
}
