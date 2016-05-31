package zwj.test.MultiThreads.FutureTaskCallable;

import java.util.concurrent.*;

/**
 * Created by zhangwj on 16-1-13.
 */
public class testFutureTask {

    static class Task implements Callable<Integer>{
        //Java 5以后创建线程除了继承Thread/实现Runnable接口还有第三种方式：实现Callable接口，该接口中的call方法可以在线程执行结束时产生一个返回值
        public Integer call() throws Exception {
            System.out.println("子线程2在进行计算");
            Thread.sleep(3000);
            int sum = 0;
            for(int i=0;i<100;i++)
                sum += i;
            return sum;
        }
    }

    public static void method2(){
        //第一种方式
        ExecutorService executor = Executors.newCachedThreadPool();//创建线程池,效率较高的可以使用Executors.newFixedThreadPool(10);
        Task task = new Task();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
        executor.submit(futureTask);
        executor.shutdown();

        //第二种方式，注意这种方式和第一种方式效果是类似的，只不过一个使用的是ExecutorService（线程池的方式），一个使用的是Thread
        /*Task task = new Task();
        FutureTask<Integer> futureTask = new FutureTask<Integer>(task);
        Thread thread = new Thread(futureTask);
        thread.start();*/

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        System.out.println("2线程在执行任务");

        try {
            System.out.println("task运行结果"+futureTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("所有任务执行完毕");
    }

    public static void method1() throws ExecutionException, InterruptedException {
        //异步开启子线程
        FutureTask futureTask = null;
        futureTask = new FutureTask(new Callable<String>() {    //匿名内部类
            public String call() throws InterruptedException {
                System.out.println("子线程1在进行计算");
                Thread.sleep(10000);
                int sum = 0;
                for(int i=0;i<100;i++)
                    sum += i;
                return "The result is:"+sum;
            }
        });
        (new Thread(futureTask)).start();//在start后，futureTask会调用run，run使用的是上面床底过去的call实现方法，传给FutureTask的是Callable<T>泛型接口

        //主线程
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println("1线程在执行任务");

        if(futureTask != null) {
            String result = (String) futureTask.get();
            futureTask = null;
            if(result != null) {
                System.out.println(result);
            }
        }
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        method1();
        method2();
    }
}
