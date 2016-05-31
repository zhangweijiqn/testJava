package zwj.test.Utils.SQLUtils.sqlparser.hive.node;

import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.TableFinder;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.ConditionFiller;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.SQlGenerator;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator.AbstractSQLAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator.JoinSQLAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import org.antlr.runtime.Token;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.parse.ASTNode;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HiveASTNode extends ASTNode {

	private static final long serialVersionUID = 4228739510236472727L;

	public HiveASTNode(Token payload) {
		super(payload);
	}
	
	/**
	 * 注入自定义条件
	 * @param context 上下文
	 * @throws SQLException
	 */
	public void fillConditions(HiveSQLContext context) throws SQLException {
		if (context == null) {
			return ;
		}
		
		ConditionFiller filler = context.getConditionFiller();
		if (filler == null) {
			return ;
		}
		
		filler.fillConditions(context, this);
	}
	
	/**
	 * 查找表名
	 * @param context 上下文
	 * @return
	 * @throws SQLException
	 */
	public void findTables(HiveSQLContext context, List<Table> ret) throws SQLException {
		TableFinder finder = context.getTableFinder();
		if (finder == null) {
			return ;
		}
		
		finder.findTables(context, this, ret);
		
//		int childrenCnt = getChildCount();
//		for (int i = 0; i < childrenCnt; i++) {
//			HiveASTNode child = (HiveASTNode) getChild(i);
//
//			child.findTables(context, ret);
//		}
	}
	
	/**
	 * 将语法树节点转换成SQL
	 * @param context 上下文
	 * @return
	 * @throws SQLException
	 */
	public String toSQL(HiveSQLContext context) throws SQLException {
		SQlGenerator generator = context.getSqlGenerator();

        int type = getType();
        boolean format = context.isFormat();
        boolean shouldEnter = context.isShouldEnter();

        if (format) {
            if (type == HiveParser.TOK_UNION) {
                return formaBinary(context);
            } else if (type == HiveParser.TOK_LATERAL_VIEW || type == HiveParser.TOK_LATERAL_VIEW_OUTER) {
                StringBuilder ret = new StringBuilder();

                int indent = context.getIndent();
                StringBuilder indentStr = new StringBuilder();
                for (int i = 0; i < indent; i ++) {
                    indentStr.append(INDENT_CHAR);
                }

                List<Node> children = getChildren();
                for (int i = children.size() - 1; i >= 1; i --) {
                    HiveASTNode child = (HiveASTNode) children.get(i);
                    ret.append(child.toSQL(context)).append(" ");
                }

                String nodeName = AbstractSQLAdaptor.getRealName(false, this);
                ret.append(shouldEnter ? ENTER_CHAR + indentStr.toString() : "").append(nodeName).append(" ");
                HiveASTNode selExprNode = (HiveASTNode) getChild(0).getChild(0);
                List<Node> selExprChildren = selExprNode.getChildren();
                int selExprChildrenCnt = selExprChildren.size();
                HiveASTNode functionNode = (HiveASTNode) selExprChildren.get(0);
                ret.append(functionNode.toSQL(context)).append(" ");
                HiveASTNode tabAliasNode = (HiveASTNode) selExprChildren.get(selExprChildrenCnt - 1);
                ret.append(tabAliasNode.toSQL(context)).append(" ");
                if (selExprChildrenCnt > 2) {
                    ret.append("as ");
                    for (int i = 1; i < selExprChildrenCnt - 1; i ++) {
                        HiveASTNode child = (HiveASTNode) selExprChildren.get(i);
                        if (i > 1) {
                            ret.append(",");
                        }
                        ret.append(child.toSQL(context));
                    }
                }

                return ret.toString();
            } else if (FORMAT_TOKEN_LIST.contains(type)) {
                int indent = context.getIndent();
                StringBuilder indentStr = new StringBuilder();
                for (int i = 0; i < indent; i ++) {
                    indentStr.append(INDENT_CHAR);
                }

                // 缩进增加
                context.setIndent(indent + 1);

                String ret = generator.generatSQL(context, this);

                // 缩进恢复
                context.setIndent(indent);

                ret = (shouldEnter ? ENTER_CHAR + indentStr.toString() : "") + ret;

                return ret;
            } else if (new JoinSQLAdaptor().acceptTypes().contains(type)) {
                StringBuffer ret = new StringBuffer();

                int indent = context.getIndent();
                StringBuilder indentStr = new StringBuilder();
                for (int i = 0; i < indent; i ++) {
                    indentStr.append(INDENT_CHAR);
                }

                int cnt = getChildCount();
                if (cnt >= 2) {
                    String joinTag = AbstractSQLAdaptor.getRealName(false, this);
                    HiveASTNode joinNode = (HiveASTNode) getChild(0);
                    ret.append(joinNode.toSQL(context));

                    HiveASTNode onTableNode = (HiveASTNode) getChild(1);
                    context.setIndent(indent + 1);
//                    context.setShouldEnter(false);
                    ret.append(ENTER_CHAR).append(indentStr.toString()).append(joinTag).append(" ").append(onTableNode.toSQL(context));
//                    context.setShouldEnter(true);
                    context.setIndent(indent);

                    if (cnt == 3) {
                        HiveASTNode onCondition = (HiveASTNode) getChild(2);
                        context.setIndent(indent + 1);
                        ret.append(ENTER_CHAR).append(indentStr).append(INDENT_CHAR).append(" ON (").append(onCondition.toSQL(context)).append(")");
                        context.setIndent(indent);
                    }

                } else {
                    throw new SQLException("join statement syntax error");
                }

                return ret.toString();
            }
	    }

        return generator.generatSQL(context, this);
   }


    private String formaBinary(HiveSQLContext context) throws SQLException {
        SQlGenerator generator = context.getSqlGenerator();
        if (getChildCount() != 2) {
            return generator.generatSQL(context, this);
        }

        String realName = AbstractSQLAdaptor.getRealName(false, this);
        boolean format = context.isFormat();
        if (format) {
            int indent = context.getIndent();
            StringBuilder indentStr = new StringBuilder();
            for (int i = 0; i < indent; i ++) {
                indentStr.append(INDENT_CHAR);
            }

            realName = ENTER_CHAR + indentStr.toString() + realName;
        }

        HiveASTNode leftNode = (HiveASTNode) getChild(0);
        HiveASTNode rightNode = (HiveASTNode) getChild(1);

        StringBuilder left = new StringBuilder();
        boolean leftNeedParenthesis = AbstractSQLAdaptor.comparePriority(this, leftNode) > 0 ;
        if (leftNeedParenthesis) {
            left.append("(");
            left.append(leftNode.toSQL(context));
            left.append(")");
        } else {
            left.append(leftNode.toSQL(context));
        }

        String right = rightNode.toSQL(context);

        return left + realName + right;
    }

    private final static Set<Integer> FORMAT_TOKEN_LIST = new HashSet<Integer>() ;
    static {
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_EXPLAIN);

        FORMAT_TOKEN_LIST.add(HiveParser.TOK_DESTINATION);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_INSERT_INTO);

        FORMAT_TOKEN_LIST.add(HiveParser.TOK_FROM);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_SELECT);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_SELECTDI);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_WHERE);

        FORMAT_TOKEN_LIST.add(HiveParser.TOK_SELEXPR);

        FORMAT_TOKEN_LIST.add(HiveParser.TOK_TABREF);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_TAB);

        FORMAT_TOKEN_LIST.add(HiveParser.TOK_SORTBY);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_CLUSTERBY);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_ORDERBY);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_DISTRIBUTEBY);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_HAVING);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_GROUPBY);

        FORMAT_TOKEN_LIST.add(HiveParser.TOK_LIMIT);

        FORMAT_TOKEN_LIST.add(HiveParser.TOK_CREATETABLE);
        FORMAT_TOKEN_LIST.add(HiveParser.TOK_LIKETABLE);

