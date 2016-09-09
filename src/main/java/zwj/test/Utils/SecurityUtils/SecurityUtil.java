package zwj.test.Utils.SecurityUtils;

import org.apache.commons.lang.StringEscapeUtils;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Created by yuanshuaiming on 2015/11/12.
 * 调用编码或过滤方法对危险参数进行处理
 * 输入的敏感字符过滤 输出的时候编码
 */
public class SecurityUtil {

    /**
     * 用于过滤非法的字符,及处理字符中的特殊字符,
     * 防止SQL注入等安全缺陷.
     *
     * @param value
     * @return
     */
    public static String filterSQLJavaScriptHtmlValue(String value) {
        return StringEscapeUtils.escapeHtml(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeSql(value)));
    }

    public static String filterSQLValue(String value) {
        return StringEscapeUtils.escapeSql(value);
    }

    //以下提供5种编码函数，支持5种输出环境

    /**
     * 用户提交数据输出到html
     *
     * @param dirtyData
     * @return
     */
    public static String encodeValueForHTML(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
//        String cleanData = securityEncoder.encodeForHTML(dirtyData);
//        return cleanData;
        return "";
    }

    /**
     * 用户提交数据输出到html标签属性内部
     *
     * @param dirtyData
     * @return
     */
    public static String encodeValueForHTMLAttribute(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
//        String cleanData = securityEncoder.encodeForHTMLAttribute(dirtyData);
//        return cleanData;
        return "";
    }

    /**
     * 用户提交数据输出到JavasSript
     *
     * @param dirtyData
     * @return
     */
    public static String encodeValueForJavasSript(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
//        String cleanData = securityEncoder.encodeForJavaScript(dirtyData);
//        return cleanData;
        return "";
    }

    /**
     * 用户提交数据输出到CSS
     *
     * @param dirtyData
     * @return
     */
    public static String encodeValueForCSS(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
//        String cleanData = securityEncoder.encodeForCSS(dirtyData);
//        return cleanData;
        return "";
    }

    /**
     * 用户提交数据输出到URL
     *
     * @param dirtyData
     * @return
     */
    public static String encodeValueForURL(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
//        String cleanData = securityEncoder.encodeForURL(dirtyData);
//        return cleanData;
        return "";
    }
    //提供4种过滤函数

    /**
     * json回调函数名过滤
     *
     * @param dirtyData
     * @return
     */
    public static String filterValueForJsonCallback(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
        String cleanData = securityEncoder.jsonFilter(dirtyData);
        return cleanData;
    }

    /**
     * 通用get请求参数过滤
     *
     * @param dirtyData
     * @return
     */
    public static String filterValueForGetParameter(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
        String cleanData = securityEncoder.filterForGetRequest(dirtyData);
        return cleanData;
    }

    /**
     * 通用post请求参数过滤
     *
     * @param dirtyData
     * @return
     */
    public static String filterValueForPostParameter(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
        String cleanData = securityEncoder.filterForPostRequest(dirtyData);
        return cleanData;
    }

    /**
     * 通用cookie过滤
     *
     * @param dirtyData
     * @return
     */
    public static String filterValueForCookie(String dirtyData) {
        SecurityEncoder securityEncoder = new SecurityEncoder();
        String cleanData = securityEncoder.filterForPostRequest(dirtyData);
        return cleanData;
    }
    /**
     * CSRF漏洞之验证referer
     *
     * @param referer
     * @return
     */
    public static boolean validCsrfAddress(String referer) {
//        String referer=request.getHeader("Referer");
        String[] whiteList = {"localhost", "dcs.jcloud.com"};
        if (org.apache.commons.lang3.StringUtils.isEmpty(referer)) {
            return false;
        }
        if (referer.contains("?")) {
            referer = referer.substring(0, referer.indexOf("?"));
        }
        URI referUri = null;
        try {
            referUri = new URI(referer);
        } catch (URISyntaxException e) {
            return false;
        }
        String domain = referUri.getHost().toLowerCase();

        for (int i = 0; i < whiteList.length; i++) {
            if (whiteList[i].toLowerCase().equals(domain)) {
                return true;
            }
        }
        return false;
    }
}
