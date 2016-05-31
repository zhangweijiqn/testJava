package zwj.test.Utils.UUID;

/**
 * Created by zhangweijian on 2015/11/24.
 */
import java.util.Calendar;
import  java.util.UUID;
public class TestUUID {
    public static String getKey() throws Exception {
        //produce random String
        String key = UUID.randomUUID().toString()/*MD5Util.md5Hex(sqlContent)*/;
        return key;
    }
    public static String getTimeKey(){
        //produce key according to time
        final String instanceId = DateUtils.formatN(Calendar.getInstance().getTime()) + OrderGenerator.newOrder();
        return instanceId;
    }


    public static void main(String[] args) throws Exception {
        for(int i=0;i<10;++i)
            System.out.println(getKey());
        for(int i=0;i<10;++i)
            System.out.println(getTimeKey());
    }
}
