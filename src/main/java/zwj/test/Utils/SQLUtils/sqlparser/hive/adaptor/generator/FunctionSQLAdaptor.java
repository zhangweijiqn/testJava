package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import org.antlr.runtime.tree.Tree;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户函数sql的生成
 * @author bjliuhongbin
 *
 */
public class FunctionSQLAdaptor extends AbstractSQLAdaptor {

	@SuppressWarnings("deprecation")
	@Override
	public String generatSQL(HiveSQLContext context, HiveASTNode node)
			throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		int cnt = node.getChildCount();
		if (cnt < 1) {
			throw new SQLException(" function sytax error");
		}
		
		Tree functionNameNode = node.getChild(0);
        int nameNodeType = functionNameNode.getType();
        if (nameNodeType == HiveParser.KW_CASE) {
            return case2SQL(context, node);
        } else if (nameNodeType == HiveParser.KW_WHEN) {
            return when2SQL(context, node);
        }  else if (nameNodeType == HiveParser.KW_IF) {
            return if2SQL(context, node);
        } else if (castTypes.contains(nameNodeType)) {
            String realFunctionName = getRealName(false, (HiveASTNode) functionNameNode);
            return "CAST(" + ((HiveASTNode) node.getChild(1)).toSQL(context) + " AS " + realFunctionName + ") ";
        } else {
            String functionName = ((HiveASTNode) functionNameNode).toSQL(context);

            if (!StringUtils.isEmpty(functionName)
                    && functionName.trim().equalsIgnoreCase("between")) {
                return between2SQL(context, node);
            }


            if (functionNameNode.getType() ==  HiveParser.TOK_ISNULL
                    || functionNameNode.getType() ==  HiveParser.TOK_ISNOTNULL) {
                return isnull2SQL(context, node);
            }

            if (functionNameNode.getType() ==  HiveParser.KW_IN) {
                return in2SQL(context, node);
            }

            if (cnt == 1) {
                ret.append(functionName).append("()");
            } else {
                if (node.getType() == HiveParser.TOK_FUNCTIONDI) {
                    ret.append(functionName).append("(DISTINCT ");
//                functionName = "DISTINCT " + functionName;
                } else if ("row_number".equalsIgnoreCase(functionNameNode.getText())) {
                    ret.append("row_number() over ").append("(");
                } else {
                    ret.append(functionName).append("(");
                }

                for (int i = 1; i < cnt; i++) {
                    ret.append(((HiveASTNode) node.getChild(i)).toSQL(context));
                    if (i == cnt -1) {
                        ret.append(")");
                    } else {
                        ret.append(",");
                    }
                }
            }

            return ret.toString();
        }
	}
	
	@Override
	public List<Integer> acceptTypes() {
		return accept_types;
	}

    private String case2SQL(HiveSQLContext context, HiveASTNode node) {
        StringBuilder ret = new StringBuilder();

        Tree functionNameNode = node.getChild(0);
        String functionName = functionNameNode.getText();
        ret.append(functionName);
        ret.append(" ").append(((HiveASTNode) node.getChild(1)).toSQL(context));

        for (int i = 2; i < node.getChildCount(); i++ ) {
            HiveASTNode child = (HiveASTNode) node.getChild(i);
            if (child.getType() == HiveParser.TOK_TABLE_OR_COL) {
                ret.append(" WHEN ").append(child.toSQL(context));
            } else if (i % 2 == 0){
                ret.append(" ELSE ").append(child.toSQL(context));
            } else {
                ret.append(" THEN ").append(child.toSQL(context));
            }
        }
        ret.append(" end ");

        return ret.toString();
    }

    private String when2SQL(HiveSQLContext context, HiveASTNode node) {
        StringBuilder ret = new StringBuilder();

        ret.append("CASE ");
        for (int i = 1; i < node.getChildCount(); i++ ) {
            HiveASTNode child = (HiveASTNode) node.getChild(i);

            if (i % 2 == 0) {
                ret.append(" THEN ").append(child.toSQL(context));
            } else if (i == node.getChildCount() - 1) {
                ret.append(" else ").append(child.toSQL(context));
            } else {
                ret.append(" WHEN ").append(child.toSQL(context));
            }
//            if (child.getType() == HiveParser.TOK_TABLE_OR_COL) {
//                ret.append(" WHEN ").append(child.toSQL(context));
//            } else if (i % 2 == 0){
//                ret.append(" ELSE ").append(child.toSQL(context));
//            } else {
//                ret.append(" THEN ").append(child.toSQL(context));
//            }
        }
        ret.append(" end ");

        return ret.toString();
    }

    private String if2SQL(HiveSQLContext context, HiveASTNode node) {
        StringBuilder ret = new StringBuilder();

        Tree functionNameNode = node.getChild(0);
        String functionName = functionNameNode.getText();
        ret.append(functionName);
        ret.append("(");

        for (int i = 1; i < node.getChildCount(); i ++) {
            if (i > 1) {
                ret.append(",");
            }

            ret.append(((HiveASTNode) node.getChild(i)).toSQL(context));
        }
        ret.append(")");

        return ret.toString();
    }
	
	private String between2SQL(HiveSQLContext context, HiveASTNode node) throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		String functionName = ((HiveASTNode) node.getChild(0)).toSQL(context);
		HiveASTNode booleanNode = (HiveASTNode) node.getChild(1);
		String target =  ((HiveASTNode) node.getChild(2)).toSQL(context);
		String from =  ((HiveASTNode) node.getChild(3)).toSQL(context);
		String to =  ((HiveASTNode) node.getChild(4)).toSQL(context);
	
		ret.append(target);
		if (booleanNode.getType() == HiveParser.KW_TRUE) {
			ret.append(" NOT");
		}
		ret.append(" ").append(functionName).append(" ");
		ret.append(from);
		ret.append(" AND ");
		ret.append(to);
		
		return ret.toString();
	}
	
	private String isnull2SQL(HiveSQLContext context, HiveASTNode node) throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		String functionName = ((HiveASTNode) node.getChild(0)).toSQL(context);
		String target =  ((HiveASTNode) node.getChild(1)).toSQL(context);
	
		ret.append(target);
		ret.append(" ").append(functionName);
		
		return ret.toString();
	}
	
	private String in2SQL(HiveSQLContext context, HiveASTNode node) throws SQLException {
		StringBuffer ret = new StringBuffer();
		
		int cnt = node.getChildCount();
		String functionName = getRealName(false, (HiveASTNode) node.getChild(0));
		String target =  ((HiveASTNode) node.getChild(1)).toSQL(context);
	
		ret.append(target);
		ret.append(" ").append(functionName).append("(");
		
		StringBuffer tmp = new StringBuffer();
		for (int i = 2; i < cnt; i++) {
			if (tmp.length() > 0) {
				tmp.append(", ");
			}
			String nodeSQL = ((HiveASTNode) node.getChild(i)).toSQL(context);
			tmp.append(nodeSQL);
		}
		ret.append(tmp);
		
		ret.append(")");
		
		return ret.toString();
	}

	private static List<Integer> accept_types = new ArrayList<Integer>();
	static {
		accept_types.add(HiveParser.TOK_FUNCTIONDI);
		accept_types.add(HiveParser.TOK_FUNCTION);
	}

    private static Set<Integer> castTypes = new HashSet<Integer>();
    static {
        castTypes.add(HiveParser.TOK_TINYINT);
        castTypes.add(HiveParser.TOK_SMALLINT);
        castTypes.add(HiveParser.TOK_INT);
        castTypes.add(HiveParser.TOK_BIGINT);
        castTypes.add(HiveParser.TOK_BOOLEAN);
        castTypes.add(HiveParser.TOK_FLOAT);
        castTypes.add(HiveParser.TOK_DOUBLE);
        castTypes.add(HiveParser.TOK_STRING);
        castTypes.add(HiveParser.TOK_BINARY);
        castTypes.add(HiveParser.TOK_TIMESTAMP);
        castTypes.add(HiveParser.TOK_DECIMAL);
        castTypes.add(HiveParser.TOK_DATE);
        castTypes.add(HiveParser.TOK_VARCHAR);

    }

}
