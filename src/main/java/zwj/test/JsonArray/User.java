package zwj.test.JsonArray;

/**
 * Created by zhangweijian on 2015/12/1.
 */
public class User {
    private int id;
    private String name;
    private String gender;

    public User() {

    }

    public User(int id, String name, String gender) {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "id=" + id + "��name=" + name + "��gender=" + gender;
    }
}
