package zwj.test.RegularExpression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangwj on 15-12-11.
 */
public class testRE {



    public static void ShowPartitionCommand(){
          final Pattern PATTERN = Pattern.compile("\\s*([\\w\\.]+)(\\s*|(\\s+PARTITION\\s*\\((.*)\\)))\\s*", 2);
          final Pattern PUB_PATTERN = Pattern.compile("\\s*([\\w\\.]+)(\\s*|(\\s*\\((.*)\\)))\\s*", 2);
        String sql = "show partitions v72.v72 partition(dt='2015-12')";
        Matcher m = PATTERN.matcher(sql);
        boolean match = m.matches();
        if(match && m.groupCount() >= 1) {
            String tableName = m.group(1);
        }

        if(match && m.groupCount() >= 4) {
            String partitionSpec = m.group(4);

            if(!match){
                Matcher pub_m = PUB_PATTERN.matcher(sql);
                boolean pub_match = pub_m.matches();
                if(pub_match && pub_m.groupCount() >= 1) {
                    String tableName = m.group(1);
                }

                if(pub_match && pub_m.groupCount() >= 3) {
                    partitionSpec = m.group(4);
                }
            }   }
    }

    public static void checkName(String name){
        final Pattern PATTERN = Pattern.compile("[a-zA-Z_]{1,}[0-9a-zA-Z_]{0,}",Pattern.CASE_INSENSITIVE);
        Matcher m = PATTERN.matcher(name);
        boolean match = m.matches();
        System.out.println(match);
    }

    public static void createTable(){
        final Pattern PATTERN = Pattern.compile("\\s*CREATE\\s+TABLE\\s+(.|[\r\n])*",Pattern.CASE_INSENSITIVE);//注意：  .不能匹配\n
        String sql = "create table tbl_word_count_input(\n" +
                "line string)\n" +
                "PARTITIONED BY ( \n" +
                "dt1 string)\n" +
                "ROW FORMAT DELIMITED \n" +
                "FIELDS TERMINATED BY '\\t' \n" +
                "LINES TERMINATED BY '\\n' ";
        String sql1 = "create table tbl_word_count_input(name String)";
        Matcher m = PATTERN.matcher(sql);
        boolean match = m.matches();
        System.out.println(match);
    }

    public static void createDatabase(){
        final Pattern PATTERN = Pattern.compile("\\s*SHOW\\s+PARTITIONS\\s+(.*)partition()",Pattern.CASE_INSENSITIVE);
        final Pattern PUBLIC_PATTERN = Pattern.compile("\\s*CREATE\\s+DATABASE\\s+([a-zA-Z_]{1,}[a-zA-Z_0-9]{0,})\\s*",Pattern.CASE_INSENSITIVE);
        String sql ="create database vm";
        Matcher m = PUBLIC_PATTERN.matcher(sql);
        boolean match = m.matches();
        System.out.println(match);
    }

    public static void testTruncateTable(String script){
        final Pattern PATTERN1 = Pattern.compile("\\s*truncate\\s+table\\s+(.*)partition\\s+.*",Pattern.CASE_INSENSITIVE);//注意：  .不能匹配\n
        Matcher m = PATTERN1.matcher(script);
        boolean match = m.matches();
        System.out.println(match);
        if(!match){
            final Pattern PATTERN2 = Pattern.compile("\\s*truncate\\s+table\\s+([a-zA-Z_]{1,}[0-9a-zA-Z_]{0,})\\s*",Pattern.CASE_INSENSITIVE);//注意：  .不能匹配\n
            Matcher m2 = PATTERN2.matcher(script);
            System.out.println(m2.matches());
            if(m2.matches() && m2.groupCount() >= 1) {
                String tableName = m2.group(1);
                System.out.println(tableName);
            }
        }
    }

