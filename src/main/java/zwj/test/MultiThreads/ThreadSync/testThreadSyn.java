package zwj.test.MultiThreads.ThreadSync;

/**
 * Created by zhangwj on 15-12-22.
 *
 * 多线程同步
 *
 */
public class testThreadSyn {
    public static void main(String[] args) throws InterruptedException {
        ThreadB b = new ThreadB();
        //启动计算线程
        b.start();
//        b.join();//可以代替下面的b.wait
        //线程A拥有b对象上的锁。线程为了调用wait()或notify()方法，该线程必须是那个对象锁的拥有者
        synchronized (b) {
            try {
                System.out.println("等待对象b完成计算。。。");
                //当前线程A等待
                b.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("b对象计算的总和是：" + b.total);
        }
    }

/**
 * 计算1+2+3 ... +100的和
 *
 */
public static class ThreadB extends Thread {
        int total;

        public void run() {
        synchronized (this) {
            for (int i = 0; i < 1000000; i++) {
                total += i;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //（完成计算了）唤醒在此对象监视器上等待的单个线程，在本例中线程A被唤醒
            notify();
            }
        }
    }

}