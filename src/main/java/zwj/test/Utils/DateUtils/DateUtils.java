package zwj.test.Utils.DateUtils;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {

    private Log logger = LogFactory.getLog(getClass());
    public final static String DEFAULT_PATTERN = "yyyy/MM/dd HH:mm:ss";
    public final static String DEFAULT_PATTERN_T = "yyyy-MM-dd HH:mm:ss";
    public final static String DEFAULT_PATTERN_N = "yyyyMMddHHmmss";
    public final static String NOPARSE="not parse!";
    public final static String[] keyword = new String[]{};
    public static Date getDate(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);

        return calendar.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day);

        return calendar.getTime();
    }

    public static String format(Date date) {
        return format(date, DEFAULT_PATTERN);
    }

    public static String formatN(Date date) {
        return format(date, DEFAULT_PATTERN_N);
    }

    public static String format(Date date, String pattern) {
        if (date == null) return "";

        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }

    public static Date parse(String date) {
        return parse(date, DEFAULT_PATTERN);
    }
    public static Date parseT(String date) {
        return parse(date, DEFAULT_PATTERN_T);
    }
    public static Date parse(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        try {
            return format.parse(date);
        } catch (Exception e) {
            return null;
        }
    }
/**
 *
 *
 */
public static String handleSqlDate(String sqlContent) {
    return handleSqlDate(sqlContent, "");
}

    public static String handleSqlDate(String sqlContent, String setDate) {
        String sql = DateUtils.handleSql4Date(sqlContent, "${", "}", setDate);

        return sql;
    }

    private static String handleSql4Date(String sql,String begin,String end, String setDate) {
        String[] part = org.apache.commons.lang.StringUtils.substringsBetween(sql, begin, end);

        if(part==null||part.length==0){
            return sql;
        }
        Map<String, String> distinct = new HashMap<String,String>();

        for (int i = 0; i < part.length; i++) {
            if (!distinct.containsKey(part[i])) {
                distinct.put(part[i], "1");
            }
        }
        for (String key:distinct.keySet()) {
            distinct.put(key, handleDate(key,begin,end, setDate));
        }

        String sqlnew = sql ;

        for (String key:distinct.keySet()) {
            if(distinct.get(key).equals(NOPARSE)){
//                sqlnew = org.apache.commons.lang3.StringUtils.replace(sqlnew, begin + key + end,  distinct.get(key) );
            }else{
                sqlnew = org.apache.commons.lang.StringUtils.replace(sqlnew, "'" + begin + key + end + "'", "'" + distinct.get(key) + "'");
                sqlnew = org.apache.commons.lang.StringUtils.replace(sqlnew, begin + key + end, "'" + distinct.get(key) + "'");
            }
        }
        return sqlnew;
    }

    private static String handleDate(String s,String begin,String end) {
        return handleDate(s, begin, end, "");
    }

    private static String handleDate(String s,String begin,String end, String setDate) {
        int diffPeriod = 0;
        Date date = new Date();

        if (!org.apache.commons.lang.StringUtils.isEmpty(setDate)) {
            int len = setDate.length();
            String append = org.apache.commons.lang.StringUtils.repeat("0", 14 - len);
            date = DateUtils.parse(setDate+append, "yyyyMMddHHmmss");
        }
        int diff = 0;
        String result = "";
        String[] vals = new String[3];
        String temp = s;
        String[] arr = org.apache.commons.lang.StringUtils.split(temp, ",");
        if (arr.length == 3) {
            for (int j = 0; j < arr.length; j++) {
                vals[j] = arr[j];
            }
        } else if (arr.length == 2) {
            if(NumberUtils.isNumber(arr[1])){
                for (int j = 0; j < arr.length; j++) {
                    vals[j] = arr[j];
                }
                vals[2] = "yyyyMMdd";
            }else{
                vals[0] = arr[0];
                vals[1] = "0";
                vals[2] = arr[1];
            }
        }else{
            vals[0] = arr[0];
            vals[1] = "0";
            vals[2] = "yyyyMMdd";
        }

        String key = vals[0].trim().toLowerCase();
        String offset = vals[1].trim();
        String format = vals[2].trim();

        if (NumberUtils.isNumber(offset)) {
            diff = NumberUtils.toInt(offset);
        }


        if ("date_dt".equals(key)||"SYS_DATE".equals(key.toUpperCase())) {
            String value = getDateStr(changeDay(date, diff - diffPeriod), format);
            return value;
        } else if ("date_wk".equals(key)) {
            String value = getDateStr(changeWeek(date, diff - diffPeriod), format);
            return value;
        }else{
//            System.out.println("//"+begin+s+end);
            return NOPARSE;
        }
//        return "";
    }
    public static String getDateStr(Date date, String formatStr) {
        if (org.apache.commons.lang.StringUtils.isBlank(formatStr)) {
            formatStr = DEFAULT_PATTERN;
        }
        SimpleDateFormat dateformat1 = new SimpleDateFormat(formatStr);
        return dateformat1.format(date);

    }
    public static Date changeWeek(Date d, int week) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.WEEK_OF_YEAR, week);
        return calendar.getTime();
    }

    public static Date changeDay(Date d, int day) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        calendar.add(calendar.DATE, day);
        return calendar.getTime();
    }
}