    public static void ParamsFilter(){
        //加入了<的匹配，原来的<>测试未成功
        final Pattern PATTERN = Pattern.compile("<.*|<[^>]*?=[^>]*?&#[^>]*?>|\\b(alert\\(|confirm\\(|expression\\(|prompt\\()|<[^>]*?\\b(onerror|onmousemove|onload|onclick|onmouseover)\\b[^>]*?>|\\b(and|or)\\b\\s*?([\\(\\)\'\"\\d]+?=[\\(\\)\'\"\\d]+?|[\\(\\)\'\"a-zA-Z]+?=[\\(\\)\'\"a-zA-Z]+?|>|<|\\s+?[\\w]+?\\s+?\\bin\\b\\s*?\\(|\\blike\\b\\s+?[\"\'])|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)", 2);
        String str="<script src=http://x.x/hacker.js></script>";

        Matcher m = PATTERN.matcher(str);
        boolean match = m.matches();
        System.out.println(match);
    }
    public static void testGreedyAndNongreedyMode(){
        // Greedy quantifiers
        String match = find("A.*c", "AbcAbc");  // AbcAbc
        System.out.println(match);
        match = find("A.+", "AbcAbc");          // AbcAbc
        System.out.println(match);
        // Nongreedy quantifiers
        match = find("A.*?c", "AbcAbc");        // Abc
        System.out.println(match);
        match = find("A.+?", "AbcAbc");         // Ab
        System.out.println(match);
    }

    public static String find(String patternStr, CharSequence input) {
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    public static void main(String[] args) {

        /*checkName("中国");
        *//*System.out.println(find("A.*c", "AbcAdc"));    //greedy
        System.out.println(find("A.*?c", "AbcAdc"));     //reluctant*//*

        String sql1 = "insert overwrite table pri_result.c111 partition(dt='${date_ymd-4}') select b111.id, b111.b111 from pri_upload.b111 b111 where b111.b111='${date_ymd}'";
        String sql="${date_ymd-4}";
//        String p1 = "'\\$\\{" "date_ymd" "(-(\\d{1,23}))?\\}'";
        final Pattern PATTERN_Select = Pattern.compile(".+\\$\\{date_ymd-(\\d{1,23})?\\}.+",Pattern.CASE_INSENSITIVE);
        Matcher m = PATTERN_Select.matcher(sql1);
        boolean match = m.matches();
        System.out.println(match);
        System.out.println(m.group(1));*/

        String sql = "truncate table msn";
        testTruncateTable(sql);


    }

    public static void test(){
        Pattern pattern = Pattern.compile("b*g");
        Matcher matcher = pattern.matcher("bbg");
        System.out.println(matcher.matches());
        System.out.println(pattern.matches("b*g","bbg"));
        //验证邮政编码
        System.out.println(pattern.matches("[0-9]{6}", "200038"));
        System.out.println(pattern.matches("//d{6}", "200038"));
        //验证电话号码
        System.out.println(pattern.matches("[0-9]{3,4}//-?[0-9]+", "02178989799"));
        getDate("Nov 10,2009");
        charReplace();
        //验证身份证:判断一个字符串是不是身份证号码，即是否是15或18位数字。
        System.out.println(pattern.matches("^//d{15}|//d{18}$", "123456789009876"));
        getString("D:/dir1/test.txt");
        getChinese("welcome to china,江西奉新,welcome,你!");
        validateEmail("luosijin123@163.com");
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
        System.out.println(matcher.group(1));	//分组的索引值是从1开始的，所以取第一个分组的方法是m.group(1)而不是m.group(0)。
    }
    /**
     * 字符替换:本实例为将一个字符串中所有包含一个或多个连续的“a”的地方都替换成“A”。
     *
     */
    public static void charReplace(){
        String regex = "a+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("okaaaa LetmeAseeaaa aa booa");
        String s = matcher.replaceAll("A");
        System.out.println(s);
    }
    /**
     * 字符串提取
     * @param str
     */
    public static void getString(String str){
        String regex = ".+/(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        if(!matcher.find()){
            System.out.println("文件路径格式不正确！");
            return;
        }
        System.out.println(matcher.group(1));
    }
    /**
     * 中文提取
     * @param str
     */
    public static void getChinese(String str){
        String regex = "[//u4E00-//u9FFF]+";//[//u4E00-//u9FFF]为汉字
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while(matcher.find()){
            sb.append(matcher.group());
        }
        System.out.println(sb);
    }
    /**
     * 验证Email
     * @param email
     */
    public static void validateEmail(String email){
        String regex = "[0-9a-zA-Z]+@[0-9a-zA-Z]+//.[0-9a-zA-Z]+";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if(matcher.matches()){
            System.out.println("这是合法的Email");
        }else{
            System.out.println("这是非法的Email");
        }
    }


}
