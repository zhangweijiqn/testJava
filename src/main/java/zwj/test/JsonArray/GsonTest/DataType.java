package zwj.test.JsonArray.GsonTest;

/**
 * ģ����������
 * Created by bjliuhongbin on 14-3-13.
 */
public enum DataType {
    _TINYINT,
    _SMALLINT,
    _INT,
    _BIGINT,
    _BOOLEAN,
    _FLOAT,
    _DOUBLE,
    _STRING,
    _BINARY,
    _TIMESTAMP,
    _DECIMAL,
    _CHAR,
    _DATE;

    private int len = -1;
    private int decimal = -1;

    DataType() {
    }

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public String getRealType() {
        String typeName = this.name().substring(1);
        if (len < 0) {
            return typeName;
        } else if (decimal < 0) {
            return typeName + "(" + len + ")";
        } else {
            return typeName + "(" + len + "," + decimal + ")";
        }
    }

    public String getRealName() {
        String typeName = this.name().substring(1);
        return typeName;
    }

    public static DataType createByRealType(String type) {

        String typeName = null;

        int leftBracket = type.indexOf("(");
        if (leftBracket < 0) {
            typeName = type;
        } else {
            typeName = type.substring(0, leftBracket);
        }

        DataType dataType;
        if (typeName.startsWith("_")) {
            dataType = DataType.valueOf(typeName.toUpperCase());
        } else {
            dataType = DataType.valueOf("_" + typeName.toUpperCase());
        }

        int comma = type.indexOf(",");
        int rightBracket = type.indexOf(")");

        if (comma > 0 && rightBracket > 0) {
            String lenStr = type.substring(leftBracket + 1, comma);
            dataType.setLen(Integer.valueOf(lenStr.trim()));

            String decimalStr = type.substring(comma + 1, rightBracket);
            dataType.setDecimal(Integer.valueOf(decimalStr.trim()));
        } else if (comma > 0 && rightBracket <= 0) {
            String lenStr = type.substring(leftBracket + 1, rightBracket);
            dataType.setLen(Integer.valueOf(lenStr.trim()));
        }

        return dataType;
    }
}
