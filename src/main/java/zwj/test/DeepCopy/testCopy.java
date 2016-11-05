package zwj.test.DeepCopy;

/**
 * Created by zhangwj on 16-11-4.
 */
class Student
{
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    String name;
    int age;
    Student(String name,int age){
        this.name=name;
        this.age=age;
    }
}
public class testCopy {
    public static void main(String[] args) {
        // 直接赋值对象，两个都指向同一个内存空间
        Student s1= new Student("zhangsan",18);
        Student s2=s1;
        s2.name="lisi";
        s2.age=20;
        System.out.println("name="+s1.name+","+"age="+s1.age);//name=lisi,age=20
        System.out.println("name="+s2.name+","+"age="+s2.age);//name=lisi,age=20
    }
}
