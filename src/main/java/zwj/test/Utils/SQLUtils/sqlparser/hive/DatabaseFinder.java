package zwj.test.Utils.SQLUtils.sqlparser.hive;

import zwj.test.Utils.SQLUtils.DBType;
import zwj.test.Utils.SQLUtils.sqlparser.ISQLParser;
import zwj.test.Utils.SQLUtils.sqlparser.SQLParserFactory;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Json.JSONUtils;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.List;

/**
 * Created by bjliuhongbin on 14-12-24.
 */
public class DatabaseFinder {

    public static void main(String args[]) {
        String sql = "(select * from db1.t1)";

//(TOK_DESCDATABASE(db))
//        String sql = "SHOW TABLE EXTENDED from db1 like '' partition(dt='')";
//        HiveSqlParser parser = HiveSqlParser.parse(sql);
        ISQLParser parser = SQLParserFactory.getSQLParser(DBType.Hive, sql);
        try {
            List<String> dbs = parser.findDatabases();
            System.out.println(JSONUtils.toJSON(dbs));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
//        System.out.println( parser.sqlDump());
    }

    public void find(HiveASTNode node, List<String> dbList) throws SQLException {
        int type = node.getType();
        DBAdapter adapter = getDBAdapter(type);

        adapter.find(node, dbList);
    }

    private DBAdapter getDBAdapter(int type) throws SQLException {
        switch (type) {
            case HiveParser.TOK_SHOWINDEXES :
                return new DBAdapter() {
                    @Override
                    public void find(HiveASTNode node, List<String> dbList) throws SQLException {
                        int childrenCnt = node.getChildCount();
                        if (childrenCnt == 2) {
                            HiveASTNode dbNode = (HiveASTNode) node.getChild(1);
                            if (dbNode.getType() != HiveParser.Identifier) return ;

                            String db = dbNode.getText();
                            if (!dbList.contains(db.toUpperCase())) {
                                dbList.add(db.toUpperCase());
                            }
                        } else if (childrenCnt == 3) {
                            HiveASTNode dbNode = (HiveASTNode) node.getChild(2);
                            if (dbNode.getType() != HiveParser.Identifier) return ;

                            String db = dbNode.getText();
                            if (!dbList.contains(db.toUpperCase())) {
                                dbList.add(db.toUpperCase());
                            }
                        }
                    }
                };

            case HiveParser.TOK_SHOWTABLES :
            case HiveParser.TOK_SHOWCOLUMNS :
                return new DBAdapter() {
                    @Override
                    public void find(HiveASTNode node, List<String> dbList) throws SQLException {
                        int childrenCnt = node.getChildCount();
                        if (childrenCnt >= 2) {
                            HiveASTNode dbNode = (HiveASTNode) node.getChild(1);
                            if (dbNode.getType() != HiveParser.Identifier) return ;

                            String db = dbNode.getText();
                            if (!dbList.contains(db.toUpperCase())) {
                                dbList.add(db.toUpperCase());
                            }
                        }
                    }
                };
            case HiveParser.TOK_DROPDATABASE:
            case HiveParser.TOK_ALTERDATABASE_PROPERTIES:
            case HiveParser.TOK_SWITCHDATABASE:
            case HiveParser.TOK_DESCDATABASE:
                return new DBAdapter() {
                    @Override
                    public void find(HiveASTNode node, List<String> dbList) throws SQLException {
                        HiveASTNode dbNode = (HiveASTNode) node.getChild(0);
                        String db = dbNode.getText();
                        if (!dbList.contains(db.toUpperCase())) {
                            dbList.add(db.toUpperCase());
                        }
                    }
                };
            default:
                return defaultAdapter;
        }
    }

    final DBAdapter defaultAdapter = new DBAdapter();

    class DBAdapter {
        public void find(HiveASTNode node, List<String> dbList) throws SQLException {
            if (node.getType() == HiveParser.TOK_TABNAME) {
                int childrenCnt = node.getChildCount();
                if (childrenCnt == 2) {
                    HiveASTNode dbNode = (HiveASTNode) node.getChild(0);
                    String db = dbNode.getText();
                    if (!dbList.contains(db.toUpperCase())) {
                        dbList.add(db.toUpperCase());
                    }
                }
            } else {
                List<Node> children = node.getChildren();
                if (children == null || children.size() <= 0) return ;

                for (Node child : children) {
                    find((HiveASTNode)child, dbList);
                }
            }
        }
    }
}
