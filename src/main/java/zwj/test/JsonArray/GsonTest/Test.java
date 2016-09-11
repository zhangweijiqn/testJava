package zwj.test.JsonArray.GsonTest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import zwj.test.JsonArray.GsonTest.Type.CellData;
import zwj.test.JsonArray.GsonTest.Type.SQLResultSet;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangwj on 15-12-15.
 */
public class Test
{
    public class JsonTest {
        public void testJson() {
            Type type = new TypeToken<List<SQLResultSet>>() {}.getType();
            Gson gson = new Gson();
            String jsonString = "[{\"orgSQL\":null,\"sql\":null,\"type\":\"QUERY\",\"cost\":197,\"message\":null,\"total\":0,\"metaCols\":[{\"id\":\"v73.v71_1\",\"field\":\"v73.v71_1\",\"title\":\"v73.v71\",\"type\":\"_STRING\"}],\"result\":[[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"1\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}],[{\"field\":\"v73.v71_1\",\"value\":\"0\"}]],\"query\":true}]\n";
            List<SQLResultSet> sqlResultSet = gson.fromJson(jsonString, type);
            List<List<CellData>> result = sqlResultSet.get(0).getResult();
            for(int i=0;i<result.size();++i){
                List<CellData> cellDatas = (List<CellData>)result.get(i);
                for(int j=0;j<cellDatas.size();++j){
                    CellData cellData = (CellData)cellDatas.get(j);
                    System.out.println(cellData.getField()+"-->"+cellData.getValue());
                }
            }
        }

    }
    public static void testToJsonString(){
        Gson gson = new Gson();
        List<String> a= new ArrayList<String>(10);
        for(int i=0;i<10;++i)a.add(i+"");
        String jsonString = gson.toJson(a);
        System.out.println(jsonString);
    }

    public static void main(String[] args) {
        testToJsonString();
    }
}
