package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要为了纠正From在前的问题
 * Created by bjliuhongbin on 15-5-15.
 */
public class QuerySQLAdaptor extends AbstractSQLAdaptor {
    @Override
    public String generatSQL(HiveSQLContext context, HiveASTNode node) throws SQLException {
        StringBuilder ret = new StringBuilder();

//        int childrenCnt = node.getChildCount();
//        for (int i = 1; i < childrenCnt; i ++) {
//            ret.append(((HiveASTNode) node.getChild(i)).toSQL(context));
//        }
//        ret.append(((HiveASTNode) node.getChild(0)).toSQL(context));
        int childrenCnt = node.getChildCount();
        if (childrenCnt == 2) {
            HiveASTNode fromNode = (HiveASTNode) node.getChild(0);
            HiveASTNode insertNode= (HiveASTNode) node.getChild(1);
            int insertChildrenCnt = insertNode.getChildCount();
            for (int i = 0; i < insertChildrenCnt; i++) {
                ret.append(((HiveASTNode) insertNode.getChild(i)).toSQL(context)).append(" ");
                if (i == 1) {
                    ret.append(((HiveASTNode) fromNode).toSQL(context)).append(" ");
                }
            }
//            ret.append(((HiveASTNode) insertNode.getChild(0)).toSQL(context));

        } else {
            for (int i = 0; i < childrenCnt; i++) {
                ret.append(((HiveASTNode) node.getChild(i)).toSQL(context));
            }
        }

        return ret.toString();
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
        ACCEPT_TYPES.add(HiveParser.TOK_QUERY);
    }
}
