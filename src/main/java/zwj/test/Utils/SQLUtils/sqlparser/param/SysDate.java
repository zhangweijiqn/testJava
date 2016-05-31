package zwj.test.Utils.SQLUtils.sqlparser.param;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.utils.SQLDateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * 内置SQL变量
 * Created by bjliuhongbin on 14-8-4.
 */
public class SysDate extends AbstractParam {

    public SysDate() {
        super("SYS_DATE");
    }

    @Override
    public Object getValue(String[] params) throws SQLException {
        Calendar cToday = Calendar.getInstance();
        Date today = cToday.getTime();

        int offset = Integer.valueOf(params[0]);

        return offset(today, offset);
    }

    @Override
    public String parse2SQL(String[] params) throws SQLException {
        Date value = (Date) getValue(params);
        String pattern = params[1];

        return "'" + SQLDateFormat.format(value, pattern) + "'";
    }

    @Override
    public void check(String[] params) throws SQLException {
    }

    private Date offset(Date date, int offset) {
        long old = date.getTime();
        old += offset * 1000 * 60 * 60 * 24;

        return new Date(old);
    }

//    public static void main(String[] args) {
//        String exp = "YESTERDAY, 1, yyyyMMdd";
//        String[] tokens = parseParams(exp);
//
//        int offset = Integer.valueOf(tokens[1]);
//
//        System.out.println();
//
//    }
}
