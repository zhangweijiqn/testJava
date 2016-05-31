package zwj.test.Utils.SQLUtils.sqlparser.base;

import zwj.test.Utils.SQLUtils.sqlparser.param.SQLParamAdaptor;

/**
 * SQL 上下文
 * Created by bjliuhongbin on 15-5-12.
 */
public class BaseSQLContext {
    private int indent = 0;
    private boolean shouldEnter = true;
    private boolean format = false;

//    private int limitNum = - 1;// limit行数
//    private boolean overWriteLimit;// 是否要覆盖Limit
//    private boolean hasLimit = false;// 原sql是否包含limit内容

    private SQLParamAdaptor paramAdaptor;// sql参数适配器

    /**
     * 构造函数
     * @param paramAdaptor 参数适配器，用于处理参数取值等问题
     */
    public BaseSQLContext(SQLParamAdaptor paramAdaptor) {
        this.paramAdaptor = paramAdaptor;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public boolean isShouldEnter() {
        return shouldEnter;
    }

    public void setShouldEnter(boolean shouldEnter) {
        this.shouldEnter = shouldEnter;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

//    public int getLimitNum() {
//        return limitNum;
//    }
//
//    public void setLimitNum(int limitNum) {
//        this.limitNum = limitNum;
//    }
//
//    public boolean isOverWriteLimit() {
//        return overWriteLimit;
//    }
//
//    public void setOverWriteLimit(boolean overWriteLimit) {
//        this.overWriteLimit = overWriteLimit;
//    }
//
//    public boolean isHasLimit() {
//        return hasLimit;
//    }
//
//    public void setHasLimit(boolean hasLimit) {
//        this.hasLimit = hasLimit;
//    }

    public SQLParamAdaptor getParamAdaptor() {
        return paramAdaptor;
    }

    public void setParamAdaptor(SQLParamAdaptor paramAdaptor) {
        this.paramAdaptor = paramAdaptor;
    }
}
