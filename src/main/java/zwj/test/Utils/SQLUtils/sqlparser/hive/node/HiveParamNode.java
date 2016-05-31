package zwj.test.Utils.SQLUtils.sqlparser.hive.node;

import zwj.test.Utils.SQLUtils.sqlparser.exception.SQLException;
import zwj.test.Utils.SQLUtils.sqlparser.hive.enums.ErrorCode;
import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.HiveSQLContext;
import zwj.test.Utils.SQLUtils.sqlparser.param.AbstractParam;
import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import org.antlr.runtime.Token;

/**
 * SQL参数将诶点
 * @author bjliuhongbin
 *
 */
public class HiveParamNode extends HiveASTNode {
	
	private static final long serialVersionUID = -7416089921422287451L;
	
	private String paramName ;
	
	private String[] functionParams;
	
	public HiveParamNode(Token payload, String paramNodeText) throws SQLException {
		super(payload);
		loadInfo(paramNodeText);;
	}
	
	public String getParamName() {
		return paramName;
	}

	@Override
	public String toSQL(HiveSQLContext context) throws SQLException {
		if (context == null) {
			return null;
		}
		
		AbstractParam param = context.getParamAdaptor().find(paramName);
		if (param == null) {
			return getText();
		}

        param.check(functionParams);
		
		return param.parse2SQL(functionParams);
	}

    public static String[] parseParams(String exp) throws SQLException{
        if (StringUtils.isEmpty(exp)) {
            throw new SQLException("Error parameter pattern");
        }

        String[] tokens = exp.split(",");
        for (int i = 0; i < tokens.length ; i ++) {
            if (StringUtils.isEmpty(tokens[i])) {
                continue;
            }

            tokens[i] = tokens[i].trim();
        }

        return tokens;
    }
	
	private void loadInfo(String paramNodeText) throws  SQLException {
        if (StringUtils.isEmpty(paramNodeText)) {
            throw new SQLException("Error Param Patterns: Param name can not be empty", ErrorCode.DEFAULT_SQL_ERROR);
        }

        String[] params = parseParams(paramNodeText);
        this.paramName = params[0];

        if (params.length > 1) {
            functionParams = new String[params.length - 1];
            for (int i = 1; i < params.length; i++) {
                functionParams[i - 1] = params[i];
            }
        }

//		this.exp = paramNodeText;
//		if (StringUtils.isEmpty(paramNodeText)) {
//			return ;
//		}
//
//		int opIndex = paramNodeText.indexOf('+');
//        if (opIndex < 0) {
//			opIndex = paramNodeText.indexOf('-');
//		} else {
//            int _opIndex = paramNodeText.indexOf('-');
//            if (_opIndex > 0 && _opIndex < opIndex) {
//                opIndex = _opIndex;
//            }
//        }
//
//		if (opIndex < 0) {
//			paramName = paramNodeText;
//		} else {
//			paramName = paramNodeText.substring(0, opIndex).trim();
//		}
//
//        check();
	}

//    private void check() throws SQLException {
//        if (StringUtils.isEmpty(this.exp)) {
//            throw new SQLException("Parameter expression can not be empty", ErrorCode.DEFAULT_SQL_ERROR);
//        }
//
//        String _exp = this.exp;
//        if (this.paramName.trim().indexOf(' ') > 0) {
//            throw new SQLException("Space symbols are not allowed to appear in the Parameter name", ErrorCode.DEFAULT_SQL_ERROR);
//        }
//
//        if(StringUtils.isEmpty(this.paramName)) {
//            throw new SQLException("There none param name in Parameter expression", ErrorCode.DEFAULT_SQL_ERROR);
//        }
//
//        int opIndex = this.exp.indexOf('+');
//        if (opIndex < 0 ) {
//            opIndex = this.exp.indexOf('-');
//        } else {
//            int _opIndex = this.exp.indexOf('-');
//            if (_opIndex > 0 && _opIndex < opIndex) {
//                opIndex = _opIndex;
//            }
//        }
//
//        int offset = 0;
//        if (opIndex > 0 && opIndex < this.exp.length() - 1) {
//            try {
//                String _offsetStr = this.exp.substring(opIndex + 1);
//                if (!StringUtils.isEmpty(_offsetStr)) {
//                    _offsetStr = _offsetStr.trim();
//                }
//                offset = Integer.parseInt(_offsetStr);
//            } catch (Exception e) {
//                throw new SQLException("parameter offset can only be positive integer" , e, ErrorCode.DEFAULT_SQL_ERROR);
//            }
//
//        }
//
//        if (offset < 0 || offset > 99) {
//            throw new SQLException("parameter offset can only between 0 and 99", ErrorCode.DEFAULT_SQL_ERROR);
//        }
//    }
	
}
