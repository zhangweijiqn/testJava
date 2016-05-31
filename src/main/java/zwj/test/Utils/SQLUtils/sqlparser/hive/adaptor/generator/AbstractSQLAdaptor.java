package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL转换器
 * @author bjliuhongbin
 *
 */
public abstract class AbstractSQLAdaptor {

	/**
	 * 将某节点转换成SQL
	 * @param context 上下文
	 * @param node 要转换的节点
	 * @return
	 * @throws SQLException
	 */
	public abstract String generatSQL(HiveSQLContext context, HiveASTNode node) throws SQLException;

	/**
	 * 当前可以接受的节点类型列表
	 * @return
	 * @throws SQLException
	 */
	public abstract List<Integer> acceptTypes() ;
	
	/*
	 * 比较优先级
	 * @return
	 */
	public static int comparePriority(HiveASTNode node1, HiveASTNode node2) {
		Integer priority1 = PRIORITY_MAP.get(node1.getType());
		if (priority1 == null) {
			priority1 = Integer.MAX_VALUE;
		}
		Integer priority2 = PRIORITY_MAP.get(node2.getType());
		if (priority2 == null) {
			priority2 = Integer.MAX_VALUE;
		}
		
		if (priority1 > priority2) {
			return 1;
		} else if (priority1 < priority2) {
			return -1;
		} else {
			return 0;
		}
	}
	
	/**
	 * SQL中显示的名称
	 */
	public static String getRealName(boolean nullable, HiveASTNode node) {
		String text = node.getText();
		String name = xlateMap.get(text);
		if (StringUtils.isEmpty(name) && !nullable) {
			name = text;
		}
		
		return name;
	}
	
	/**
	 * 运算优先级
	 */
	private static Map<Integer, Integer> PRIORITY_MAP = new HashMap<Integer, Integer>();
	static {
		/* 算术运算符  0x000A-- */
		PRIORITY_MAP.put(HiveParser.TILDE, 0x000AA0);
		PRIORITY_MAP.put(HiveParser.STAR, 0x000A90);
		PRIORITY_MAP.put(HiveParser.DIVIDE, 0x000A90);
		PRIORITY_MAP.put(HiveParser.MOD, 0x000A90);
		PRIORITY_MAP.put(HiveParser.PLUS, 0x000A80);
		PRIORITY_MAP.put(HiveParser.MINUS, 0x000A80);
		PRIORITY_MAP.put(HiveParser.AMPERSAND, 0x000A80);
		PRIORITY_MAP.put(HiveParser.BITWISEOR, 0x000A80);
		PRIORITY_MAP.put(HiveParser.BITWISEXOR, 0x000A80);
		
		/* 算术运算符 0x0009-- */
		PRIORITY_MAP.put(HiveParser.EQUAL, 0x000990);
		PRIORITY_MAP.put(HiveParser.EQUAL_NS, 0x000990);
		PRIORITY_MAP.put(HiveParser.NOTEQUAL, 0x000990);
		PRIORITY_MAP.put(HiveParser.LESSTHAN, 0x000990);
		PRIORITY_MAP.put(HiveParser.LESSTHANOREQUALTO, 0x000990);
		PRIORITY_MAP.put(HiveParser.GREATERTHAN, 0x000990);
		PRIORITY_MAP.put(HiveParser.GREATERTHANOREQUALTO, 0x000990);
		
		/* like系列 0x0008-- */
		PRIORITY_MAP.put(HiveParser.KW_LIKE, 0x000890);
		PRIORITY_MAP.put(HiveParser.KW_RLIKE, 0x000890);
		PRIORITY_MAP.put(HiveParser.KW_REGEXP, 0x000890);
		
		/* NOT 0x000690  AND 0x000590  OR  0x000490 */
		PRIORITY_MAP.put(HiveParser.KW_NOT, 0x000690);
		PRIORITY_MAP.put(HiveParser.KW_AND, 0x000590);
		PRIORITY_MAP.put(HiveParser.KW_OR, 0x000490);
	}
	
