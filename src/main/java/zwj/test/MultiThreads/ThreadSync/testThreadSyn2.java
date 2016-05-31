package zwj.test.MultiThreads.ThreadSync;

/**
 * Created by zhangwj on 16-3-9.
 */

public class testThreadSyn2 {

    public static int count = 0;

    public static void inc() {

        //这里延迟1毫秒，使得结果明显
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }
        count++;
    }

    public static synchronized void SyncInc(){      // synchronized 方法
        //这里延迟1毫秒，使得结果明显
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }
        count++;
    }

    public static void SyncInc2(){
        synchronized (testThreadSyn2.class) {           // synchronized 代码块

            //synchronized()要传入当前class，本方法为static，不能使用this
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        //同时启动1000个线程，去进行i++计算，看看实际结果

        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    testThreadSyn2.inc();          //调用该方法线程间没有同步，执行结果不正确
//                    testThreadSyn2.SyncInc();   //该方法线程进行了同步，执行结果正确
//                    SyncInc2();                     //该方法线程进行了同步，执行结果正确
                }
            }).start();
        }

        Thread.sleep(2500);    //可能发生1000个子线程还未执行完主线程就已经退出的情况，所以让主线程停止2s
        //这里每次运行的值都有可能不同,可能为1000
        System.out.println("运行结果:Counter.count=" + testThreadSyn2.count); //结果不论count带（958）不带(963) volatile关键字，都不等于1000
    }
}