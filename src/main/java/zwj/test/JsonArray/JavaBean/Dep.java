package zwj.test.JsonArray.JavaBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwj on 15-12-14.
 */
public class Dep {
    String name;
    String owner;

    public Dep() {
        deptSub = new ArrayList<DeptSub>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DeptSub> getDeptSub() {
        return deptSub;
    }

    public void setDeptSub(List<DeptSub> deptSub) {
        for(DeptSub dep:deptSub){
            this.deptSub.add(dep) ;
        }
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    List<DeptSub> deptSub;
}
