package zwj.test.getConfigure;

/**
 * Created by zhangweijian on 2015/10/9.
 * ��Ҫʹ�õ���jar����
 commons-collections-3.2.1.jar��commons-configuration-1.10.jar��commons-lang-2.6.jar��commons-logging-1.2.jar��
 */
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

public class xmlLoaderTest {
    public static void main(String[] args) throws ConfigurationException {
        Configuration config = null;
        config = new XMLConfiguration("src/main.zwj/test/getConfigure/config.xml");
        String name = config.getString("Account.name");
        System.out.println("name:" + name);
    }
}
