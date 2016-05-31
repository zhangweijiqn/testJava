package zwj.test.Utils.SQLUtils.sqlparser.param;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.utils.SQLDateFormat;

import java.util.Date;

public final class DefaultParam extends AbstractParam {
	private Object value;
	
	public DefaultParam(String name, Object value) {
		super(name);
	}

	@Override
	public Object getValue(String params[]) throws SQLException {
		return this.value;
	}

	@Override
	public String parse2SQL(String params[]) throws SQLException {
		Object value = getValue(null);
		if (value instanceof Number) {
			return value.toString();
		} else if (value instanceof String) {
			return "'" + value.toString() + "'";
		} else if (value instanceof Date) {
			return "\"" + SQLDateFormat.format((Date) value) + "\"";
		} else if (value instanceof Boolean) {
			return value.toString();
		}
		
		throw new RuntimeException("不支持[" + getName() + "]变量的数据类型");
	}

    @Override
    public void check(String[] params) throws SQLException {
    }

}
