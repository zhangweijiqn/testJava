package zwj.test.Utils.SQLUtils;

import zwj.test.Json.JSONUtils;
import zwj.test.Utils.SQLUtils.sqlparser.ISQLParser;
import zwj.test.Utils.SQLUtils.sqlparser.SQLParserFactory;
import zwj.test.Utils.SQLUtils.sqlparser.TableFinder;
import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.hive.HiveSqlParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangweijian on 2015/11/24.
 */
public class TestSQLParser {
    public static void main(String[] args) {
        String sql = "create table temp_dws_tfc_wx_access_itm_session_log_h\n" +
                "as\n" +
                "select\n" +
                "to_date(visit_tm) the_dt\n" +
                ",hour(visit_tm) the_hr\n" +
                ",count(1) pv\n" +
                ",sum(stay_time) visit_duration\n" +
                ",session_id\n" +
                ",uuid\n" +
                ",shop_session_id\n" +
                ",buyer_id\n" +
                ",shop_id\n" +
                ",seller_id\n" +
                ",sku_id\n" +
                "from dwd.dwd_tfc_wx_access_log\n" +
                "where dt = timeprocess('${date}',1)  and flag = 2\n" +
                "group by\n" +
                "to_date(visit_tm)\n" +
                ",hour(visit_tm)\n" +
                ",session_id\n" +
                ",uuid\n" +
                ",shop_session_id\n" +
                ",buyer_id\n" +
                ",shop_id\n" +
                ",seller_id\n" +
                ",sku_id";
        String sqlContent = "insert overwrite table  db_990.db_101_tab2 partition(dt=251525) " +
                "select name from  db_990.db_101_tab2;" +
                "select * from db_990.db_101_tab2; ";
        try {
            System.out.println(SQLUtils.format(sqlContent));
            HiveSqlParser hiveSqlParser = HiveSqlParser.parse(sql);
            System.out.println(JSONUtils.toJSON(hiveSqlParser.findTableNames()));//��ӡ����������sql�еı�

            sqlContent = org.apache.commons.lang.StringUtils.replace(sqlContent,";",";\r\n");//Դsql�п��ܻ����;�ָ��Ķ��sql���,������滻���ڷֺź���ӻ��лس�
            List<String> sqlStrs = SQLUtils.stripScript(sqlContent);//���������sql�����Ϊ���������sql
            for (String sqltem : sqlStrs) {
//                SQLResultSet rs = execute(sql, conn, limit, params, dbType, progressID);
                System.out.println(sqltem);
            }

            TableFinder finder = new TableFinder(sqlContent);
            finder.parse();
            List<Table> sourceTables = finder.getSourceTables();
            List<Table> targetTables = finder.getTargetTables();
            List<Table> allTables = new ArrayList<Table>();
            allTables.addAll(sourceTables);
            allTables.addAll(targetTables);
            for(Table table:allTables){
                System.out.println("tableName="+table.getName()+"and DBName="+table.getSchemaName());
            }


            ISQLParser parser = SQLParserFactory.getSQLParser(DBType.Hive, sql);
            System.out.println(parser.generateSQL(true));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
