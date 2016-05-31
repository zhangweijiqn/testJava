package zwj.test.Generic;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import zwj.test.Json.JSONUtils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwj on 15-12-23.
 */
public class testGeneric {
    public static void main(String[] args) throws Exception {
        List<String>aaa = new ArrayList<String>();
        for(int i=0;i<10;++i)
        {
            aaa.add(i+"th");
        }
        Gclass<List<String>> testG = new Gclass<List<String>>();
        testG.setData(aaa);
        testG.setMsg("success");
        testG.setSuccess(true);
        String testStr = JSONUtils.toJSON(testG);

        //使用Gson再解析出来
        Type type = new TypeToken<Gclass<List<String>>>() {}.getType();
        Gson gson = new Gson();
        Gclass<List<String>> testG2= gson.fromJson(testStr, type);
        List<String>aaa2 = testG2.getData();
        System.out.println(aaa2);
    }
}