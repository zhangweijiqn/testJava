package zwj.test.JsonArray.GsonTest.Type;


import java.io.Serializable;

public class ColumnInfo implements Serializable {
	
	String id;

	// ��ID
	private String field;

	// ����
	private String title;

	// ����
	private DataType type;
	

//	public ColumnInfo(String field, DataType type) {
//		this.id = field;
//		this.field = field.replace("(", "_").replace(")", "_").replace(".", "_");
//		this.title = field;
//		this.type = type;
//	}

    public ColumnInfo(String field, String title, DataType type) {
        this.id = field;
        this.field = field/*.replace("(", "_").replace(")", "_").replace(".", "_")*/;
        this.title = title;
        this.type = type;
    }

    public ColumnInfo() {

    }
    public ColumnInfo(String id,String field, String title, DataType type) {
        this.id = field;
        this.field = field/*.replace("(", "_").replace(")", "_").replace(".", "_")*/;
        this.title = title;
        this.type = type;
    }

    public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }
}
