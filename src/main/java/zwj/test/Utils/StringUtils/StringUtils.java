package zwj.test.Utils.StringUtils;

import java.util.Calendar;

public class StringUtils {
	
	public static int stringToInt(String str) {
		String[] temp = str.split("\\*");
		int j = 1;
		for (int i = 0; i < temp.length; i++) {
			j = j * Integer.parseInt(temp[i]);
		}
		return j;
	}

	public static boolean isEmpty(String str) {
		return str == null || str.trim().equals("");
	}

	/**
	 * 随即生成一个排序号
	 * 
	 * @return
	 */
	public static long randomOrder() {
		return Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * 把中文转成Unicode码
	 * 
	 * @param str
	 * @return
	 */
	public static String chinaToUnicode(String str) {
		String result = "";
		for (int i = 0; i < str.length(); i++) {
			int chr1 = (char) str.charAt(i);
			if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				result += "\\u" + Integer.toHexString(chr1);
			} else {
				result += str.charAt(i);
			}
		}
		return result;
	}

	/**
	 * 判断是否为中文字符
	 * 
	 * @param c
	 * @return
	 */
	public boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	public static String unicode2String(String unicodeStr) {
		StringBuffer sb = new StringBuffer();
		String str[] = unicodeStr.toUpperCase().split("U");
		for (int i = 0; i < str.length; i++) {
			if (str[i].equals(""))
				continue;
			char c = (char) Integer.parseInt(str[i].trim(), 16);
			sb.append(c);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String str =null;
		//isEmpty
		if(StringUtils.isEmpty(str)){
			System.out.println(str);
		}
		str="abcdef abc def gh";
		//substringAfter , substringAfterLast
		String substr = org.apache.commons.lang.StringUtils.substringAfter(str,"b");
		System.out.println(substr);

		//substringBeforeLast
		substr = org.apache.commons.lang.StringUtils.substringBeforeLast(str,"e");
		System.out.println(substr);

		//replace , split
		String lineSeparator = System.getProperty("line.separator", "/n");
		str = org.apache.commons.lang.StringUtils.replace(str," ",lineSeparator);
		String lines[] = str.split(lineSeparator);
		for(String s:lines) System.out.println(s);


	}
}
