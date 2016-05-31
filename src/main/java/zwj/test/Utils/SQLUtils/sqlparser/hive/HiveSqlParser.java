package zwj.test.Utils.SQLUtils.sqlparser.hive;

import zwj.test.Utils.SQLUtils.sqlparser.ISQLParser;
import zwj.test.Utils.SQLUtils.sqlparser.beans.JoinCondition;
import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.exception.ParseException;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.HiveSqlParseAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.SQLType;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.*;
import zwj.test.Utils.SQLUtils.sqlparser.param.AbstractParam;
import zwj.test.Utils.SQLUtils.sqlparser.param.SQLParamAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.param.SysDate;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.tree.Tree;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.ArrayList;
import java.util.List;

//import com.sun.org.apache.bcel.internal.generic.RETURN;

public class HiveSqlParser implements ISQLParser {

	private String orgSQL;		// 最原始的SQL语句
	private HiveASTNode tree;		// 解析后的语法树

	private SQLParamAdaptor paramAdaptor;// sql参数适配器

    private int limitNum = - 1;// limit行数
    private boolean overWriteLimit;// 是否要覆盖Limit

    private SQLType type = SQLType.UNKNOWN;

	public HiveSqlParser(String orgSQL) throws SQLException {
		this.orgSQL = orgSQL;

        init();
	}

	/**
	 * 解析sql，并返回解析器
	 * @param orgSQL 原始的sql语句
	 * @return
	 * @throws SQLException
	 */
    public static HiveSqlParser parse(String orgSQL) throws SQLException {
		return new HiveSqlParser(orgSQL);
	}

	/**
	 * 判断当前sql语句是什么类型
	 * @return sql语句类型，参见{@link SQLType}
	 * @throws SQLException
	 */
	public SQLType getType() throws SQLException {
        if (this.type != SQLType.UNKNOWN)  {
            return this.type;
        }

		int type = this.tree.getType();
		switch (type) {
		case HiveParser.TOK_QUERY:
			{
				Tree insert = this.tree.getChild(1);
				Tree destination = insert.getChild(0);
				Tree dir = destination.getChild(0);
				if (dir.getType() == HiveParser.TOK_DIR) {
					return SQLType.SELECT;
				} else {
					return SQLType.INSERT;
				}
			}
		case HiveParser.TOK_UNION:
			return SQLType.SELECT;
		case HiveParser.TOK_CREATETABLE:
			return SQLType.CREATE_TABLE;
		case HiveParser.TOK_DROPTABLE:
			return SQLType.DROP_TABLE;
		case HiveParser.TOK_TRUNCATETABLE:
			return SQLType.TRUNK_TABLE;
		case HiveParser.TOK_ALTERTABLE_RENAME:
		case HiveParser.TOK_ALTERTABLE_RENAMECOL:
		case HiveParser.TOK_ALTERTABLE_ADDCOLS:
		case HiveParser.TOK_ALTERTABLE_REPLACECOLS:
		case HiveParser.TOK_ALTERTABLE_PROPERTIES:
		case HiveParser.TOK_ALTERTABLE_ADDPARTS:
		case HiveParser.TOK_ALTERTABLE_DROPPARTS:
		case HiveParser.TOK_ALTERTABLE_ARCHIVE:
		case HiveParser.TOK_ALTERTABLE_UNARCHIVE:
//		case org.apache.hadoop.hive.ql.parse.HiveParser.TOK_ALTERTABLE_PARTITION:
			return SQLType.ALERT_TABLE;
        case HiveParser.TOK_SWITCHDATABASE:
            return SQLType.USE;
        case HiveParser.TOK_SHOW_CREATETABLE:
        case HiveParser.TOK_SHOWINDEXES:
        case HiveParser.TOK_SHOW_GRANT:
        case HiveParser.TOK_SHOW_ROLE_GRANT:
        case HiveParser.TOK_SHOW_TABLESTATUS:
        case HiveParser.TOK_SHOWCOLUMNS:
        case HiveParser.TOK_SHOWFUNCTIONS:
        case HiveParser.TOK_SHOWLOCKS:
        case HiveParser.TOK_SHOWPARTITIONS:
            return SQLType.SHOW_OTHERS;
        case HiveParser.TOK_SHOWTABLES:
            return SQLType.SHOW_TABLES;
        case HiveParser.TOK_SHOWDATABASES:
            return SQLType.SHOW_DATABASES;
        case HiveParser.TOK_DESCTABLE:
            return SQLType.DESC_TABLE;
        case HiveParser.TOK_SHOW_TBLPROPERTIES:
            return SQLType.SHOW_TABLE_PROPERTIES;
		default:
			return SQLType.UNKNOWN;
		}

//		if (type != HiveParser.TOK_QUERY) {
//			return SQLType.UNKNOWN;
//		} else {
//			Tree insert = this.tree.getChild(0);
//			Tree destination = insert.getChild(0);
//			Tree dir = destination.getChild(0);
//			if (dir.getType() == HiveParser.TOK_DIR) {
//				return SQLType.SELECT;
//			} else {
//				return SQLType.INSERT;
//			}
//		}
	}

