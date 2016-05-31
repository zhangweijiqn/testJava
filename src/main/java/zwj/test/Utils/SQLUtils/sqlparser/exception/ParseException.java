package zwj.test.Utils.SQLUtils.sqlparser.exception;


import zwj.test.Utils.SQLUtils.sqlparser.hive.parse.ParseError;

import java.util.List;

/**
 * Created by bjliuhongbin on 14-2-17.
 */
public class ParseException extends SQLException {
    private static final long serialVersionUID = 1L;
    List<ParseError> errors;

    @Deprecated
    public ParseException(String error) {
        super(error);
    }

    public ParseException(List<ParseError> errors) {
        super("");
        this.errors = errors;
    }

    @Override
    public String getMessage() {

        StringBuilder sb = new StringBuilder();
        for (ParseError err : errors) {
            sb.append(err.getMessage());
            sb.append("\n");
        }

        return sb.toString();
    }
}