//        FORMAT_TOKEN_LIST.add(HiveParser.TOK_JOIN);
//        FORMAT_TOKEN_LIST.add(HiveParser.TOK_LEFTOUTERJOIN);
//        FORMAT_TOKEN_LIST.add(HiveParser.TOK_RIGHTOUTERJOIN);
//        FORMAT_TOKEN_LIST.add(HiveParser.TOK_FULLOUTERJOIN);
//        FORMAT_TOKEN_LIST.add(HiveParser.TOK_LEFTSEMIJOIN);
//        FORMAT_TOKEN_LIST.add(HiveParser.TOK_CROSSJOIN);

//        BINARYOPERATOR_TYPES.add(HiveParser.KW_AND);// AND
//        BINARYOPERATOR_TYPES.add(HiveParser.KW_OR);// OR

//        FORMAT_TOKEN_LIST.add(HiveParser.TOK_USER);
//        FORMAT_TOKEN_LIST.add(HiveParser.TOK_ROLE);
//        CLAUSE_NEEDCHILDREN_TYPES.add(HiveParser.TOK_DIR);
//        ACCEPT_TYPES.add(HiveParser.TOK_PARTSPEC);
//        ACCEPT_TYPES.add(HiveParser.TOK_PARTVAL);


    }

	private final static String INDENT_CHAR = "\t";
    private final static String ENTER_CHAR = System.getProperty("line.separator");

}
