package zwj.test.MultiThreads.ThreadSync;

/**
 * Created by zhangwj on 16-3-9.
 *
 * volatile volatile 变量表示保证它必须是与主内存保持一致，它实际是变量的同步。保证线程每次使用前立即从主内存刷新，把变量声明为volatile类型后,编译器与运行时都会注意到这个变量是共享的.
 *
 * 本例中使用volatile仍不能够保证线程完全同步， volatile 并不完全是线程安全的.
 *
 * 每一个线程运行时都有一个线程栈， 线程栈保存了线程运行时候变量值信息。当线程访问某一个对象时候值的时候，首先通过对象的引用找到对应在堆内存的变量的值，然后把堆内存

 变量的具体值load到线程本地内存中，建立一个变量副本，之后线程就不再和对象在堆内存变量值有任何关系，而是直接修改副本变量的值，

 在修改完之后的某一个时刻（线程退出之前），自动把线程变量副本的值回写到对象在堆中变量。
 *
 * 对于volatile修饰的变量，jvm虚拟机只是保证从主内存加载到线程工作内存的值是最新的。因此读不需要加锁，写的时候需要加锁。
 *
 */

public class testVolatile {

    public volatile static int count = 0;

    public static void inc() {

        //这里延迟1毫秒，使得结果明显
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
        }

        count++;
    }

    public static void main(String[] args) throws InterruptedException {

        //同时启动1000个线程，去进行i++计算，看看实际结果

        for (int i = 0; i < 1000; i++) {
            new Thread(new Runnable() {
                public void run() {
                    testVolatile.inc();
                }
            }).start();
        }

        Thread.sleep(2500);    //可能发生1000个子线程还未执行完主线程就已经退出的情况，所以让主线程停止2s
        //这里每次运行的值都有可能不同,可能为1000
        System.out.println("运行结果:Counter.count=" + testVolatile.count); //结果不论count带（958）不带(963) volatile关键字，都不等于1000
    }
}