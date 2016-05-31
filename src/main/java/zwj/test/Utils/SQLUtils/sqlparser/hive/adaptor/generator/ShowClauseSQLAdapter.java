package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Show 系列控件
 * Created by bjliuhongbin on 14-9-22.
 */
public class ShowClauseSQLAdapter extends ClauseSQLAdaptor {

    @Override
    public String generatSQL(HiveSQLContext context, HiveASTNode _node) throws SQLException {
        int type = _node.getType();
        if (type == HiveParser.TOK_SHOWCOLUMNS) {
            StringBuilder buf = new StringBuilder();
            String name = getRealName(false, _node);
            buf.append(name);

            int childrenCnt = _node.getChildCount();
            if (childrenCnt == 2) {
                buf.append(" from ").append(((HiveASTNode) _node.getChild(1)).toSQL(context));
                buf.append(" from ").append(((HiveASTNode) _node.getChild(0)).toSQL(context));
            } else if (childrenCnt == 1){
                buf.append(" from ").append(((HiveASTNode) _node.getChild(0)).toSQL(context));
            }

            return buf.toString();
        } else if (type == HiveParser.TOK_SHOWINDEXES) {
            StringBuilder buf = new StringBuilder();

            int childrenCnt = _node.getChildCount();
            if (childrenCnt == 3) {
                buf.append("SHOW FORMATTED INDEXES");
            } else {
                buf.append("SHOW INDEXES");
            }

            buf.append(" ON ").append(((HiveASTNode) _node.getChild(0)).toSQL(context));

            if (childrenCnt >= 2) {
                buf.append(" FROM ").append(((HiveASTNode) _node.getChild(childrenCnt - 1)).toSQL(context));
            }

            return buf.toString();
        } else {
            return super.generatSQL(context, _node);
        }
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
        return accept_types;
    }

    private static List<Integer> accept_types = new ArrayList<Integer>();
    static {
        accept_types.add(HiveParser.TOK_SHOWDATABASES);// SHOW DATABASES;
        accept_types.add(HiveParser.TOK_SHOWTABLES);// SHOW TABLES
        accept_types.add(HiveParser.TOK_SHOWCOLUMNS);// SHOW COLUMNS (FROM|IN) table_name [(FROM|IN) db_name]
        accept_types.add(HiveParser.TOK_SHOWPARTITIONS);//show partitions tableName [PARTITION(ds='2010-03-03')]
        accept_types.add(HiveParser.TOK_SHOW_TBLPROPERTIES);// show tblproperties tableName
        accept_types.add(HiveParser.TOK_SHOWFUNCTIONS);// SHOW FUNCTIONS "a.*"
        accept_types.add(HiveParser.TOK_SHOWINDEXES);// SHOW [FORMATTED] (INDEX|INDEXES) ON table_with_index [(FROM|IN) db_name]
        accept_types.add(HiveParser.TOK_SHOWLOCKS);// SHOW LOCKS <TABLE_NAME> PARTITION (<PARTITION_DESC>) EXTENDED;
        accept_types.add(HiveParser.TOK_SHOW_CREATETABLE);// SHOW create table table_name
//        accept_types.add(HiveParser.TOK_SHOW_GRANT);
        accept_types.add(HiveParser.TOK_SHOW_ROLE_GRANT);// SHOW ROLE GRANT User principal_name
//        accept_types.add(HiveParser.TOK_SHOW_TABLESTATUS);
    }
}
