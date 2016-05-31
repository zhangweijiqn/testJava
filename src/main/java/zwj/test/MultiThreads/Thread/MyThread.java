package zwj.test.MultiThreads.Thread;

/**
 * 一个计数器，计数到100，在每个数字之间暂停1秒，每隔10个数字输出一个字符串
 */
public class MyThread extends Thread{
    public void run() {
        for (int i = 0; i < 100; i++) {
            if ((i) % 10 == 0) {
                System.out.println("-------" + i);
            }
            System.out.print(i);
            try {
                Thread.sleep(100);
                System.out.print("    线程睡眠1毫秒！\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        new MyThread().start();      //需要先开启子线程

        for(int i=0;i<100;++i){     //主线程会和子线程同时执行
            System.out.println("main thread: "+i);
            Thread.sleep(100);
        }
    }
}
