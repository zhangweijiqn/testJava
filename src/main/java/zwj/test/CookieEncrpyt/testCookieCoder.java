package zwj.test.CookieEncrpyt;

import com.google.common.collect.Maps;
import zwj.test.Json.JSONUtils;

import java.util.Map;

/**
 * Created by zhangwj on 16-2-14.
 * Java Cryptography Extension（JCE）是一组包，它们提供用于加密、密钥生成和协商以及 Message Authentication Code（MAC）算法的框架和实现
 * 更多加密查看EncryptUtils
 */
public class testCookieCoder {
    public static void main(String[] args) throws Exception {
        //prepare the data to be encrypted

        String key="omdLJagQq7finM4GTyUkfPpn7scQUCtv";//长度固定，私钥
        System.out.println(key.length());

        Map<String, Object> ret = Maps.newHashMap();
        ret.put("username", "aaa"); //将这些cookie参数放到一个json中进行加密
        ret.put("project_id", "12345");
        ret.put("project_name", "test");
        String json = JSONUtils.toJSON(ret).toString();


        CookieCipherTools cookieCipherTools = new CookieCipherTools();
        cookieCipherTools.setCharsetName("utf-8");//默认utf-8，也可以不设置
        String result = cookieCipherTools.encrypt(json, key);   //DES加密，返回的是一个串,这样可以使用一个cookieName来存储三个变量的内容了
        System.out.println(result);

        //decrypt
        json = cookieCipherTools.decrypt(result, key);
        System.out.println(json);//解析Json可以用Gson
    }
}
