package zwj.test.JsonArray.GsonTest;



import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * SQL执行结果集
 * Created by bjliuhongbin on 14-3-26.
 */
public class SQLResultSet implements Serializable {
    private String orgSQL;
    private String sql;

    private List<String> logs;

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }

    private ResultType type = ResultType.QUERY;
    private long cost;
    private String message;
    private int total;

    private List<ColumnInfo> metaCols = new ArrayList<ColumnInfo>();

    private List<List<CellData>> result = new ArrayList<List<CellData>>();

    public String getOrgSQL() {
        return orgSQL;
    }

    public void setOrgSQL(String orgSQL) {
        this.orgSQL = orgSQL;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public ResultType getType() {
        return type;
    }

    public void setType(ResultType type) {
        this.type = type;
    }

    public boolean isQuery() {
        return type == ResultType.QUERY;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ColumnInfo> getMetaCols() {
        return metaCols;
    }

    public List<List<CellData>> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "SQLResultSet{" +
                "total=" + total +
                ", metaCols=" + metaCols +
                ", result.size=" + result.size() +
                '}';
    }

    public void load(ResultSet rs) throws Exception {
        // 获得结果集相关属性描述对象
        ResultSetMetaData metaData = rs.getMetaData();
        loadColumnInfo(metaData);

        int colCnt = metaData.getColumnCount();
        while (rs.next()) {
                List<CellData> row = new ArrayList<CellData>();
            boolean add = false;
            for (int i = 1; i <= colCnt; i++) {
//                String tableName = metaData.getTableName(i);
                String colName = metaData.getColumnName(i);
                colName = URLEncoder.encode(colName, "UTF-8")/*colName.replace("(", "_").replace(")", "_").replace(".", "_")*/;// 处理页面异常
                Object value = rs.getObject(i);

                CellData cell = new CellData(colName + "_" + i, value == null ? null : value.toString());
//                CellData cell = new CellData(colName + "_" + i, value);
                row.add(cell);

                if (!(value == null ||
                        (value instanceof String && StringUtils.isEmpty((String) value)))) {
                    add = true;
                }
            }
            result.add(row);
        }
    }

    private void loadColumnInfo(ResultSetMetaData metaData) throws Exception {
        this.metaCols.clear();

        List<ColumnInfo> cols = new ArrayList<ColumnInfo>();
        int colCnt = metaData.getColumnCount();

        for (int i = 1; i <= colCnt; i++) {
//            String tableName = metaData.getTableName(i);
            String colName = metaData.getColumnName(i);
            DataType type = DataType._STRING;

            ColumnInfo colInfo = new ColumnInfo(colName + "_" + i, colName, type);
            cols.add(colInfo);
        }

        this.metaCols.addAll(cols);
    }
}
