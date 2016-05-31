package zwj.test.SecurityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by zhangweijian on 2015/12/3.
 */
public class TestJavaSecurity {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(System.getProperty("java.security.manager"));//获得系统属性
        InputStream in = new FileInputStream(new File("src/main.zwj/test/SecurityManager/java.policy"));//该操为读操作，会触发拦截


    }
}
