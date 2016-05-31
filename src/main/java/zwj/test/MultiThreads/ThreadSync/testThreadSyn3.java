package zwj.test.MultiThreads.ThreadSync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zhangwj on 16-3-9.
 *
 *  test Re'entrantLock
 *
 */
public class testThreadSyn3 {

    public static int count = 0;

    public static Lock lock = new ReentrantLock();  //锁要定义为多个线程共享的，即定义在方法外面。如果定义在方法里面，别的线程看不到线程内的锁

    public static void inc() {
        //这里延迟1毫秒，使得结果明显
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            // update object state
            count++;
        }
        finally {
            lock.unlock();
        }

    }

    public static void main(String[] args) throws InterruptedException {

        //同时启动1000个线程，去进行i++计算，看看实际结果

        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    testThreadSyn3.inc();
                }
            }).start();
        }

        Thread.sleep(2500);    //可能发生1000个子线程还未执行完主线程就已经退出的情况，所以让主线程停止2s
        //这里每次运行的值都有可能不同,可能为1000
        System.out.println("运行结果:Counter.count=" + testThreadSyn3.count); //结果不论count带（958）不带(963) volatile关键字，都不等于1000
    }
}

