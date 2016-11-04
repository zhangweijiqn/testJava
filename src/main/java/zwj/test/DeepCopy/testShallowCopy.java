package zwj.test.DeepCopy;

/**
 * Created by zhangwj on 16-11-4.
 */

//Professor没有实现Cloneable接口，默认使用java.lang.Object类的clone()方法
class Professor{
    String name;
    int age;
    Professor(String name,int age){
        this.name=name;
        this.age=age;
    }
}
//Student实现了Cloneable接口
class Student1 implements Cloneable{
    String name;//常量对象。
    int age;
    Professor p;
    Student1(String name,int age,Professor p){
        this.name=name;
        this.age=age;
        this.p=p;
    }
    public Object clone(){
        Student1 o=null;
        try{
            o=(Student1)super.clone();
        }catch(CloneNotSupportedException e){
            System.out.println(e.toString());
        }
        //使用Object类的clone()方法
//        o.p=(Professor)this.p.clone();
        return o;
    }
}


public class testShallowCopy {
    public static void main(String[] args){
        //浅拷贝，变量都会clone过去，但是变量中的引用还是指向原来的空间
        //浅拷贝简单归纳就是只复制一个对象，对象内部存在指向其他对象，数组或引用则不复制。
        Professor p=new Professor("wangwu",50);
        Student1 s1=new Student1("zhangsan",18,p);
        Student1 s2=(Student1)s1.clone();
        s2.name = "zhaoliu";
        s2.age = 20;
        s2.p.name="lisi";
        s2.p.age=30;
        //学生1的教授也变成了lisi,age为30
        System.out.println("name="+s1.name+","+"age="+s1.age+",pname="+s1.p.name+","+"page="+s1.p.age);//name=zhangsan,age=18,pname=lisi,page=30
        System.out.println("name="+s2.name+","+"age="+s2.age+",pname="+s2.p.name+","+"page="+s2.p.age);//name=zhaoliu,age=20,pname=lisi,page=30
    }
}
