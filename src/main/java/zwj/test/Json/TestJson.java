package zwj.test.Json;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by zhangweijian on 2015/11/24.
 */
public class TestJson {
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    String name="zhang";
    int age=20;
    public static void main(String[] args) throws Exception {
        TestJson testJson = new TestJson();
        String json = JSONUtils.toJSON(testJson);//����������getter��Ԫ�ز��ܱ�ת����Json�����
        System.out.println(json);

        Type type = new TypeToken<Map<String,Object>>() {}.getType();
        Gson gson = new Gson();
        Map<String,Object> m = gson.fromJson(json, type);

        System.out.println(m);
        if(m.get("age").equals(20)){
            System.out.println("true");
        }else{
            System.out.println(m.get("name"));
        }
    }
}