	private static HashMap<String, String> xlateMap = new HashMap<String, String>();
	static {
		xlateMap.put("KW_TRUE", "TRUE");
		xlateMap.put("KW_FALSE", "FALSE");
		xlateMap.put("KW_ALL", "ALL");
		xlateMap.put("KW_AND", "AND");
		xlateMap.put("KW_OR", "OR");
		xlateMap.put("KW_NOT", "NOT");
		xlateMap.put("KW_LIKE", "LIKE");

		xlateMap.put("KW_ASC", "ASC");
		xlateMap.put("KW_DESC", "DESC");
		xlateMap.put("KW_ORDER", "ORDER");
		xlateMap.put("KW_BY", "BY");
		xlateMap.put("KW_GROUP", "GROUP");
		xlateMap.put("KW_WHERE", "WHERE");
		xlateMap.put("KW_FROM", "FROM");
		xlateMap.put("KW_AS", "AS");
		xlateMap.put("KW_SELECT", "SELECT");
		xlateMap.put("KW_DISTINCT", "DISTINCT");
		xlateMap.put("KW_INSERT", "INSERT");
		xlateMap.put("KW_OVERWRITE", "OVERWRITE");
		xlateMap.put("KW_OUTER", "OUTER");
		xlateMap.put("KW_JOIN", "JOIN");
		xlateMap.put("KW_LEFT", "LEFT");
		xlateMap.put("KW_RIGHT", "RIGHT");
		xlateMap.put("KW_FULL", "FULL");
		xlateMap.put("KW_ON", "ON");
		xlateMap.put("KW_PARTITION", "PARTITION");
		xlateMap.put("KW_PARTITIONS", "PARTITIONS");
		xlateMap.put("KW_TABLE", "TABLE");
		xlateMap.put("KW_TABLES", "TABLES");
		xlateMap.put("KW_TBLPROPERTIES", "TBLPROPERTIES");
		xlateMap.put("KW_SHOW", "SHOW");
		xlateMap.put("KW_MSCK", "MSCK");
		xlateMap.put("KW_DIRECTORY", "DIRECTORY");
		xlateMap.put("KW_LOCAL", "LOCAL");
		xlateMap.put("KW_TRANSFORM", "TRANSFORM");
		xlateMap.put("KW_USING", "USING");
		xlateMap.put("KW_CLUSTER", "CLUSTER");
		xlateMap.put("KW_DISTRIBUTE", "DISTRIBUTE");
		xlateMap.put("KW_SORT", "SORT");
		xlateMap.put("KW_UNION", "UNION");
		xlateMap.put("KW_LOAD", "LOAD");
		xlateMap.put("KW_DATA", "DATA");
		xlateMap.put("KW_INPATH", "INPATH");
		xlateMap.put("KW_IS", "IS");
		xlateMap.put("KW_NULL", "NULL");
		xlateMap.put("KW_CREATE", "CREATE");
		xlateMap.put("KW_EXTERNAL", "EXTERNAL");
		xlateMap.put("KW_ALTER", "ALTER");
		xlateMap.put("KW_DESCRIBE", "DESCRIBE");
		xlateMap.put("KW_DROP", "DROP");
		xlateMap.put("KW_REANME", "REANME");
		xlateMap.put("KW_TO", "TO");
		xlateMap.put("KW_COMMENT", "COMMENT");
		xlateMap.put("KW_BOOLEAN", "BOOLEAN");
		xlateMap.put("KW_TINYINT", "TINYINT");
		xlateMap.put("KW_SMALLINT", "SMALLINT");
		xlateMap.put("KW_INT", "INT");
		xlateMap.put("KW_BIGINT", "BIGINT");
		xlateMap.put("KW_FLOAT", "FLOAT");
		xlateMap.put("KW_DOUBLE", "DOUBLE");
		xlateMap.put("KW_DATE", "DATE");
		xlateMap.put("KW_DATETIME", "DATETIME");
		xlateMap.put("KW_TIMESTAMP", "TIMESTAMP");
		xlateMap.put("KW_STRING", "STRING");
		xlateMap.put("KW_BINARY", "BINARY");
		xlateMap.put("KW_ARRAY", "ARRAY");
		xlateMap.put("KW_MAP", "MAP");
		xlateMap.put("KW_REDUCE", "REDUCE");
		xlateMap.put("KW_PARTITIONED", "PARTITIONED");
		xlateMap.put("KW_CLUSTERED", "CLUSTERED");
		xlateMap.put("KW_SORTED", "SORTED");
		xlateMap.put("KW_INTO", "INTO");
		xlateMap.put("KW_BUCKETS", "BUCKETS");
		xlateMap.put("KW_ROW", "ROW");
		xlateMap.put("KW_FORMAT", "FORMAT");
		xlateMap.put("KW_DELIMITED", "DELIMITED");
		xlateMap.put("KW_FIELDS", "FIELDS");
		xlateMap.put("KW_TERMINATED", "TERMINATED");
		xlateMap.put("KW_COLLECTION", "COLLECTION");
		xlateMap.put("KW_ITEMS", "ITEMS");
		xlateMap.put("KW_KEYS", "KEYS");
		xlateMap.put("KW_KEY_TYPE", "$KEY$");
		xlateMap.put("KW_LINES", "LINES");
		xlateMap.put("KW_STORED", "STORED");
		xlateMap.put("KW_SEQUENCEFILE", "SEQUENCEFILE");
		xlateMap.put("KW_TEXTFILE", "TEXTFILE");
		xlateMap.put("KW_INPUTFORMAT", "INPUTFORMAT");
		xlateMap.put("KW_OUTPUTFORMAT", "OUTPUTFORMAT");
		xlateMap.put("KW_LOCATION", "LOCATION");
		xlateMap.put("KW_TABLESAMPLE", "TABLESAMPLE");
		xlateMap.put("KW_BUCKET", "BUCKET");
		xlateMap.put("KW_OUT", "OUT");
		xlateMap.put("KW_OF", "OF");
		xlateMap.put("KW_CAST", "CAST");
		xlateMap.put("KW_ADD", "ADD");
		xlateMap.put("KW_REPLACE", "REPLACE");
		xlateMap.put("KW_COLUMNS", "COLUMNS");
		xlateMap.put("KW_RLIKE", "RLIKE");
		xlateMap.put("KW_REGEXP", "REGEXP");
		xlateMap.put("KW_TEMPORARY", "TEMPORARY");
		xlateMap.put("KW_FUNCTION", "FUNCTION");
		xlateMap.put("KW_EXPLAIN", "EXPLAIN");
		xlateMap.put("KW_EXTENDED", "EXTENDED");
		xlateMap.put("KW_SERDE", "SERDE");
		xlateMap.put("KW_WITH", "WITH");
		xlateMap.put("KW_SERDEPROPERTIES", "SERDEPROPERTIES");
		xlateMap.put("KW_LIMIT", "LIMIT");
		xlateMap.put("KW_SET", "SET");
		xlateMap.put("KW_PROPERTIES", "TBLPROPERTIES");
		xlateMap.put("KW_VALUE_TYPE", "$VALUE$");
		xlateMap.put("KW_ELEM_TYPE", "$ELEM$");

		xlateMap.put("DOT", "lib");
		xlateMap.put("COLON", ":");
		xlateMap.put("COMMA", ",");
		xlateMap.put("SEMICOLON", ");");

		xlateMap.put("LPAREN", "(");
		xlateMap.put("RPAREN", ")");
		xlateMap.put("LSQUARE", "[");
		xlateMap.put("RSQUARE", "]");

		xlateMap.put("EQUAL", "=");
		xlateMap.put("NOTEQUAL", "<>");
		xlateMap.put("EQUAL_NS", "<=>");
		xlateMap.put("LESSTHANOREQUALTO", "<=");
		xlateMap.put("LESSTHAN", "<");
		xlateMap.put("GREATERTHANOREQUALTO", ">=");
		xlateMap.put("GREATERTHAN", ">");

		xlateMap.put("DIVIDE", "/");
		xlateMap.put("PLUS", "+");
		xlateMap.put("MINUS", "-");
		xlateMap.put("STAR", "*");
		xlateMap.put("MOD", "%");

		xlateMap.put("AMPERSAND", "&");
		xlateMap.put("TILDE", "~");
		xlateMap.put("BITWISEOR", "|");
		xlateMap.put("BITWISEXOR", "^");
		xlateMap.put("CharSetLiteral", "\\'");
		
		xlateMap.put("TOK_FROM", "FROM");
        xlateMap.put("TOK_EXPLAIN", "EXPLAIN");
		xlateMap.put("TOK_SELECT", "SELECT");
		xlateMap.put("TOK_WHERE", "WHERE");
		xlateMap.put("TOK_SELECTDI", "SELECT DISTINCT");
		
		xlateMap.put("TOK_ALLCOLREF", "*");
		xlateMap.put("TOK_JOIN", "JOIN");
		xlateMap.put("TOK_LEFTOUTERJOIN", "LEFT OUTER JOIN");
		xlateMap.put("TOK_FULLOUTERJOIN", "FULL OUTER JOIN");
		xlateMap.put("TOK_RIGHTOUTERJOIN", "RIGHT OUTER JOIN");
		xlateMap.put("TOK_LEFTSEMIJOIN", "LEFT SEMI JOIN");
		xlateMap.put("TOK_CROSSJOIN", "CROSS JOIN");
		xlateMap.put("TOK_GROUPBY", "GROUP BY");
		xlateMap.put("TOK_DESTINATION", "INSERT OVERWRITE");
		xlateMap.put("TOK_INSERT_INTO", "INSERT INTO");
		
		xlateMap.put("TOK_TAB", "TABLE");
		xlateMap.put("TOK_DIR", "DIRECTORY");
		xlateMap.put("TOK_SORTBY", "SORT BY");
		xlateMap.put("TOK_TABSORTCOLNAMEASC", "ASC");
		xlateMap.put("TOK_TABSORTCOLNAMEDESC", "DESC");
		xlateMap.put("TOK_CLUSTERBY", "CLUSTER BY");
		xlateMap.put("TOK_ORDERBY", "ORDER BY");
		xlateMap.put("TOK_DISTRIBUTEBY", "DISTRIBUTE BY");
		xlateMap.put("TOK_HAVING", "HAVING");
		xlateMap.put("TOK_LIMIT", "LIMIT");
		xlateMap.put("TOK_UNION", "UNION ALL");
		xlateMap.put("TOK_ISNULL", "IS NULL");
		xlateMap.put("TOK_ISNOTNULL", "IS NOT NULL");

        xlateMap.put("TOK_NULL", "NULL");

        xlateMap.put("TOK_SHOWDATABASES", "SHOW DATABASES");
        xlateMap.put("TOK_SHOWTABLES", "SHOW TABLES");
        xlateMap.put("TOK_SHOWCOLUMNS", "SHOW COLUMNS");
        xlateMap.put("TOK_SHOWPARTITIONS", "SHOW PARTITIONS");
        xlateMap.put("TOK_SHOW_TBLPROPERTIES", "SHOW TBLPROPERTIES");
        xlateMap.put("TOK_SHOWFUNCTIONS", "SHOW FUNCTIONS");
        xlateMap.put("TOK_SHOWLOCKS", "SHOW LOCKS");
        xlateMap.put("TOK_SHOW_CREATETABLE", "SHOW CREATE TABLE");
        xlateMap.put("TOK_SHOW_ROLE_GRANT", "SHOW ROLE GRANT");

        xlateMap.put("TOK_USER", "USER");
        xlateMap.put("TOK_ROLE", "ROLE");
        xlateMap.put("TOK_DESCTABLE", "desc");

        xlateMap.put("TOK_TINYINT", "TINYINT");
        xlateMap.put("TOK_SMALLINT", "SMALLINT");
        xlateMap.put("TOK_INT", "INT");
        xlateMap.put("TOK_BIGINT", "BIGINT");
        xlateMap.put("TOK_BOOLEAN", "BOOLEAN");
        xlateMap.put("TOK_FLOAT", "FLOAT");
        xlateMap.put("TOK_DOUBLE", "DOUBLE");
        xlateMap.put("TOK_STRING", "STRING");
        xlateMap.put("TOK_BINARY", "BINARY");
        xlateMap.put("TOK_TIMESTAMP", "TIMESTAMP");
        xlateMap.put("TOK_DECIMAL", "DECIMAL");
        xlateMap.put("Tok_DATE", "DATE");
        xlateMap.put("TOK_VARCHAR", "VARCHAR");

        xlateMap.put("TOK_STREAMTABLE", "STREAMTABLE");
        xlateMap.put("TOK_MAPJOIN", "MAPJOIN");

        xlateMap.put("TOK_CREATETABLE", "CREATE TABLE");
        xlateMap.put("TOK_LIKETABLE", "AS");

        xlateMap.put("TOK_LATERAL_VIEW", "LATERAL VIEW");
        xlateMap.put("TOK_LATERAL_VIEW_OUTER", "LATERAL VIEW OUTER");
	}
	
}
