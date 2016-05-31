package zwj.test.Utils.SQLUtils.sqlparser;

import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.SQLType;
import zwj.test.Utils.SQLUtils.sqlparser.param.AbstractParam;

import java.util.List;

/**
 * sql parser interface
 * Created by bjliuhongbin on 15-5-12.
 */
public interface ISQLParser {
    void registerParams(AbstractParam... params);

    void setLimit(int limit, boolean overwrite) throws SQLException;

    SQLType getType() throws SQLException;

    String generateSQL(boolean format) throws SQLException;

    List<Table> findTableNames() throws SQLException;

    List<String> findDatabases() throws SQLException;

    String findDBIfUseType() throws SQLException;

    String sqlDump() throws SQLException;


}
