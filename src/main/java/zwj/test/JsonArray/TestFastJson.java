package zwj.test.JsonArray;

import com.alibaba.fastjson.JSON;
import org.apache.avro.data.Json;
import org.apache.log4j.Logger;
import zwj.test.JsonArray.JavaBean.Dep;
import zwj.test.JsonArray.JavaBean.DeptSub;
import zwj.test.JsonArray.JavaBean.Student;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwj on 15-12-14.
 */

public class TestFastJson {
    protected static final Logger LOGGER = Logger.getLogger(Json.class);
    public static void main(String[] args) {

        DeptSub deptsub = new DeptSub();
        deptsub.setSub("sub");

        List<DeptSub> deptsubs = new ArrayList<DeptSub>();
        deptsubs.add(deptsub);

        Dep dept = new Dep();
        dept.setName("name1");
        dept.setOwner("owner1");
        dept.setDeptSub(deptsubs);
        Dep dept2 = new Dep();
        dept2.setName("name2");
        dept2.setOwner("owner2");
        dept.setDeptSub(deptsubs);
        List<Dep> depts = new ArrayList<Dep>();

        depts.add(dept);
        depts.add(dept2);


        List<Student> students = new ArrayList<Student>();
        Student student = new Student();
        student.setId("id");
        student.setDep(dept);

        Student student2 = new Student();
        student2.setId("id");
        student2.setDep(dept);

        students.add(student);
        students.add(student2);
        String jsonString = JSON.toJSONString(student) ;
        LOGGER.info(jsonString);
        Student  studentReturn = JSON.parseObject(JSON.toJSONString(student), Student.class);

        List<Student>  studentsReturn = JSON.parseArray(JSON.toJSONString(students), Student.class);

        LOGGER.info(JSON.toJSONString(studentReturn));
        LOGGER.info(JSON.toJSONString(studentsReturn));
        for (int i = 0; i < studentsReturn.size(); i++) {
            LOGGER.info(JSON.toJSONString(studentsReturn.get(i)));
        }

//        List<CellData> l = new ArrayList<CellData>();
//        for(int i=0;i<3;++i){
//            CellData cellData = new CellData(i+"",i*i+"");
//            l.add(cellData);
//        }
//        List<List<CellData>> ls = new ArrayList<List<CellData>>();
//        ls.add(l);
//        List<CellData>  ll = JSON.parseArray(JSON.toJSONString(ls), CellData.class);

    }
}
