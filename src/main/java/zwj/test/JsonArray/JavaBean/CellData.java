package zwj.test.JsonArray.JavaBean;

/**
 * Created by zhangwj on 15-12-14.
 */
public class CellData {
    private String field;

    private Object value;

    public CellData(String field, Object value) {
        this.field = field;
        this.value = value;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

}
