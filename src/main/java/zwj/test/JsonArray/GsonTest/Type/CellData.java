package zwj.test.JsonArray.GsonTest.Type;

import java.io.Serializable;

public class CellData implements Serializable {

	// ��ID
	private String field;
	
	private Object value;
	
	public CellData(String field, Object value) {
		this.field = field;
		this.value = value;
	}

	public CellData() {
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
