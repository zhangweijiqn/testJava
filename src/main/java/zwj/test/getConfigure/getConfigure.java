/**
 * Created by zhangweijian on 2015/10/9.
 */
package zwj.test.getConfigure;

import java.util.ResourceBundle;

public class getConfigure {
    private static ResourceBundle resourceBundle = ResourceBundle.getBundle("config");
    public  static  void main(String args[]){
        String name = resourceBundle.getString("name");
        System.out.println(name);
    }
}
