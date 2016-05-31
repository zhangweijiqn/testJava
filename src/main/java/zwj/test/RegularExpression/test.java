package zwj.test.RegularExpression;

import org.apache.commons.lang.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangwj on 16-5-13.
 */

public class test {

    public static String validateTruncate(String script){
        final Pattern PATTERN1 = Pattern.compile("\\s*truncate\\s+table.+partition.*",Pattern.CASE_INSENSITIVE);//注意：  .不能匹配\n
        Matcher m = PATTERN1.matcher(script);
        boolean match = m.matches();
        System.out.println(match);
        if(!match){
            final Pattern PATTERN2 = Pattern.compile("\\s*truncate\\s+table\\s+(.+)\\s*",Pattern.CASE_INSENSITIVE);
            Matcher m2 = PATTERN2.matcher(script);
            System.out.println(m2.matches());
            if(m2.matches() && m2.groupCount() >= 1) {
                String tableName = m2.group(1);
                System.out.println(tableName);
                return tableName;
            }
            return "2";
        }
        return "1";
    }

    private static String[] getDBTableName(String name) {
        String[] pair = new String[2];
        int dotPosition = name.indexOf('.');
        if (dotPosition <= 0) {
            pair[1] = name;
        } else {
            String dbName = name.substring(0, dotPosition);
            String tableName = name.substring(dotPosition+1);
            pair[0] = dbName;
            pair[1] = tableName;
        }
        return pair;
    }

    public static void main(String[] args) {
        String sql = "truncate table msn partition (dt='1213')";
//        String sql = "truncate table aaa.bbb";
        String table = validateTruncate(sql);
        if(StringUtils.isEmpty(table)){
            System.out.println("truncate table partition");
        }else{
            String[] tableName=getDBTableName(table);
            System.out.println(tableName[0]+":"+tableName[1]);
        }

    }
}