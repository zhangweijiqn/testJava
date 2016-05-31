package zwj.test.Preconditions;

/**
 * Created by zhangweijian on 2015/11/27.
 */
import org.apache.hadoop.thirdparty.guava.common.base.Preconditions;
public class TestPreconditions {
    public static void main(String[] args) {
        try {
            String testStr = null;
            Preconditions.checkArgument(!StringUtils.isEmpty(testStr), "id and name cannot be empty");//
        }catch (Exception e){
//            System.out.println("error:"+e);
            System.out.printf("%s",e);
        }
    }
}
