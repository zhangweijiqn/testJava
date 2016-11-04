package zwj.test.DeepCopy;

/**
 * Created by zhangwj on 16-11-4.
 */

//Professor没有实现Cloneable接口，默认使用java.lang.Object类的clone()方法
class Professor2 implements Cloneable{
    String name;
    int age;
    Professor2(String name,int age){
        this.name=name;
        this.age=age;
    }
    public Object clone(){
        Object o=null;
        try{
            o=super.clone();
        }catch(CloneNotSupportedException e){
            System.out.println(e.toString());
        }
        return o;
    }
}

//Student实现了Cloneable接口
class Student2 implements Cloneable{
    String name;//常量对象。
    int age;
    Professor2 p;
    Student2(String name,int age,Professor2 p){
        this.name=name;
        this.age=age;
        this.p=p;
    }
    public Object clone(){
        Student2 o=null;
        try{
            o=(Student2)super.clone();
        }catch(CloneNotSupportedException e){
            System.out.println(e.toString());
        }
        //使用Object类的clone()方法
        o.p=(Professor2)this.p.clone();
        return o;
    }
}

public class testDeepCopy {

    public static void main(String[] args){
        //深拷贝，将对象中的所有字段复制到新的对象中。不过，无论是对象的值类型字段，还是引用类型字段，都会被重新创建并赋值
        Professor2 p=new Professor2("wangwu",50);
        Student2 s1=new Student2("zhangsan",18,p);
        Student2 s2=(Student2)s1.clone();
        s2.name = "zhaoliu";
        s2.age = 20;
        s2.p.name="lisi";
        s2.p.age=30;
        //学生1的教授也变成了lisi,age为30
        System.out.println("name="+s1.name+","+"age="+s1.age+",pname="+s1.p.name+","+"page="+s1.p.age);//name=zhangsan,age=18,pname=lisi,page=30
        System.out.println("name="+s2.name+","+"age="+s2.age+",pname="+s2.p.name+","+"page="+s2.p.age);//name=zhaoliu,age=20,pname=lisi,page=30
    }

}


