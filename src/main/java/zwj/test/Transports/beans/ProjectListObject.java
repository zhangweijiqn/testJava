package zwj.test.Transports.beans;

/**
 * Created by zhangweijian on 2015/10/26.
 */
public class ProjectListObject {
    private String id;
    private String name;
    private String desc;
    // for index
    private int dbNumber;
    private int tableNumber;
    private int sqlTaskNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getDbNumber() {
        return dbNumber;
    }

    public void setDbNumber(int dbNumber) {
        this.dbNumber = dbNumber;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getSqlTaskNumber() {
        return sqlTaskNumber;
    }

    public void setSqlTaskNumber(int sqlTaskNumber) {
        this.sqlTaskNumber = sqlTaskNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
