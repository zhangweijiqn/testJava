package zwj.test.Reflection;

import java.lang.reflect.Method;

/**
 * Created by zhangwj on 15-12-17.
 */
public class TestReflection {
    private static final String[] Test_Classes = new String[]{"Person"};
    public static void Test(){
        try {
            for (int i$ = 0; i$ < Test_Classes.length; ++i$) {
                String command = "main.zwj.test.Reflection." + Test_Classes[i$];

                Class serviceClass = null;
                serviceClass = Class.forName(command);

                Method m = serviceClass.getMethod("setCount", Integer.class);
                m.invoke((Object) null, 1);//调用静态方法，不用实例化
                System.out.println(Person.getCount());

                m.invoke(serviceClass.newInstance(),3);//实例化了Person
                System.out.println(Person.getCount());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) {
        Test();

    }
}
