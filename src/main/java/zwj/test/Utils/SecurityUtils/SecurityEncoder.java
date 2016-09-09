package zwj.test.Utils.SecurityUtils;

/**
 * Created by zhangwj on 16-9-9.
 */
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SecurityEncoder {
    private List codecs = new ArrayList();

    private static final char[] IMMUNE_HTML = new char[]{',', '.', '-', '_', ' '};
    private static final char[] IMMUNE_HTMLATTR = new char[]{',', '.', '-', '_'};
    private static final char[] IMMUNE_CSS = new char[0];
    private static final char[] IMMUNE_JAVASCRIPT = new char[]{',', '.', '_'};
    private static final char[] IMMUNE_URL = new char[0];

    public SecurityEncoder() {

    }

    public String jsonFilter(String CallbackStr) {
        if(CallbackStr != null && !"".equals(CallbackStr)) {
            CallbackStr = CallbackStr.replaceAll("[^\\w\\_\\.]", " ");
            return CallbackStr;
        } else {
            return "";
        }
    }

    public String filterForGetRequest(String input) {
        String result = input;
        if(input != null && !"".equals(input)) {
            Pattern pattern = Pattern.compile("<[^>]*?=[^>]*?&#[^>]*?>|\\b(alert\\(|confirm\\(|expression\\(|prompt\\()|<[^>]*?\\b(onerror|onmousemove|onload|onclick|onmouseover)\\b[^>]*?>|\\b(and|or)\\b\\s*?([\\(\\)\'\"\\d]+?=[\\(\\)\'\"\\d]+?|[\\(\\)\'\"a-zA-Z]+?=[\\(\\)\'\"a-zA-Z]+?|>|<|\\s+?[\\w]+?\\s+?\\bin\\b\\s*?\\(|\\blike\\b\\s+?[\"\'])|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)", 2);
            Matcher mat = pattern.matcher(input);
            if(mat.find()) {
                result = mat.replaceAll("");
            }

            return result;
        } else {
            return "";
        }
    }

    public String filterForPostRequest(String input) {
        String result = input;
        if(input != null && !"".equals(input)) {
            Pattern pattern = Pattern.compile("<[^>]*?=[^>]*?&#[^>]*?>|\\b(alert\\(|confirm\\(|expression\\(|prompt\\()|<[^>]*?\\b(onerror|onmousemove|onload|onclick|onmouseover)\\b[^>]*?>|\\b(and|or)\\b\\s*?([\\(\\)\'\"\\d]+?=[\\(\\)\'\"\\d]+?|[\\(\\)\'\"a-zA-Z]+?=[\\(\\)\'\"a-zA-Z]+?|>|<|\\s+?[\\w]+?\\s+?\\bin\\b\\s*?\\(|\\blike\\b\\s+?[\"\'])|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)", 2);
            Matcher mat = pattern.matcher(input);
            if(mat.find()) {
                result = mat.replaceAll("");
            }

            return result;
        } else {
            return "";
        }
    }

    public String filterForCookie(String input) {
        String result = input;
        if(input != null && !"".equals(input)) {
            Pattern pattern = Pattern.compile("\\b(and|or)\\b\\s*?([\\(\\)\'\"\\d]+?=[\\(\\)\'\"\\d]+?|[\\(\\)\'\"a-zA-Z]+?=[\\(\\)\'\"a-zA-Z]+?|>|<|\\s+?[\\w]+?\\s+?\\bin\\b\\s*?\\(|\\blike\\b\\s+?[\"\'])|\\/\\*.+?\\*\\/|<\\s*script\\b|\\bEXEC\\b|UNION.+?SELECT|UPDATE.+?SET|INSERT\\s+INTO.+?VALUES|(SELECT|DELETE).+?FROM|(CREATE|ALTER|DROP|TRUNCATE)\\s+(TABLE|DATABASE)", 2);
            Matcher mat = pattern.matcher(input);
            if(mat.find()) {
                result = mat.replaceAll("");
            }

            return result;
        } else {
            return "";
        }
    }
}
