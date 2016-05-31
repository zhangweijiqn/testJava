package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Lateral view
 * Created by bjliuhongbin on 15-5-19.
 */
public class LateralViewSQLAdaptor extends AbstractSQLAdaptor {
    @Override
    public String generatSQL(HiveSQLContext context, HiveASTNode node) throws SQLException {
        StringBuilder ret = new StringBuilder();

        List<Node> children = node.getChildren();
        for (int i = children.size() - 1; i >= 1; i --) {
            HiveASTNode child = (HiveASTNode) children.get(i);
            ret.append(child.toSQL(context)).append(" ");
        }

        String nodeName = getRealName(false, node);
        ret.append(nodeName).append(" ");
        HiveASTNode selExprNode = (HiveASTNode) node.getChild(0).getChild(0);
        List<Node> selExprChildren = selExprNode.getChildren();
        int selExprChildrenCnt = selExprChildren.size();
        HiveASTNode functionNode = (HiveASTNode) selExprChildren.get(0);
        ret.append(functionNode.toSQL(context)).append(" ");
        HiveASTNode tabAliasNode = (HiveASTNode) selExprChildren.get(selExprChildrenCnt - 1);
        ret.append(tabAliasNode.toSQL(context)).append(" ");
        if (selExprChildrenCnt > 2) {
            ret.append("as ");
            for (int i = 1; i < selExprChildrenCnt - 1; i ++) {
                HiveASTNode child = (HiveASTNode) selExprChildren.get(i);
                if (i > 1) {
                    ret.append(",");
                }
                ret.append(child.toSQL(context));
            }
        }

        return ret.toString();
    }

    @Override
    public List<Integer> acceptTypes() {
        return accept_types;
    }

    private static List<Integer> accept_types = new ArrayList<Integer>();
    static {
        accept_types.add(HiveParser.TOK_LATERAL_VIEW);
        accept_types.add(HiveParser.TOK_LATERAL_VIEW_OUTER);
    }
}
