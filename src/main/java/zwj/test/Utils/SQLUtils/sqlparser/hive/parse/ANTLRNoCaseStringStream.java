package zwj.test.Utils.SQLUtils.sqlparser.hive.parse;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;

/**
 * 用于消除大小写敏感问题
 * @author bjliuhongbin
 *
 */
public class ANTLRNoCaseStringStream extends ANTLRStringStream {
	public ANTLRNoCaseStringStream(String input) {
		super(input);
	}

	@Override
	public int LA(int i) {

		int returnChar = super.LA(i);
		if (returnChar == CharStream.EOF) {
			return returnChar;
		} else if (returnChar == 0) {
			return returnChar;
		}

		return Character.toUpperCase((char) returnChar);
	}
	
}
