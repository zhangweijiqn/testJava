package zwj.test.Utils.SQLUtils.sqlparser;

import zwj.test.Utils.SQLUtils.DBType;
import zwj.test.Utils.SQLUtils.sqlparser.base.BaseSQLParser;
import zwj.test.Utils.SQLUtils.sqlparser.hive.HiveSqlParser;

/**
 * Factory of sql parser
 * Created by bjliuhongbin on 15-5-14.
 */
public class SQLParserFactory {
    public static ISQLParser getSQLParser(DBType type, String sql) {
        switch (type) {
            case Hive:
                return HiveSqlParser.parse(sql);
            case MYSQL:
            default:
                return BaseSQLParser.parse(sql);
        }
    }

    public static void main(String[] args) {
        String sql = "SELECT * FROM exampleTable\n" +
                "LATERAL VIEW explode(col1) myTable1 AS myCol1,mycol1_1\n" +
                "LATERAL VIEW explode(col2) myTable2 AS myCol2\n" +
                "LATERAL VIEW explode(myCol3) myTable3 AS myCol3" ;
        ISQLParser parser = SQLParserFactory.getSQLParser(DBType.Hive, sql);
        System.out.println(parser.generateSQL(false));
//        System.out.println(parser.sqlDump());
    }
}
