package zwj.test.Utils.SQLUtils.sqlparser.hive.parse;

import org.antlr.runtime.CharStream;
import org.antlr.runtime.NoViableAltException;
import org.antlr.runtime.RecognitionException;

import java.util.ArrayList;

public class LocalHiveLexer extends org.apache.hadoop.hive.ql.parse.HiveLexer {
	private final ArrayList<ParseError> errors;

	public LocalHiveLexer() {
		super();
		errors = new ArrayList<ParseError>();
	}

	public LocalHiveLexer(CharStream input) {
		super(input);
		errors = new ArrayList<ParseError>();
	}

	@Override
	public void displayRecognitionError(String[] tokenNames,
			RecognitionException e) {

		errors.add(new ParseError(this, e, tokenNames));
	}

	@Override
	public String getErrorMessage(RecognitionException e, String[] tokenNames) {
		String msg = null;

		if (e instanceof NoViableAltException) {
			@SuppressWarnings("unused")
			NoViableAltException nvae = (NoViableAltException) e;
			// for development, can add
			// "decision=<<"+nvae.grammarDecisionDescription+">>"
			// and "(decision="+nvae.decisionNumber+") and
			// "state "+nvae.stateNumber
			msg = "character " + getCharErrorDisplay(e.c)
					+ " not supported here";
		} else {
			msg = super.getErrorMessage(e, tokenNames);
		}

		return msg;
	}
	
	public ArrayList<ParseError> getErrors() {
		return errors;
	}
}
