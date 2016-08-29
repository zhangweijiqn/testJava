package zwj.test.MultiThreads.ThreadPool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhangwj on 16-3-9.
 *
 *  线程池：如果并发的线程数量很多，并且每个线程都是执行一个时间很短的任务就结束了，这样频繁创建线程就会大大降低系统的效率，因为频繁创建线程和销毁线程需要时间。
 *
 */


class MyTask implements Runnable {
    private int taskNum;

    public MyTask(int num) {
        this.taskNum = num;
    }

    public void run() {
        System.out.println("正在执行task "+taskNum);
        try {
            Thread.currentThread().sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task "+taskNum+"执行完毕");
    }
}


public class testThreadPool {
    public static void main(String[] args) throws InterruptedException {
        //ThreadPoolExecutor,ScheduledThreadPoolExecutor
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(15));
        //SynchronousQueue阻塞队列，LinkedBlockingQueue队列

        for(int i=0;i<15;i++){
            MyTask myTask = new MyTask(i+1);
            executor.execute(myTask);
            System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
        }
        Thread.sleep(9000);
        System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());

        //继续增加线程，队列中的数目会一直保持5，之前保存的线程会被替换
        for(int i=0;i<25;i++){
            MyTask myTask = new MyTask(i+16);
            executor.execute(myTask);
            System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
        }

        executor.shutdown();

        //从结果中可以看出执行过程为：
        /*
        *（1） 线程池创建后，大小为0
        *（2） 启动线程，数目到达coreSize后，随后来的线程加入到Queue中
        *（3） 当Queue中的数据达到最大后，开始继续向线程池中增加线程，知道达到设定的maxSize
        *（4） 随后增加线程抛出异常。
        *
        * */
    }
}
