package zwj.test.Utils.StringUtils;

/**
 * Created by zhangwj on 16-3-4.
 * 测试的是 org.apache.commons.lang.StringUtils，
 * 为了便于查看类的方法，StringUtils代码拷贝到了当前目录中
 */
public class testStringUtils {

    static void testHashCode(){
        String str = "test";
        System.out.println(str.hashCode());//3556498

        Integer i = 10;
        System.out.println(i.hashCode());//10

        Double d = 10.123;
        System.out.println(d.hashCode());//-1694044644

    }

    void testStringUtils(){
        String str = "abcdedfg";
        str = ApacheStringUtils.substringAfter(str,"a");
        str = ApacheStringUtils.substringBeforeLast(str,"d");
        String splits[] = ApacheStringUtils.split(str,"d");
        Boolean isAlpha = ApacheStringUtils.isAlpha(str);
        Boolean isNumber = ApacheStringUtils.isNumeric(str);
        Boolean isExists = ApacheStringUtils.contains(str,"cd");
    }
    public static void main(String[] args) {

        testHashCode();

    }
}
