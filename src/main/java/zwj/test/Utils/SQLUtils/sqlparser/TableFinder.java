package zwj.test.Utils.SQLUtils.sqlparser;

import zwj.test.Utils.SQLUtils.DBType;
import zwj.test.Utils.SQLUtils.SQLUtils;
import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.SQLType;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.TableScope;
import zwj.test.Json.JSONUtils;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * used to find source tables
 * Created by bjliuhongbin on 15-1-16.
 */
public class TableFinder {
    private Log log = LogFactory.getLog(getClass());

    private List<String> sqlList = new ArrayList<String>();
    private List<Table> targetTables = new ArrayList<Table>();
    private List<Table> createTables = new ArrayList<Table>();

    public List<Table> getCreateTables() {
        return createTables;
    }

    public void setCreateTables(List<Table> createTables) {
        this.createTables = createTables;
    }

    private List<Table> sourceTables = new ArrayList<Table>();


    public TableFinder(String script) {
        try {
            sqlList.addAll(SQLUtils.stripScript(script));
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
        }
    }

    public void parse() throws Throwable {
        String currDB = "default";
        for (String sql : sqlList) {
//            HiveSqlParser parser = HiveSqlParser.parse(sql);
            ISQLParser parser = SQLParserFactory.getSQLParser(DBType.Hive, sql);
            SQLType type =  parser.getType();
            if (type == SQLType.USE) {
                List<String> database = parser.findDatabases();
                currDB = database.get(0);

                continue;
            }

            List<zwj.test.Utils.SQLUtils.sqlparser.beans.Table> tables = parser.findTableNames();
            for (zwj.test.Utils.SQLUtils.sqlparser.beans.Table table : tables) {
                String dbName = table.getSchemaName();
                if (StringUtils.isEmpty(dbName)) {
                    dbName = currDB;
                }

                String group = sql == null ? null : sql.trim().toUpperCase();
                String tableName = table.getName();
                TableScope scope = table.getTableScope();
                switch (scope) {
                    case CREATE_TABLE_LIKE:
                    case DROP_TABLE:
                    case TRUNCATE_TABLE:
                    case ALTER_TABLE:
                    case SHOW_TABLE_ABOUT:
                    case DESC_TABLE:
                    case QUERY_TABLE:
                    case JOIN_TABLE:
                    {
                        Table _table = new Table(dbName, tableName);
                        if (!this.sourceTables.contains(_table)) {
                            this.sourceTables.add(_table);
                        }
                    }
                        break;
                    case CREATE_TABLE:
                    {
                        Table _table = new Table(dbName, tableName);
                        getTargetTables(group, _table);
                        if (!this.createTables.contains(_table)) {
                            this.createTables.add(_table);
                        }
                    }
                    break;
                    case LOAD_TABLE:
                    case INSERT_TABLE:
                    {
                        Table _table = new Table(dbName, tableName);
                        getTargetTables(group, _table);
                    }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void getTargetTables(String group, Table _table) {
        if (!this.targetTables.contains(_table)) {
            this.targetTables.add(_table);
        }
    }

    public List<Table> getTargetTables() {
        return targetTables;
    }

    public List<Table> getSourceTables() {
        return sourceTables;
    }

    public static void main(String[] args) throws Exception {
        // PARTITION (dt=1)
        String sql = "create table temp.temp_smart_fw_cat_rela_7d\n" +
                "as\n" +
                "select\n" +
                "a.category_1_id as category_1_id,\n" +
                "b.cid as category_2_id,\n" +
                "a.cate_name,\n" +
                "a.lev,\n" +
                "b.fws_pin as seller_id,\n" +
                "b.id as sku_id\n" +
                "from temp.fw_cat_temp_7d a\n" +
                "join (select id,cid,fws_pin from temp.fw_market_service_7d) b\n" +
                "on a.category_2_id=b.cid" ;

//        HiveSqlParser parser = HiveSqlParser.parse(sql);
        ISQLParser parser = SQLParserFactory.getSQLParser(DBType.Hive, sql);
        System.out.println(JSONUtils.toJSON(parser.findTableNames()));
    }
}