	/**
	 * 注册参数
	 * @param params 要注册的参数列表
	 */
	public void registerParams(AbstractParam... params) {
		this.paramAdaptor.addParams(params);
	}

	/**
	 * 加入自定义sql条件
	 * @param conditions 自定义sql条件列表
	 * @throws SQLException
	 */
	public void fillConditions(JoinCondition...conditions) throws SQLException {
        if (type != SQLType.UNKNOWN) {
            return ;
        }

		HiveSQLContext context = createContext();
		context.addConditions(conditions);

		tree.fillConditions(context);
	}

    /**
     * 设置sql中的limit
     * @param limit 限制的行数
     * @param overwrite 是否覆盖sql中已经设置的limit数
     * @throws Exception
     */
    public void setLimit(int limit, boolean overwrite) throws SQLException {
        this.limitNum = limit;
        this.overWriteLimit = overwrite;
    }

    public String generateSQL(boolean format) throws SQLException {

        if (type != SQLType.UNKNOWN || tree == null) {
            return orgSQL;
        }

        HiveSQLContext context = createContext();
        context.setOverWriteLimit(this.overWriteLimit);
        context.setLimitNum(this.limitNum);
        context.setFormat(format);

        String handleSQL = tree.toSQL(context);
        if (!context.isHasLimit() && this.limitNum > 0) {
            return handleSQL + " limit " + this.limitNum;
        } else {
            return handleSQL;
        }
    }

    public String generateSQL() throws SQLException {
        return generateSQL(false);
    }

//	/**
//	 * 将语法树转换成SQL语句
//	 * @return
//	 * @throws SQLException
//	 */
//	public String generateSQL() throws SQLException {
//		return generateSQL(null);
//	}

	/**
	 * 查找sql中使用到的表名
	 * @return
	 * @throws SQLException
	 */
	public List<Table> findTableNames() throws SQLException {
        if (type != SQLType.UNKNOWN) {
            return new ArrayList<Table>();
        }

		List<Table> ret = new ArrayList<Table>();

		HiveSQLContext context = createContext();
		this.tree.findTables(context, ret);

		return ret;
	}

    public List<String> findDatabases()throws SQLException {
        if (type != SQLType.UNKNOWN) {
            return new ArrayList<String>();
        }

        List<String> ret = new ArrayList<String>();

        DatabaseFinder finder = new DatabaseFinder();
        finder.find(tree, ret);

        return ret;
    }

    /**
     * use语句时，返回dbname
     * @return
     */
    public String findDBIfUseType() {
        if (type != SQLType.UNKNOWN) {
            return null;
        }

        SQLType type = getType();
        if (type == SQLType.USE) {
            HiveASTNode useNode = getRoot();
            return useNode.getChild(0).getText();
        }

        return null;
    }

	/**
	 * 用于测试是查看sql结构用
	 * @return
	 */
	public String sqlDump() {
        if (type != SQLType.UNKNOWN) {
            return orgSQL;
        }

		return this.tree.dump();
	}

	/**
	 * 返回解析后的语法树根节点
	 * @return
	 */
	public HiveASTNode getRoot() {
		return this.tree;
	}

	/**
	 * 构建SQL解析上下文
	 * @return
	 */
	private HiveSQLContext createContext() {
		return new HiveSQLContext(this.paramAdaptor);
	}

    private void check(LocalHiveLexer lexer, LocalHiveParser parser) throws SQLException {
        if (type != SQLType.UNKNOWN) {
            return ;
        }

           List<ParseError> lexerErrors = lexer.getErrors();
           List<ParseError> parserErrors = parser.getErrors();

           if ((lexerErrors.size() == 0) && (parserErrors.size() == 0)) {

           } else {
               if (lexerErrors.size() != 0) {
                   throw new ParseException(lexerErrors);
               }
               throw new ParseException(parserErrors);
           }
    }

	@SuppressWarnings("deprecation")
	private void init() throws SQLException {
        discernCommandType();
        if (type != SQLType.UNKNOWN) {
            return ;
        }

		String processedSQL = processParamTokens(orgSQL);
		CharStream noCaseStringStream = getNoCaseStringStream(processedSQL);
		LocalHiveLexer lexer = new LocalHiveLexer(noCaseStringStream);
		HiveTokenRewriteStream tokens = new HiveTokenRewriteStream(lexer);
		LocalHiveParser parser = new LocalHiveParser(tokens);
		parser.setTreeAdaptor(new HiveSqlParseAdaptor());

		HiveParser.statement_return r = null;
		try {
			r = parser.statement();

			HiveASTNode root = (HiveASTNode) r.getTree();
			this.tree = findRootNonNullToken(root);
		} catch (Throwable e) {
			throw new SQLException(e);
		}

        check(lexer, parser);

		paramAdaptor = new SQLParamAdaptor();

		initDefaultParams();
	}

