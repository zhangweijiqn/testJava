package zwj.test.Utils.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by zhangwj on 16-1-14.
 */
public class testDateUtils {
    private static Date createTime;//定义时间参数
    public static void setCreateTime(Date date){
        createTime = date;
    }

    public static String getCreateTimeAsString() {
        return DateUtils.format(createTime);//存入数据库以字符串的形式，数据库    createTime ，varchar(50)
    }

    public static void setDateFromString(String dateAsString){
        createTime = DateUtils.parse(dateAsString);
    }

    public static String getFirstDayOfMonth()
    {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM01");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth ;
    }
    public static String getFirstDayofWeek(){
        Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); //获取本周一的日期
        System.out.println(df.format(cal.getTime()));
        return df.format(cal.getTime());
    }

    public static String getDay(){
        Calendar cal =Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        System.out.println(df.format(cal.getTime()));
        return df.format(cal.getTime());
    }

    public static String getYesterdayDay(){
        Date date=new Date();//取时间
        Calendar cal =Calendar.getInstance();
        cal.setTime(date);
        cal.add(cal.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
        date=cal.getTime(); //这个时间就是日期往前推一天的结果
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        System.out.println(df.format(date));
        return df.format(date);
    }

    //获取时间戳
    public static Long getDateLong(){
        //        String date = "1472474574643";
        Date date = new Date();
        // 返回自 1970 年 1 月 1 日 00:00:00 GMT 以来此 Date 对象表示的毫秒数。
        long time = date.getTime();
        System.out.println(time);
        return time;
    }

    public static void main(String[] args) {
        System.out.println("current:"+getDay());
        System.out.println(getFirstDayOfMonth());

        String a="dd";
        String v="vv";
        String tt="44";
        tt+=a;
        System.out.println(tt);


        getYesterdayDay();
        //获取当前月第一天：
      /*  System.out.println(getFirstDayofWeek());

        System.out.println(new Date().getTime());
        String timestamp = Long.toString(new Date().getTime());
        System.out.println(timestamp);
        Date date = Calendar.getInstance().getTime();
        System.out.println(date);//Fri Jan 29 17:07:53 CST 2016
        setCreateTime(date);
        String dateAsString = getCreateTimeAsString();
        System.out.println(dateAsString);

        setDateFromString(dateAsString);
        System.out.println(getCreateTimeAsString());*/

    }

}
