package zwj.test.Utils.SQLUtils.sqlparser.hive.adaptor;

import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveASTNode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.node.HiveParamNode;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenStream;
import org.antlr.runtime.tree.CommonTree;
import org.antlr.runtime.tree.CommonTreeAdaptor;
import org.apache.hadoop.hive.ql.parse.ASTErrorNode;

public class HiveSqlParseAdaptor extends CommonTreeAdaptor {

	/**
	 * Creates an ASTNode for the given token. The ASTNode is a wrapper around
	 * antlr's CommonTree class that implements the Node interface.
	 * 
	 * @param payload
	 *            The token.
	 * @return Object (which is actually an ASTNode) for the token.
	 */
	@Override
	public Object create(Token payload) {
		if (payload == null) {
			return  new HiveASTNode(payload);
		}
		
		int type = payload.getType();
		String name = payload.getText();
		if (type == 286 && !StringUtils.isEmpty(name) && name.startsWith("'${") && name.endsWith("}'") ) {
			String paramName = name.substring(3, name.length() - 2);
			if (!StringUtils.isEmpty(paramName)) {
				HiveParamNode paramNode =  new HiveParamNode(payload, paramName);
				return paramNode;
			}
		}

		return new HiveASTNode(payload);
	}

	@Override
	public Object dupNode(Object t) {

		return create(((CommonTree) t).token);
	};

	@Override
	public Object errorNode(TokenStream input, Token start, Token stop,
			RecognitionException e) {
		return new ASTErrorNode(input, start, stop, e);
	}

}