    private void discernCommandType() {
        String[] tokens = orgSQL.trim().split("\\s+");
        String command = tokens[0].toLowerCase();

        if ("set".equals(command)) {
            type = SQLType.SET;
        } else if ("dfs".equals(command)) {
            type = SQLType.DFS;
        } else if ("add".equals(command)) {
            type = SQLType.ADD;;
        } else if ("delete".equals(command)) {
            type = SQLType.DELETE;;
        }
    }

	// 初始化默认参数
	private void initDefaultParams() {
        registerParams(new SysDate());
	}

	/*
	 * 处理sql语句中的参数token
	 */
	private String processParamTokens(String sql) {
		CharStream noCaseStringStream = getNoCaseStringStream(sql);
		LocalHiveLexer lexer = new LocalHiveLexer(noCaseStringStream);
		HiveTokenRewriteStream tokens = new HiveTokenRewriteStream(lexer);
		tokens.processParamTokens();

		return tokens.toString();
//        return sql;
	}

	/*
	 * 忽略大小写处理
	 */
	private CharStream getNoCaseStringStream(String sql) {
		return new ANTLRNoCaseStringStream(sql);
	}

	/**
	 * Performs a descent of the leftmost branch of a tree, stopping when either
	 * a node with a non-null token is found or the leaf level is encountered.
	 *
	 * @param tree
	 *            candidate node from which to start searching
	 *
	 * @return node at which descent stopped
	 */
	private static HiveASTNode findRootNonNullToken(HiveASTNode tree) {
		while ((tree.getToken() == null) && (tree.getChildCount() > 0)) {
			tree = (HiveASTNode) tree.getChild(0);
		}
		return tree;
	}

    public static void main(String[] args) {/*测试*//*+ parallel(t,32) */
//        String sql = "SELECT /*+ MAPJOIN(a, b) */ a FROM t";
//        String sql = "SELECT /* STREAMTABLE(a) , MAPJOIN(a, b) */ a from t";

//        String sql = "SELECT /*+ parallel(t,32) */ a from t";
//        String sql = "select ${sys_date, -1, yyyyMMdd} from t1";
//        String sql = "select concat(s.session_id,'_',cast(t.an as string)) shop_session_id from t1";
//        String sql = "select cast(t.an as string) shop_session_id from t1";
//        String sql = "select concat(s.session_id,'_','aaa') shop_session_id from t1";
        //String sql = "SELECT a.val, b.val, c.val FROM a JOIN b ON (a.key = b.key1) JOIN c ON (c.key = b.key2) ";
//        		String sql = "select *  "
//				+ "from "
//				+ "(select t1.* from seller join t1 on seller.sid= t1.sid) t1 "
//				+ "join "
//				+ "(select t2.* from seller join t2 on seller.sid=t2.sid) t2"
//				+ " on t1.id=t2.id"
//				+ " where t1.name=''";
        String sql = "SELECT page_views.* "
                + "FROM page_views "
                + " join t1 on(true)"
                + "JOIN dim_users "
                + "ON (page_views.user_id = dim_users.id AND page_views.date >= '2008-03-01' AND page_views.date <= '2008-03-31')";
//        String sql = "FROM  (\n" +
//                "\tFROM \n" +
//                "\t\tdws.dws_shop_access_log_d \n" +
//                "\tSELECT \n" +
//                "\t\t* \n" +
//                "\tWHERE dt = '20150426' and shop_id in('10026', '51893', '35078', '18784', '11702', '65414', '54134', '20638', '56957', '58528', '42547', '28837', '17117', '72295', '56957', '32514', '69506')) a\n" +
//                "\tLEFT OUTER JOIN  (\n" +
//                "\t\tFROM \n" +
//                "\t\t\tdws.bdp_dws_shop_access_log_d \n" +
//                "\t\tSELECT \n" +
//                "\t\t\t* \n" +
//                "\t\tWHERE dt = '20150426' and shop_id in('10026', '51893', '35078', '18784', '11702', '65414', '54134', '20638', '56957', '58528', '42547', '28837', '17117', '72295', '56957', '32514', '69506')) b\n" +
//                "\t\t ON (a.the_dt = b.the_dt and a.shop_id = b.shop_id and a.seller_id = b.seller_id and a.buyer_id = b.buyer_id and a.visit_tm = b.visit_tm and a.visit_platform = b.visit_platform and a.ip = b.ip and a.uuid = b.uuid and a.session_id = b.session_id and a.ref_url = b.ref_url and a.access_url = b.access_url and a.url_title = b.url_title) \n" +
//                "SELECT \n" +
//                "\ta.* \n" +
//                "WHERE b.the_dt IS NULL";
            String sql1 = "CREATE TABLE IF NOT EXISTS db_1.bbb(" +
                    "name STRING comment '姓名' )" +
                    " partitioned by (dt STRING ) " +
                    "row format delimited fields terminated by '\\u0001' lines terminated by '\\n'" +
                    "stored as TEXTFILE";//sql中最后不能有分号
        try {
            HiveSqlParser parser = HiveSqlParser.parse(sql1);

            List<Table> tableList =parser.findTableNames();
            for(Table t : tableList) {
                System.out.println(t.getName());
            }
//            System.out.println( parser.sqlDump());
            System.out.println(parser.generateSQL(true));
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }
}
