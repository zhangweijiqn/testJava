package zwj.hadoop.testHive.testUDF;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * Created by zhangwj on 16-3-8.
 */
public final class testUDF extends UDF{

    public Integer evaluate(Integer a, Integer b){
        if(null ==a || null==b){
            return null;
        }
        return a+b;
    }
    public Double evaluate(Double a, Double b){
        if(null==a||null==b){
            return null;
        }
        return a+b;
    }
    public Integer evaluate(Integer ...a){
        int total=0;
        for(Integer tmp:a){
            if(tmp!=null){
                total+=tmp;
            }
        }
        return total;
    }

    public static void main(String[] args) {

        /*
        *   hive> add jar path/testUDF.jar
            hive> create temporary function add_example as 'zwj.hadoop.Hive.testUDF.testUDF';    //创建临时function，永久创建去掉temporary
            hive> show functions;
            hive> select add_example(count(1),count(2)) from employees;
            hive>DROP TEMPORARY FUNCTION add_example;
        * */

    }
}
