package zwj.test.Iterator;

/**
 * Created by zhangweijian on 2015/10/10.
 */
abstract  class X<T>{   //���ͣ�Generics
    abstract T doIt();
        }

public class test extends X<Object> {

    @Override
    Integer  doIt() {
        return null;
    }
}
