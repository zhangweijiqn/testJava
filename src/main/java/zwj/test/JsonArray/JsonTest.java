package zwj.test.JsonArray;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by yuzhitao on 2015/12/15.
 */
public class JsonTest {
    private static final Pattern PATTERN = Pattern.compile("\\s*CREATE\\s+DATABASE\\s+(.+)\\s*");
    private static final Pattern PUBLIC_PATTERN = Pattern.compile("\\s*CREATE\\s+DATABASE\\s+([a-zA-Z_]{1,}[0-9]{0,})\\s*");
    private static final Pattern PATTERN_Select = Pattern.compile("\\s*SELECT\\s*.+",Pattern.CASE_INSENSITIVE);
    private static final Pattern PATTERN_Select_Limit = Pattern.compile("\\s*SELECT.+LIMIT\\s+(\\d*)",Pattern.CASE_INSENSITIVE);
    public static void testCreateDB(){
        String sql = "create database db_test002%";
        Matcher m  = PATTERN.matcher(sql);
        System.out.println(m.matches());
        System.out.println(m.group(1));
        /*while (m.find()){
            System.out.println(m.group(1));
        }*/
    }

    public static void main(String[] args) {
        String sql = "select * from aaa limit 2000";
        Matcher m1  = PATTERN_Select.matcher(sql);

        System.out.println(m1.matches());
        Matcher m2 = PATTERN_Select_Limit.matcher(sql);
        System.out.println(m2.matches());
        System.out.println(m2.group(1));
    }

    /**
     * 日期提取:提取出月份来
     * @param str
     */
    public static void getDate(String str){
        String regEx="([a-zA-Z]+)|//s+[0-9]{1,2},//s*[0-9]{4}";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        if(!matcher.find()){
            System.out.println("日期格式错误!");
            return;
        }
        System.out.println(matcher.group(1));   //分组的索引值是从1开始的，所以取第一个分组的方法是m.group(1)而不是m.group(0)。
    }


}
