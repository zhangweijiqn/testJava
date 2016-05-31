package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;

/**
 * 默认的SQL处理，用于以根节点为开头和结尾，以直接子节点顺序排列的场景, 例如from子句
 * @author bjliuhongbin
 *
 */
public abstract class AbstractDefaultSQLAdaptor extends AbstractSQLAdaptor {

	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode _node)
			throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		boolean nameBefore = nameAsPrefix();
		boolean childrenEmptyAble = canChildrenEmpty();
		boolean childrenWithParenthsis = childrenWithParenthsis();
		
		String preparedName = prepareName(_node);
		String preparedChildren = prepareChildren(context, _node);
		
		if (childrenEmptyAble || preparedChildren.length() > 0) {
			ret.append(preparedChildren);
			
			if (childrenWithParenthsis) {
				ret.insert(0, "(").append(")");
			}
			
			if (nameBefore) {
				ret.insert(0, preparedName);
			} else {
				ret.append(preparedName);
			}
		}
		
		return ret.toString();
	}
	
	private String prepareName(HiveASTNode _node) {
		boolean nameBefore = nameAsPrefix();
		
		StringBuffer nameBuf = new StringBuffer();
		int cnt = _node.getChildCount();
		String name = getRealName(true, _node);
		if (!StringUtils.isEmpty(name)) {
			nameBuf.append(name);
			if (cnt > 0) {
				if (nameBefore) {
					nameBuf.append(" ");
				} else {
					nameBuf.insert(0, " ");
				}
			}
		}
		
		return nameBuf.toString();
	}
	
	private String prepareChildren(HiveSQLContext context, HiveASTNode _node) throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		String childSeparator = childrenSeparator();
		
		int cnt = _node.getChildCount();
		for (int i = 0; i < cnt; i++) {
			HiveASTNode node = (HiveASTNode) _node.getChild(i);
			String nodeSQL = node.toSQL(context);
			if (StringUtils.isEmpty(nodeSQL)) {
				continue;
			}
			if (ret.length() > 0) {
				ret.append(childSeparator);
			} else {
				ret.append("");
			}
			ret.append(nodeSQL);
		}
		
		return ret.toString();
	}

	/**
	 * true表示根节点的名称作为前缀，否则作为后缀
	 * @return
	 */
	protected abstract boolean nameAsPrefix();
	
	/**
	 * 子节点是否可以为空
	 * @return
	 */
	protected abstract boolean canChildrenEmpty();
	
	/**
	 * 子节点间的分隔符
	 * @return
	 */
	protected abstract String childrenSeparator();
	
	/**
	 * true表示需要为子节点追加括号
	 * @return
	 */
	protected abstract boolean childrenWithParenthsis() ;
}
