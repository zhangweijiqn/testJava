package zwj.test.Utils.SQLUtils;

/**
 * 数据库类型
 * Created by bjliuhongbin on 14-3-7.
 */
public enum DBType {
    Hive,
    MYSQL;

    public String getID() {
        return name();
    }

    public String getText() {
        return name();
    }
}
