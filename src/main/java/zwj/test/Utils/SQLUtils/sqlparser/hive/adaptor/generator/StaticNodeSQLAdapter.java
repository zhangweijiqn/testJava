package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * token即内容
 * Created by bjliuhongbin on 14-9-22.
 */
public class StaticNodeSQLAdapter extends AbstractSQLAdaptor {

    @Override
    public String generatSQL(HiveSQLContext context, HiveASTNode node) throws SQLException {
        StringBuffer ret = new StringBuffer();

        String text = getRealName(false, node);
        ret.append(text);

        int childrenCnt = node.getChildCount();
        for (int i = 0; i < childrenCnt; i ++) {
            ret.append(" ").append(((HiveASTNode) node.getChild(i)).toSQL(context));
        }

        return ret.toString();
    }

    @Override
    public List<Integer> acceptTypes() {
        return ACCEPT_TYPES;
    }

    private static List<Integer> ACCEPT_TYPES = new ArrayList<Integer>();
    static {
        ACCEPT_TYPES.add(HiveParser.KW_EXTENDED);

        ACCEPT_TYPES.add(HiveParser.TOK_DESCTABLE);
        ACCEPT_TYPES.add(HiveParser.TOK_DESCDATABASE);

        ACCEPT_TYPES.add(HiveParser.TOK_LIKETABLE);
    }

}
