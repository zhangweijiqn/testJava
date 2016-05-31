package zwj.test.Utils.SQLUtils.sqlparser.hive.parse;

import zwj.test.Utils.SQLUtils.sqlparser.utils.StringUtils;
import org.antlr.runtime.Token;
import org.antlr.runtime.TokenRewriteStream;

/**
 * 继承自{@link TokenRewriteStream}, 在其基础上增加了对本地自定义参数的处理
 * @author bjliuhongbin
 *
 */
public class HiveTokenRewriteStream extends TokenRewriteStream {

	public HiveTokenRewriteStream(LocalHiveLexer lexer) {
		super(lexer);
	}
	
	/**
	 * 处理所有的参数
	 */
	public void processParamTokens() {
		fill();
		int pos = 0;
		while (pos < this.tokens.size()) {
			Token token = (Token)this.tokens.get(pos);
            String tokenStr = token.getText();
            String nextTokenStr = "";
            if (this.size() > pos + 1) {
                Token next = (Token)this.tokens.get(pos + 1);
                nextTokenStr = next.getText();
            }

			if ("/".equals(tokenStr) && "*".equals(nextTokenStr)) {
                tryHandleNotes(pos);
			} else if ("$".equals(tokenStr)) {
                mergeParamTokens(pos);
            }
			
			pos ++;
		}


	}

    private void tryHandleNotes(int pos) {
        int index = pos;
//        String left = this.tokens.get(index).getText() + this.tokens.get(index ++).getText();
        String right = null;
//        String note = "";
        while (true) {
            if (this.size() <= index) {
                return ;
            }

            Token token = this.get(index);
            String tokenStr = token.getText();
            Token next = this.get(index + 1);
            String nextStr = next.getText();

            if ("*".equals(tokenStr) && "/".equals(nextStr)) {
                right = "*/";
                index ++;
                break;
            } else {
//                note += tokenStr;
                index ++;
            }
        }

        if (StringUtils.isEmpty(right)) {
            return ;
        }

        if (pos + 3 == index) {
            delete(pos, index);
        }
    }
	
	/*
	 * 合并变量token
	 * @param pos 开始索引
	 */
	private void mergeParamTokens(int pos) {
		if (pos + 3 >= this.tokens.size()) {
			return ;
		}
		
		Token leftParenthesis = (Token)this.tokens.get(pos + 1);
		String nameToken = "";
		int index = pos + 2;
		while(true) {
            if (this.size() <= index) {
                return ;
            }

			Token tmp = this.get(index);
			nameToken += tmp.getText();
			
			index ++;
			Token next = this.get(index);
			if ("}".equals(next.getText())) {
				break;
			}
		}
		Token rightParenthesis = this.get(index);
		
		String leftParenthesisStr = leftParenthesis.getText();
		String rightParenthesisStr = rightParenthesis.getText();
		
		if (!"{".equals(leftParenthesisStr) 
				|| !"}".equals(rightParenthesisStr) 
				|| StringUtils.isEmpty(nameToken)) {
			return ;
		}
		
		doMergeTokens(pos, index, "'${" + nameToken + "}'");
	}
	
	/* 
	 * 合并
	 * @param start 第一个需要删除token的索引
	 * @param end 最后一个需要删除token的索引
	 * @param tokens 连续的索引
	 */
	private void doMergeTokens(int start, int end, String name) {
		delete(start, end);
		insertBefore(start, name);
	}
}
