package zwj.test.MultiThreads.Thread;

/**
 * Created by zhangwj on 15-12-22.
 */
public class RunnableThread implements Runnable{
    private String name;

    public RunnableThread(String name) {
        this.name = name;
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
//            for (long k = 0; k < 100000000; k++) ;
                System.out.println(name + ": " + i);
        }
    }

    public static void main(String[] args) {
        RunnableThread ds1 = new RunnableThread("阿三");
        RunnableThread ds2 = new RunnableThread("李四");

        Thread t1 = new Thread(ds1);
        Thread t2 = new Thread(ds2);

        t1.start();
        t2.start();
    }
}
