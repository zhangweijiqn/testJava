package zwj.test.Utils.SQLUtils.sqlparser.hive;

import zwj.test.Utils.SQLUtils.DBType;
import zwj.test.Utils.SQLUtils.sqlparser.ISQLParser;
import zwj.test.Utils.SQLUtils.sqlparser.SQLParserFactory;
import zwj.test.Utils.SQLUtils.sqlparser.beans.Table;
import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.AbstractTableAdaptor;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.TableScope;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import zwj.test.Json.JSONUtils;
import org.apache.hadoop.hive.ql.parse.HiveParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableFinder {

	/**
	 * 查找表名
	 * @param context 上下文
	 * @return
	 * @throws SQLException
	 */
	public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
		int type = node.getType();
		AbstractTableAdaptor adaptor = getAdaptor(type);
		if (adaptor != null) {
			adaptor.findTables(context, node, ret);
		} else {
            goon(context, node, ret);
        }
	}
	
	private AbstractTableAdaptor getAdaptor(int type) {
		return adaptorFinder.get(type);
	}

    private static void findTablesFromTABREF(HiveSQLContext context, HiveASTNode node, List<Table> ret, TableScope scope) throws SQLException {
        if (node.getType() != HiveParser.TOK_TABREF) {
            goon(context, node, ret);

            return ;
        }

        HiveASTNode tabNameNode = (HiveASTNode) node.getChild(0);
        String alias = null;
        if (node.getChildCount() == 2) {
            alias = node.getChild(1).getText();
        }

        findTablesFromTABNAME(context, tabNameNode, ret, scope, alias);
    }

    private static Table findTablesFromTABNAME(HiveSQLContext context, HiveASTNode node, List<Table> ret, TableScope scope, String alias) throws SQLException {
        if (node.getType() != HiveParser.TOK_TABNAME) {
            goon(context, node, ret);

            return null;
        }

        int tabNameNodeChildrenCnt = node.getChildCount();

        String dbName = null;
        String tableName = null;

        if (tabNameNodeChildrenCnt == 1) {
            tableName = node.getChild(0).getText();
        } else if (tabNameNodeChildrenCnt == 2) {
            dbName = node.getChild(0).getText();
            tableName = node.getChild(1).getText();
        }

        Table table = new Table(dbName, tableName, alias, scope);
        ret.add(table);

        return table;
    }

    private static void goon(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
        int childrenCnt = node.getChildCount();
        for (int i = 0 ; i < childrenCnt; i++) {
            ((HiveASTNode) node.getChild(i)).findTables(context, ret);
        }
    }

	private static Map<Integer, AbstractTableAdaptor> adaptorFinder = new HashMap<Integer, AbstractTableAdaptor>();
    static {
        /* TOK_FROM  TableScope.QUERY_TABLE | TableScope.JOIN_TABLE*/
        adaptorFinder.put(HiveParser.TOK_FROM, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode firstChildNode = (HiveASTNode) node.getChild(0);
                int type = firstChildNode.getType();
                switch (type) {
                     /* TOK_TABREF */
                    case HiveParser.TOK_TABREF :
                        findTablesFromTABREF(context, firstChildNode, ret, TableScope.QUERY_TABLE);
                        break;
                    /* TOK_JOIN */
                    case HiveParser.TOK_JOIN :
                    case HiveParser.TOK_CROSSJOIN :
                    case HiveParser.TOK_FULLOUTERJOIN :
                    case HiveParser.TOK_LEFTOUTERJOIN :
                    case HiveParser.TOK_LEFTSEMIJOIN :
                    case HiveParser.TOK_MAPJOIN :
                    case HiveParser.TOK_RIGHTOUTERJOIN :
                    case HiveParser.TOK_UNIQUEJOIN :
                        findTablesFromJOIN(context, firstChildNode, ret);
                        break;
                    default:
                        goon(context, node, ret);
                        break;
                }
            }

            private void findTablesFromJOIN(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                if (node.getChild(0).getType() != HiveParser.TOK_TABREF) {
                    findTablesFromJOIN(context, (HiveASTNode) node.getChild(0), ret);
                } else {
                    findTablesFromTABREF(context, (HiveASTNode) node.getChild(0), ret, TableScope.QUERY_TABLE);
                }
                if(node.getChild(1)==null){
                    return ;
                }
                if (node.getChild(1).getType() == HiveParser.TOK_TABREF) {
                    findTablesFromTABREF(context, (HiveASTNode) node.getChild(1), ret, TableScope.JOIN_TABLE);
                } else {
                    ((HiveASTNode) node.getChild(1)).findTables(context, ret);
                }

            }

        });

         /* TOK_INSERT TableScope.INSERT_TABLE*/
        adaptorFinder.put(HiveParser.TOK_INSERT, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode firstNode = (HiveASTNode) node.getChild(0);
                int firstNodeType = firstNode.getType();
                switch (firstNodeType) {
                    case HiveParser.TOK_INSERT_INTO :
                    case HiveParser.TOK_DESTINATION :
                        HiveASTNode tabNameNode = (HiveASTNode) firstNode.getChild(0);
                        if (tabNameNode != null && tabNameNode.getType() == HiveParser.TOK_TABNAME) {
                            findTablesFromTABNAME(context, tabNameNode, ret, TableScope.INSERT_TABLE, null);
                        } else if (tabNameNode != null && tabNameNode.getType() == HiveParser.TOK_TAB) {
                            tabNameNode = (HiveASTNode) tabNameNode.getChild(0);

                            findTablesFromTABNAME(context, tabNameNode, ret, TableScope.INSERT_TABLE, null);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

        /* TOK_LOAD TableScope.LOAD_TABLE */
        adaptorFinder.put(HiveParser.TOK_LOAD, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode tabNode = (HiveASTNode) node.getChild(1);
                HiveASTNode tabNameNode = (HiveASTNode) tabNode.getChild(0);
                findTablesFromTABNAME(context, tabNameNode, ret, TableScope.LOAD_TABLE, null);
            }
        });

         /* TOK_DESCTABLE TableScope.DESC_TABLE */
        adaptorFinder.put(HiveParser.TOK_DESCTABLE, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode tabTypeNode = (HiveASTNode) node.getChild(0);
                HiveASTNode tabTypeFirstChildNode = (HiveASTNode) tabTypeNode.getChild(0);// DOT  Identifier
                int tabTypeFirstChildType = tabTypeFirstChildNode.getType();
                switch (tabTypeFirstChildType) {
                    case HiveParser.Identifier :
                    {
                        String tableName = tabTypeFirstChildNode.getText();
                        ret.add(new Table(null, tableName, null, TableScope.DESC_TABLE));
                    }
                        break;
                    case HiveParser.DOT :
                    {
                        HiveASTNode dotNode = tabTypeFirstChildNode;
                        HiveASTNode dotFirstChildNode = (HiveASTNode) dotNode.getChild(1);
                        if (dotFirstChildNode.getChildCount() == 2) {
                            dotNode = dotFirstChildNode;
                        }

                        String dbName = dotNode.getChild(0).getText();
                        String tableName = dotNode.getChild(1).getText();

                        ret.add(new Table(dbName, tableName, null, TableScope.DESC_TABLE));
                    }
                        break;
                }
            }
        });

        /* TOK_SHOWPARTITIONS  TableScope.SHOW_TABLE_ABOUT */
        adaptorFinder.put(HiveParser.TOK_SHOWPARTITIONS, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                String tableName = node.getChild(0).getText();

                ret.add(new Table(null, tableName, null, TableScope.SHOW_TABLE_ABOUT));
            }
        });
         /* TOK_SHOW_TBLPROPERTIES  TableScope.SHOW_TABLE_ABOUT */
        adaptorFinder.put(HiveParser.TOK_SHOW_TBLPROPERTIES, adaptorFinder.get(HiveParser.TOK_SHOWPARTITIONS));

        /* TOK_SHOW_CREATETABLE TableScope.SHOW_TABLE_ABOUT */
        adaptorFinder.put(HiveParser.TOK_SHOW_CREATETABLE, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode tabNameNode = (HiveASTNode) node.getChild(0);
                findTablesFromTABNAME(context, tabNameNode, ret, TableScope.SHOW_TABLE_ABOUT, null);
            }
        });

         /* TOK_SHOWCOLUMNS TableScope.SHOW_TABLE_ABOUT */
        adaptorFinder.put(HiveParser.TOK_SHOWCOLUMNS, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                int childrenCnt = node.getChildCount();
                if (childrenCnt == 1) {
                    HiveASTNode tabNameNode = (HiveASTNode) node.getChild(0);
                    findTablesFromTABNAME(context, tabNameNode, ret, TableScope.SHOW_TABLE_ABOUT, null);
                } else if (childrenCnt == 2) {
                    String dbName = node.getChild(0).getText();
                    HiveASTNode tabNameNode = (HiveASTNode) node.getChild(1);
                    Table table = findTablesFromTABNAME(context, tabNameNode, ret, TableScope.SHOW_TABLE_ABOUT, null);
                    if (table != null) {
                        table.setSchemaName(dbName);
                    }
                }
            }
        });

         /* TOK_SHOWLOCKS TableScope.SHOW_TABLE_ABOUT */
        adaptorFinder.put(HiveParser.TOK_SHOWLOCKS, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                String tableName = node.getChild(0).getChild(0).getText();

                ret.add(new Table(null, tableName, null, TableScope.SHOW_TABLE_ABOUT));
            }
        });

        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_ADDCOLS, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                String tableName = node.getChild(0).getText();

                ret.add(new Table(null, tableName, null, TableScope.ALTER_TABLE));
            }
        });
        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_ADDPARTS, adaptorFinder.get(HiveParser.TOK_ALTERTABLE_ADDCOLS));
        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_ARCHIVE, adaptorFinder.get(HiveParser.TOK_ALTERTABLE_ADDCOLS));
        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_DROPPARTS, adaptorFinder.get(HiveParser.TOK_ALTERTABLE_ADDCOLS));
        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_PROPERTIES, adaptorFinder.get(HiveParser.TOK_ALTERTABLE_ADDCOLS));
        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_REPLACECOLS, adaptorFinder.get(HiveParser.TOK_ALTERTABLE_ADDCOLS));
        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_TOUCH, adaptorFinder.get(HiveParser.TOK_ALTERTABLE_ADDCOLS));
        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_UNARCHIVE, adaptorFinder.get(HiveParser.TOK_ALTERTABLE_ADDCOLS));

        adaptorFinder.put(HiveParser.TOK_ALTERTABLE_RENAME, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                String oldName = node.getChild(0).getText();
                ret.add(new Table(null, oldName, null, TableScope.ALTER_TABLE));

                String newName = node.getChild(1).getText();
                ret.add(new Table(null, newName, null, TableScope.ALTER_TABLE));
            }
        });

       /* adaptorFinder.put(HiveParser.TOK_EXCHANGEPARTITION, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode tabNameNode = (HiveASTNode) node.getChild(0);

                findTablesFromTABNAME(context, tabNameNode, ret, TableScope.ALTER_TABLE, null);
            }
        });*/

        adaptorFinder.put(HiveParser.TOK_MSCK, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                String tableName = node.getChild(1).getText();

                ret.add(new Table(null, tableName, null, TableScope.ALTER_TABLE));
            }
        });

       /* adaptorFinder.put(HiveParser.TOK_ALTERTABLE_PARTITION, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                String tableName = node.getChild(0).getChild(0).getText();

                ret.add(new Table(null, tableName, null, TableScope.ALTER_TABLE));
            }
        });*/

        adaptorFinder.put(HiveParser.TOK_TRUNCATETABLE, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                String tableName = node.getChild(0).getChild(0).getText();

                ret.add(new Table(null, tableName, null, TableScope.TRUNCATE_TABLE));
            }
        });

        adaptorFinder.put(HiveParser.TOK_DROPTABLE, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode tabNameNode = (HiveASTNode) node.getChild(0);
                findTablesFromTABNAME(context, tabNameNode, ret, TableScope.DROP_TABLE, null);
            }
        });

        adaptorFinder.put(HiveParser.TOK_CREATETABLE, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode tabNameNode = (HiveASTNode) node.getChild(0);
                findTablesFromTABNAME(context, tabNameNode, ret, TableScope.CREATE_TABLE, null);

                int childrenCnt = node.getChildCount();
                if (childrenCnt > 1) {
                    for (int i = 1; i < childrenCnt; i ++) {
                        ((HiveASTNode) node.getChild(i)).findTables(context, ret);
                    }
                }
            }
        });

        adaptorFinder.put(HiveParser.TOK_LIKETABLE, new AbstractTableAdaptor() {
            @Override
            public void findTables(HiveSQLContext context, HiveASTNode node, List<Table> ret) throws SQLException {
                HiveASTNode tabNameNode = (HiveASTNode) node.getChild(0);
                if (tabNameNode ==  null) return ;
                findTablesFromTABNAME(context, tabNameNode, ret, TableScope.CREATE_TABLE_LIKE, null);
            }
        });

    }


	public static void main(String[] args) throws Exception {

        String sql = "ALTER TABLE t1 CONCATENATE";
		
//		String sql =
//				"create external table if not exists defdivbase(hbaserowid string,"
//				+ "`regioncode` string comment '机构编号',`locno` string comment '') comment ''"
//				+ "stored by 'org.apache.hadoop.hive.hbase.HBaseStorageHandler' "
//				+ "WITH SERDEPROPERTIES(\"hbase.columns.mapping\"=\":key,fml:`regioncode`,fml:`memo`\")TBLPROPERTIES(\"hbase.table.name\"=\"defdivbase\")";		
//		HiveSqlParser parser = HiveSqlParser.parse(sql);
        ISQLParser parser = SQLParserFactory.getSQLParser(DBType.Hive, sql);
//        parser.findTableNames();
        System.out.println(JSONUtils.toJSON(parser.findTableNames()));
//		System.out.println(parser.sqlDump());
	}
}
