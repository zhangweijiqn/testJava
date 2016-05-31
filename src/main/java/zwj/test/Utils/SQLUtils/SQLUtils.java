package zwj.test.Utils.SQLUtils;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import java.util.ArrayList;
import java.util.List;

import zwj.test.Utils.SQLUtils.sqlparser.SQLParserFactory;
import zwj.test.Utils.SQLUtils.sqlparser.ISQLParser;

/**
 * 关于SQL 的工具类
 * Created by bjliuhongbin on 14-5-5.
 */
public class SQLUtils {
    public static List<String> stripScript(String script) throws Exception {
        List<String> sqlStrs = new ArrayList<String>();


        String sql = "";
        String lineSeparator = System.getProperty("line.separator", "/n");//'/n'传递的是默认值，如果没有获取到line.separator属性，则使用传递的默认值
        String lines[] = script.split("[" + lineSeparator + "]");
        int pos = 0;
        for (String line : lines) {
            line = line.trim();
            if (StringUtils.isEmpty(line)) {
//                line = " ";
                pos ++;
                continue;
            }
            int commentIndex = line.indexOf("--");
            if (commentIndex >=0) {
                line = line.substring(0, commentIndex).trim();
            }

            commentIndex = line.indexOf("##");
            if (commentIndex >=0) {
                line = line.substring(0, commentIndex).trim();
            }

            if (line.endsWith(";") || pos >= lines.length - 1) {
                if (line.endsWith(";")) {
                    line = line.substring(0, line.length() - 1).trim();
                } else {
                    line = line.trim();
                }
                sql += " " + line;
                if (!StringUtils.isEmpty(sql)) {
                    //sql = sql.replaceAll("/\\*[\\w\\W]*\\*/", "");// 去掉注释
                    sql = sql.replaceAll("\\t", " ");
                    sql = sql.replaceAll("\\n", " ");
                    sqlStrs.add(sql);
                }

                sql = "";// reset
            } else {
                sql += " " + line;
            }

            pos ++;
        }

        return sqlStrs;
    }

    public static String format(String script) throws Throwable {
        try {
            return format(script, DBType.MYSQL);
        } catch (Throwable e) {
            return format(script, DBType.Hive);
        }
    }

    public static String format(String script, DBType dbType) throws Throwable {
        StringBuilder ret = new StringBuilder();
        if (StringUtils.isEmpty(script)) {
            return ret.toString();
        }

        String enterChar = SystemUtils.LINE_SEPARETOR;
        String lines[] = script.split("[" + enterChar + "]");
        StringBuilder sql = new StringBuilder();
        boolean readingComment = false;
        for (String line : lines) {
            if (line.matches(COMMENT_REGEX)) {
                if (!readingComment && sql.length() > 0) {// sql中间的注释行不处理
                    continue;
                }
                ret.append(line).append(enterChar);
            } else if (line.matches(COMMENT_START_REGEX)) {// /*
                ret.append(line).append(enterChar);
                readingComment = true;
            } else if (line.matches(COMMENT_END_REGEX)) {// */
                ret.append(line).append(enterChar);
                readingComment = false;
            } else if (line.matches(SQL_END_REGEX)) {// end sql
                sql.append(line.substring(0, line.length() - 1));
                String formatSQL = formatSQL(sql.toString(), dbType);
                ret.append(formatSQL).append(enterChar).append(";").append(enterChar);

                sql = new StringBuilder();
            } else if (readingComment){
                ret.append(line).append(enterChar);
            } else {
                sql.append(line).append(enterChar);
            }
        }

        if (sql.length() > 0) {// 处理最后一条sql没有分号的情况
            String formatSQL = formatSQL(sql.toString(), dbType);
            ret.append(formatSQL).append(enterChar);
        }

        return ret.toString();
    }

    private static String formatSQL(String sql, DBType dbType) throws Throwable{
        try {
//            HiveSqlParser parser = HiveSqlParser.parse(sql);
            ISQLParser parser = SQLParserFactory.getSQLParser(dbType, sql);
            String ret = parser.generateSQL(true);
            if (ret != null) {
                ret = ret.trim();
            }

            return ret;
        } catch (Throwable e) {
            return sql;
        }
    }

    private final static String COMMENT_REGEX = "^[\\s]*[-|#]{2}[\\s\\S]*$";
    private final static String COMMENT_START_REGEX = "^[\\s]*[/][\\*][\\s\\S]*$";
    private final static String COMMENT_END_REGEX = "^[\\s\\S]*[\\*][/][\\s]*$";
    private final static String SQL_END_REGEX = "^[\\s\\S]*[;]$";

    public static void main(String[] args) {
        String sql = "create table temp_dws_tfc_wx_access_itm_session_log_h\n" +
                "as\n" +
                "select\n" +
                "to_date(visit_tm) the_dt\n" +
                ",hour(visit_tm) the_hr\n" +
                ",count(1) pv\n" +
                ",sum(stay_time) visit_duration\n" +
                ",session_id\n" +
                ",uuid\n" +
                ",shop_session_id\n" +
                ",buyer_id\n" +
                ",shop_id\n" +
                ",seller_id\n" +
                ",sku_id\n" +
                "from dwd.dwd_tfc_wx_access_log\n" +
                "where dt = timeprocess('${date}',1)  and flag = 2\n" +
                "group by\n" +
                "to_date(visit_tm)\n" +
                ",hour(visit_tm)\n" +
                ",session_id\n" +
                ",uuid\n" +
                ",shop_session_id\n" +
                ",buyer_id\n" +
                ",shop_id\n" +
                ",seller_id\n" +
                ",sku_id";
        try {
//            System.out.println(SQLUtils.format(sql));
//            HiveSqlParser parser = HiveSqlParser.parse(sql);
            ISQLParser parser = SQLParserFactory.getSQLParser(DBType.Hive, sql);
            System.out.println(parser.generateSQL(true));
//            System.out.println(parser.sqlDump());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
