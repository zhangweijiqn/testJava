package zwj.test.Utils.SQLUtils.sqlparser.hive.parse;

import org.antlr.runtime.RecognitionException;
import org.antlr.runtime.TokenStream;

import java.util.ArrayList;

/**
 * Created by bjliuhongbin on 14-2-17.
 */
public class LocalHiveParser extends org.apache.hadoop.hive.ql.parse.HiveParser {
    private final ArrayList<ParseError> errors;

    public LocalHiveParser(TokenStream input) {
        super(input);
        errors = new ArrayList<ParseError>();
    }

    public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        this.errors.add(new ParseError(this, e, tokenNames));
    }

    public ArrayList<ParseError> getErrors() {
        return errors;
    }
}
