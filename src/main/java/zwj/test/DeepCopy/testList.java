package zwj.test.DeepCopy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhangwj on 16-11-5.
 */

public class testList {

    static void testCopyList(){
        List a = new ArrayList<Integer>();
        for(int i=0;i<10;++i){
            a.add(i);
        }
        List b = a;
        b.set(0,10);
        for(int i=0;i<a.size();++i){
            System.out.println(a.get(i)); // 10,2,3,4,5,6,7,8,9
        }
    }

    static void testShallowCopyList(){
        List a = new ArrayList<Integer>();
        for(int i=0;i<10;++i){
            a.add(i);
        }
        List b = new ArrayList<Integer>();
        b.addAll(a);    //使用addAll的方式把a的值全部放入b中
        b.set(0,10);
        System.out.println(a.get(0));
        System.out.println(b.get(0));
    }

    static void testShallowCopyList2(){
        List a = new ArrayList<Student>();
        Student s = new Student("zhangsan",21);
        a.add(s);
        List b = new ArrayList<Student>();
        b.addAll(a);    //使用addAll的方式把a的值全部放入b中
        b.add(new Student("lisi",22));
        b.set(0,new Student("wangwu",12));

        System.out.println(a.get(0).getClass().getName());
        System.out.println(b.get(0).getClass().getName());
    }

    static void testDeepCopyList(){
        List<String> src = new ArrayList<String>();
        src.add("111");
        src.add("222");
        src.add("333");
        src.add("444");
        List<String> dic = new ArrayList<String>(Arrays.asList(new String[src
            .size()]));
        Collections.copy(dic, src);
        for (Object s : dic) {
            System.out.println(s);
        }
    }

    public static void main(String[] args) {
//        testCopyList();
//        testShallowCopyList();
        testShallowCopyList2();
        testDeepCopyList();
    }
}
