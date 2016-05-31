package zwj.test.TimeCount;

/**
 * Created by zhangwj on 16-1-11.
 */
public class TestTimeCount {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        while(true){
            Thread.sleep(2500);
            break;
        }
        long end = System.currentTimeMillis();
        System.out.println("total time: "+(end-start)/1000.0+"s");
    }
}
