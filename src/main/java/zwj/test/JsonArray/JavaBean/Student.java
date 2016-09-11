package zwj.test.JsonArray.JavaBean;

/**
 * Created by zhangwj on 15-12-14.
 */
public class Student {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Dep getDep() {
        return dep;
    }

    public void setDep(Dep dep) {
        this.dep = dep;
    }

    String id;
    Dep dep;
}
