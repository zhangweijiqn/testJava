package zwj.test.Utils.SQLUtils.sqlparser.base;

import zwj.test.Utils.SQLUtils.sqlparser.ISQLParser;
import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.ErrorCode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.SQLType;
import zwj.test.Utils.SQLUtils.sqlparser.param.AbstractParam;
import zwj.test.Utils.SQLUtils.sqlparser.param.SQLParamAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.param.SysDate;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.alter.Alter;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 基础SQL解析，区别于特殊sql解析器，如Hive
 * Created by bjliuhongbin on 15-5-12.
 */
public class BaseSQLParser implements ISQLParser {
    private String orgSQL;// 最原始的SQL语句
    private Statement statement;

    private SQLParamAdaptor paramAdaptor;// sql参数适配器

    private int limitNum = - 1;// limit行数
    private boolean overWriteLimit;// 是否要覆盖Limit

    private SQLType type = SQLType.UNKNOWN;

    private BaseSQLParser(String orgSQL) throws SQLException {
        this.orgSQL = orgSQL;

        init();
    }

    public static BaseSQLParser parse(String orgSQL) throws SQLException {
        return new BaseSQLParser(orgSQL);
    }

    @Override
    public void registerParams(AbstractParam... params) {// todo 参数没处理
        this.paramAdaptor.addParams(params);
    }

    @Override
    public void setLimit(int limit, boolean overwrite) throws SQLException {
//        this.limitNum = limit;
//        this.overWriteLimit = overwrite;
        if (statement == null
                || !(statement instanceof Select)) return ;

        Select select = (Select) statement;
        SelectBody selectBody = select.getSelectBody();
        setLimit(limit, overwrite, selectBody);
    }

    @Override
    public SQLType getType() throws SQLException {
        if (this.type != SQLType.UNKNOWN) {
            return this.type;
        }

        if (statement == null) return SQLType.UNKNOWN;

        if (statement instanceof Delete) {
            return SQLType.DELETE;
        } else if (statement instanceof Select) {
            return SQLType.SELECT;
        } else if (statement instanceof Insert) {
            return SQLType.INSERT;
        } else if (statement instanceof CreateTable) {
            return SQLType.CREATE_TABLE;
        } else if (statement instanceof Drop) {
            return SQLType.DROP_TABLE;
        } else if (statement instanceof Truncate) {
            return SQLType.TRUNK_TABLE;
        } else if (statement instanceof Alter) {
            return SQLType.ALERT_TABLE;
        } else {
            return SQLType.UNKNOWN;
        }
    }

    @Override
    public String generateSQL(boolean format) throws SQLException {
        if (type != SQLType.UNKNOWN || statement == null) {
            return orgSQL;
        }

        // handle format
        if (!format) {
            return statement.toString();
        } else {
            return statement.toString();
        }

    }

    @Override
    public List<Table> findTableNames() throws SQLException {
        TableFinder finder = new TableFinder();

        return finder.findTables(this.statement);
    }

    @Override
    public List<String> findDatabases() throws SQLException {

        if (type == SQLType.USE) {
            List<String> ret = new ArrayList<String>();
            ret.add(findDBIfUseType());
            return ret;
        } else {
            DatabaseFinder finder = new DatabaseFinder();
            List<String> databases = finder.findDatabases(this.statement);

            return databases;
        }
    }

    @Override
    public String findDBIfUseType() throws SQLException {
        if (type == SQLType.USE) {
            String sql = this.orgSQL.toUpperCase();
            int index = sql.indexOf("USE");
            if (index >= 0 && sql.length() > 3) {
                String db = sql.substring(index + 3);
                db = db.trim();

                return db;
            }
        }

        return null;
    }

    @Override
    public String sqlDump() throws SQLException {
        return generateSQL(true);
    }

    private void init() throws SQLException {
        discernCommandType();
        if (type != SQLType.UNKNOWN) {
            return ;
        }

        CCJSqlParserManager manager = new CCJSqlParserManager();
        StringReader reader = new StringReader(orgSQL);
        try {
            statement = manager.parse(reader);
        } catch (JSQLParserException e) {
            throw new SQLException(e.getMessage(), e, ErrorCode.DEFAULT_SQL_ERROR);
        }

        paramAdaptor = new SQLParamAdaptor();
        initDefaultParams();
    }

    // 初始化默认参数
    private void initDefaultParams() {
        registerParams(new SysDate());
    }

    private void discernCommandType() {
        if (orgSQL.matches(REGEX_USE)) {
            type = SQLType.USE;
        } else if (orgSQL.matches(REGEX_SHOW_DATABASES)) {
            type = SQLType.SHOW_DATABASES;
        } else if (orgSQL.matches(REGEX_SHOW_TABLES)) {
            type = SQLType.SHOW_TABLES;
        } else if (orgSQL.matches(REGEX_SHOW_OTHERS)) {
            type = SQLType.SHOW_OTHERS;
        } else if (orgSQL.matches(REGEX_DESC)) {
            type = SQLType.DESC_TABLE;
        }
    }

    /**
     * 构建SQL解析上下文
     * @return
     */
    private BaseSQLContext createContext() {
        return new BaseSQLContext(this.paramAdaptor);
    }

    private void setLimit(int limit, boolean overwrite, SelectBody body) throws SQLException {
        if (body instanceof PlainSelect) {
            setLimit(limit, overwrite, (PlainSelect) body);
        } else if (body instanceof SetOperationList) {
            setLimit(limit, overwrite, (SetOperationList) body);
        } else if (body instanceof WithItem) {
            setLimit(limit, overwrite, (WithItem) body);
        }
    }

    private void setLimit(int limit, boolean overWriteLimit, PlainSelect select) throws SQLException {
        Limit _limit = select.getLimit();
        if (_limit == null) {
            _limit = new Limit();
            _limit.setRowCount(limit);

            select.setLimit(_limit);
        } else if (overWriteLimit) {
            _limit.setRowCount(limit);
            _limit.setLimitNull(false);
            _limit.setRowCountJdbcParameter(false);
        }
    }

    private void setLimit(int limit, boolean overWriteLimit, SetOperationList select) throws SQLException {
        Limit _limit = select.getLimit();
        if (_limit == null) {
            _limit = new Limit();
            _limit.setRowCount(limit);

            select.setLimit(_limit);
        } else if (overWriteLimit) {
            _limit.setRowCount(limit);
            _limit.setLimitNull(false);
            _limit.setRowCountJdbcParameter(false);
        }
    }

    private void setLimit(int limit, boolean overWriteLimit, WithItem select) throws SQLException {
        setLimit(limit, overWriteLimit, select.getSelectBody());
    }

    public static void main(String [] args) {

        String sql = "DESC t1";
//        BaseSQLParser parser = BaseSQLParser.parse(sql); desc
        System.out.println(sql.matches(REGEX_DESC));
    }

    // 匹配 use <db_name>
    private final static String REGEX_USE = "^[\\s]*(?i)use(?-i)[\\s]+[\\w]+[\\s]*$";
    private final static String REGEX_SHOW_TABLES = "^[\\s]*(?i)show[\\s]+tables(?-i)[\\s]*$";
    private final static String REGEX_SHOW_DATABASES = "^[\\s]*(?i)show[\\s]+databases(?-i)[\\s]*$";
    private final static String REGEX_SHOW_OTHERS = "^[\\s]*(?i)show(?-i)[\\s]+[\\s\\S]*[\\s]*$";
    private final static String REGEX_DESC = "^[\\s]*(?i)desc(ribe)?(?-i)[\\s]+[\\s\\S]*[\\s]*$";
}
