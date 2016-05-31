package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.condition;

import zwj.test.Utils.SQLUtils.DBType;
import zwj.test.Utils.SQLUtils.sqlparser.ISQLParser;
import zwj.test.Utils.SQLUtils.sqlparser.SQLParserFactory;
import zwj.test.Utils.SQLUtils.sqlparser.beans.JoinCondition;
import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.HiveSqlParser;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.HiveSqlParseAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import org.antlr.runtime.tree.Tree;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.List;

/**
 * TOK_TABREF条件下的自定义条件注入
 * <p>要处理的场景有:没有join也没有子查询的场景
 * <p>
 * @author bjliuhongbin
 *
 */
public class QueryConditionAdaptor extends AbstractConditionAdaptor {

	@Override
	public void fillConditions(HiveSQLContext context, HiveASTNode queryNode,
			ConditionProvider provider) throws SQLException {
		
//		Tree select = getSelect(queryNode);
		
		Tree from = getFrom(queryNode);
		Tree fromChild = from.getChild(0);
		fillConditions(fromChild, provider);
	}
	
	private void fillConditions(Tree node, ConditionProvider provider) throws SQLException {
		int type = node.getType();
		switch (type) {
		case HiveParser.TOK_TABREF:
			fillConditionsAsTableRef(node, provider);
			break;
		case HiveParser.TOK_JOIN:
		case HiveParser.TOK_LEFTOUTERJOIN:
		case HiveParser.TOK_RIGHTOUTERJOIN:
		case HiveParser.TOK_FULLOUTERJOIN:
		case HiveParser.TOK_LEFTSEMIJOIN:
		case HiveParser.TOK_CROSSJOIN:
			fillConditionAsJoin(node, provider);
			break;
		default:
			break;
		}
	}
	
	/*
	 * 向tabref子句中注入条件
	 */
	private void fillConditionsAsTableRef(Tree tableref, ConditionProvider provider) throws SQLException {
		if (provider == null) {
			return ;
		}
		
		Table table = getTableFromTabrefNode(tableref);
		JoinCondition condition = provider.getCondition(table);
		if (condition == null) {
			return ;
		}
		
		HiveSqlParseAdaptor parserAdaptor = new HiveSqlParseAdaptor();
		Tree subSelect = constructNode(table, condition);
		Tree subQuery = (Tree) parserAdaptor.create(HiveParser.TOK_SUBQUERY, "TOK_SUBQUERY");
		parserAdaptor.becomeRoot(subQuery, subSelect);
		
		String alias = table.getAlias();
		if (StringUtils.isEmpty(alias)) {
			alias = table.getName();
		}
		Tree aliasNode = (Tree) parserAdaptor.create(HiveParser.Identifier, alias);
		parserAdaptor.becomeRoot(subQuery, aliasNode);
		
		Tree parent = tableref.getParent();
		int index = tableref.getChildIndex();
		parent.setChild(index, subQuery);
	}
	
	/*
	 * 向join子句中注入条件
	 */
	private void fillConditionAsJoin(Tree join, ConditionProvider provider) throws SQLException {
		Tree mainTable = join.getChild(0);
		Tree joinTable = join.getChild(1);
		
		fillConditions(mainTable, provider);
		fillConditions(joinTable, provider);
	}
	
	/*
	 * 根据给定的条件构建查询语法树
	 */
	private HiveASTNode constructNode(Table table, JoinCondition condition) throws SQLException {
//		String sql = "select t1.* from seller join t1 on seller.sid= t1.sid";
		
		StringBuffer sql = new StringBuffer();
		sql.append(generateSelectSQL(table, condition)).append(" ");
		sql.append(generateFromSQL(condition)).append(" ");
		sql.append(generateJoinSQL(table, condition));
		
//		HiveSqlParser parser = HiveSqlParser.parse(sql.toString());
        ISQLParser parser = SQLParserFactory.getSQLParser(DBType.Hive, sql.toString());
		return ((HiveSqlParser) parser).getRoot();
	}
	
	/*
	 * 由condition构造from子句
	 * @return
	 */
	private String generateFromSQL(JoinCondition condition) {
		StringBuffer ret = new StringBuffer();
		ret.append(" from ").append(condition.getJoinTable().getWholeName());
		
		return ret.toString();
	}
	
	/*
	 *  由condition构造join子句
	 */
	private String generateJoinSQL(Table table, JoinCondition condition) {
		StringBuffer ret = new StringBuffer();
		
		ret.append("join ");
		ret.append(table.getWholeName());
		String alias = table.getAlias();
		if (!StringUtils.isEmpty(alias)) {
			ret.append(" ").append(alias);
		} else {
			alias = table.getName();
		}
		ret.append(" on ");
		
		ret.append(condition.getJoinTable().getName()).append("lib").append(condition.getJoinField());
		ret.append("=");
		ret.append(alias).append("lib").append(condition.getMainField());
		
		List<String> filterExps = condition.getFilterExps();
		for (String filterExp : filterExps) {
			if (StringUtils.isEmpty(filterExp)) {
				continue;
			}
			
			ret.append(" AND ").append(filterExp);
		}
		
		return ret.toString();
	}
	
	/*
	 *  由condition构造select子句
	 */
	private String generateSelectSQL(Table table, JoinCondition condition) throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		ret.append("select ");
		
		String alias = table.getAlias();
		if (StringUtils.isEmpty(alias)) {
			alias = table.getName();
		}
		
		ret.append(alias).append(".*");

		return ret.toString();
	}
	
	/*
	 * 从tabref节点中读取table信息
	 */
	private Table getTableFromTabrefNode(Tree tableref) {
		Tree tableNameNode = tableref.getChild(0);
		Table table = new Table();
		
		if (tableNameNode.getChildCount() == 2) {
			table.setSchemaName(tableNameNode.getChild(0).getText());
			table.setName(tableNameNode.getChild(1).getText());
		} else {
			table.setName(tableNameNode.getChild(0).getText());
		}
		
		if (tableref.getChildCount() == 2) {
			Tree tableAliasNode = tableref.getChild(1);
			table.setAlias(tableAliasNode.getText());
		}
	
		return table;
	}
	
	/*
	 * 获取from节点
	 * @return
	 */
	private Tree getFrom(HiveASTNode queryNode) {
		return queryNode.getChild(0);
	}
	
	@Override
	public Integer[] acceptTypes() {
		return new Integer[] {
				HiveParser.TOK_QUERY
		};
	}
	
}
