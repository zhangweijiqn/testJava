package zwj.test.JsonArray;

/**
 * Created by zhangweijian on 2015/12/1.
 */
import java.util.ArrayList;
import java.util.List;
import net.sf.json.JSONArray;

public class TestJsonArray {
    public static void main(String[] args) {
        System.out.println(collectionToJsonString());
        jsonStringToCollection(collectionToJsonString());
    }

    /**
     * 将Java集合转换为Json字符串
     *
     * @author 高焕杰
     */
    static String collectionToJsonString(){
        List<User> userList = new ArrayList<User>();
        userList.add(new User(1, "张三", "男"));
        userList.add(new User(2, "李四", "女"));
        userList.add(new User(3, "王五", "男"));
        JSONArray jsonArray = JSONArray.fromObject(userList);
        return jsonArray.toString();
    }

    /**
     * 将Json字符串转换为Java集合
     *
     * @author 高焕杰
     */
    static void jsonStringToCollection(String jsonString){
        JSONArray jsonArray = JSONArray.fromObject(jsonString);
        @SuppressWarnings("unchecked")
        List<User> userList = (List<User>)JSONArray.toCollection(jsonArray, User.class);
        for (User user : userList) {
            System.err.println(user);
        }
    }
}
