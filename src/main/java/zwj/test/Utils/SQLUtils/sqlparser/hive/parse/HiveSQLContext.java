package zwj.test.Utils.SQLUtils.sqlparser.hive.parse;

import zwj.test.Utils.SQLUtils.sqlparser.beans.JoinCondition;
import zwj.test.Utils.SQLUtils.sqlparser.hive.TableFinder;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.ConditionFiller;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.SQlGenerator;
import zwj.test.Utils.SQLUtils.sqlparser.param.SQLParamAdaptor;

/**
 * sql语句处理时的上下文
 * @author bjliuhongbin
 *
 */
public class HiveSQLContext {
	private TableFinder tableFinder = new TableFinder();
	private SQlGenerator sqlGenerator = new SQlGenerator();
	private ConditionFiller filler = new ConditionFiller();
	
	private SQLParamAdaptor paramAdaptor;// sql参数适配器

    private int limitNum = - 1;// limit行数
    private boolean overWriteLimit;// 是否要覆盖Limit
    private boolean hasLimit = false;// 原sql是否包含limit内容

    private int indent = 0;
    private boolean shouldEnter = true;
    private boolean format = false;
	
	/**
	 * 构造函数
	 * @param paramAdaptor 参数适配器，用于处理参数取值等问题
	 */
	public HiveSQLContext(SQLParamAdaptor paramAdaptor) {
		this.paramAdaptor = paramAdaptor;
	}

	/**
	 * 参数适配器，用于处理参数取值等问题
	 * @return
	 */
	public SQLParamAdaptor getParamAdaptor() {
		return paramAdaptor;
	}

	/**
	 * 表名查找器
	 * @return
	 */
	public TableFinder getTableFinder() {
		return tableFinder;
	}

	/**
	 * 设置表名查找器
	 * @return
	 */
	public void setTableFinder(TableFinder tableFinder) {
		this.tableFinder = tableFinder;
	}
	
	/**
	 * 添加自定义条件
	 * @param conditions 自定义条件数组
	 */
	public void addConditions(JoinCondition...conditions) {
		if (conditions == null) {
			return ;
		}
		
		for (JoinCondition joinCondition : conditions) {
			this.filler.getConditions().add(joinCondition);
		}
	}
	
	/**
	 * 返回sql生成器
	 * @return
	 */
	public SQlGenerator getSqlGenerator() {
		return sqlGenerator;
	}
	
	/**
	 * 返回自定义条件注入器
	 * @return
	 */
	public ConditionFiller getConditionFiller() {
		return filler;
	}

    public int getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(int limitNum) {
        this.limitNum = limitNum;
    }

    public boolean isOverWriteLimit() {
        return overWriteLimit;
    }

    public void setOverWriteLimit(boolean overWriteLimit) {
        this.overWriteLimit = overWriteLimit;
    }

    public boolean isHasLimit() {
        return hasLimit;
    }

    public void setHasLimit(boolean hasLimit) {
        this.hasLimit = hasLimit;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public boolean isFormat() {
        return format;
    }

    public void setFormat(boolean format) {
        this.format = format;
    }

    public boolean isShouldEnter() {
        return shouldEnter;
    }

    public void setShouldEnter(boolean shouldEnter) {
        this.shouldEnter = shouldEnter;
    }
}
