package zwj.test.Utils.SQLUtils.sqlparser.hive.parse;

import org.antlr.runtime.BaseRecognizer;
import org.antlr.runtime.RecognitionException;

public class ParseError {
	private final BaseRecognizer br;
	private final RecognitionException re;
	private final String[] tokenNames;

	ParseError(BaseRecognizer br, RecognitionException re, String[] tokenNames) {
		this.br = br;
		this.re = re;
		this.tokenNames = tokenNames;
	}

	BaseRecognizer getBaseRecognizer() {
		return br;
	}

	RecognitionException getRecognitionException() {
		return re;
	}

	String[] getTokenNames() {
		return tokenNames;
	}

	public String getMessage() {
		return br.getErrorHeader(re) + " " + br.getErrorMessage(re, tokenNames);
	}
}
