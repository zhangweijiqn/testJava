package zwj.test.Utils.FileUtils;

import java.util.Properties;

/**
 * Created by zhangweijian on 2015/11/24.
 */
public class TestFileUtils {
    private final static String PROPERTY_FILE_NAME = "D:\\MyCodes\\Test Projects\\Java_test\\src\\main.zwj\\test\\FileUtils\\jdbc.properties";
    private  static Properties properties = null;
    static {
        properties = FileUtils.loadProperties(PROPERTY_FILE_NAME);
    }

    public static void main(String[] args) {
        System.out.println(properties);
    }
}
