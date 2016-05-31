package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor.generator.*;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.ErrorCode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQlGenerator {

	/**
	 * 将某节点转换成SQL
	 * @param context 上下文
	 * @param node 要转换的节点
	 * @return
	 * @throws SQLException
	 */
	public String generatSQL(HiveSQLContext context, HiveASTNode node) throws SQLException {
		AbstractSQLAdaptor adaptor = getAdaptor(node.getType());
		if (adaptor != null) {
			return adaptor.generatSQL(context, node);
		} else {
			throw new SQLException("不支持此类sql的Generate", ErrorCode.DEFAULT_SQL_ERROR);
		}
		
	}
	
	/*
	 * 返回节点类型对应的转换器
	 * @param type 节点类型
	 * @return
	 */
	private AbstractSQLAdaptor getAdaptor(int type) {
		return adaptorFinder.get(type);
	}
	
	/**
	 * 返回所有拥有的数据类型
	 * @return
	 */
	private static AbstractSQLAdaptor[] getALLAdaptors() {
		return new AbstractSQLAdaptor[] {
				new AllColrefSQLAdaptor(),
				new BinaryOperatorSQLAdaptor(),
				new ClauseNeedChildrenSQLAdaptor(),
				new ClauseSQLAdaptor(),
				new DataNodeSQLAdaptor(),
				new DotSQlAdaptor(),
				new FunctionSQLAdaptor(),
				new FunctionStarSQLAdaptor(),
				new JoinSQLAdaptor(),
				new OrderTypeSQLAdaptor(),
				new SpecialClauseSQLAdaptor(),
				new SubQuerySQLAdaptor(),
				new TableOrColSQLAdaptor(),
				new UnaryOperatorSQLAdaptor(),
				new PartitionSQLAdaptor(),
                new ShowClauseSQLAdapter(),
                new StaticNodeSQLAdapter(),
                new LimitSQLAdapter(),
                new HintListSQLAdaptor(),
                new QuerySQLAdaptor(),
                new LateralViewSQLAdaptor()
		};
	}
	
	/*
	 * sql生成适配器查找表
	 */
	private static Map<Integer, AbstractSQLAdaptor> adaptorFinder = new HashMap<Integer, AbstractSQLAdaptor>();
	static {
		AbstractSQLAdaptor[] adaptors = getALLAdaptors();
		for (AbstractSQLAdaptor adaptor : adaptors) {
			List<Integer> acceptTypes = adaptor.acceptTypes();
			for (Integer type : acceptTypes) {
				adaptorFinder.put(type, adaptor);
			}
		}
	}
}
