package zwj.test.SecurityManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.AccessControlException;
import java.security.Permission;

/**
 * Created by zhangweijian on 2015/12/3.
 */
public class TestSecurityManager {

    static class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println("进入线程"+Thread.currentThread().getName());
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                // TODO: handle exception
            }
            System.out.println("线程"+Thread.currentThread().getName()+"执行完毕");
        }
    }

    private static class MySecurityManager extends SecurityManager {
        @Override
        public void checkAccess(ThreadGroup t) {//checkAccess与线程相关的检查，包括Thread，ThreadGroup
            super.checkAccess(t);
            System.out.println("checkAccess ThreadGroup called!");
//            throw new SecurityException("Not allowed.");
        }
        @Override
        public void checkAccess(Thread t) {
            super.checkAccess(t);
            System.out.println("checkAccess Thread called!");
//            throw new SecurityException("Not allowed.");
        }
        @Override
        public void checkRead(String file) {    //拦截Read操作，对其进行处理
            System.out.println("checkRead file called!");
            if ("java.policy".contains(file)) {     //如果文件是java.policy，抛出异常
                throw new AccessControlException("cannot read file:"+ file);
            }
            //-Djava.security.manager  -Djava.security.policy=src\main.zwj\test\SecurityManager\policy
            super.checkRead(file);      //交给上层策略检查
        }
        @Override
        public void checkPermission(Permission perm) {//最终都会调用到checkPermission
            System.out.println("checkPermission  called!");
            super.checkPermission(perm);
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        /*ClassLoader current = Thread.currentThread().getContextClassLoader() ;
        System.out.println(current instanceof URLClassLoader);
        System.out.println(current.getClass().getName());*/

//install
        System.setSecurityManager(new MySecurityManager());//启动安全管理
//read
        System.out.println(System.getProperty("java.version"));//触发checkgetPropertyAccess,checkPermission
        /*
        触发checkPropertyAccess,checkPermission操作，本地策略-->全局策略，
        默认配置文件在java jdk中lib/security/java.policy文件中，通过grant permission来设置的允许可以read的文件
        */
//        ThreadGroup tg = new ThreadGroup("example");
//        tg.setMaxPriority(5);
        Thread thread = new Thread("example");//触发checkAccess(ThreadGroup),触发checkAccess(Thread)
        thread.setPriority(5);//触发checkAccess,checkPermission
        MyThread mt = new MyThread();
        mt.setPriority(5);//触发checkAccess,checkPermission
        mt.run();
        InputStream in = new FileInputStream(new File("C:/temp/abc.txt"));//触发checkRead，checkPermission操作
//uninstall
        SecurityManager sm = System.getSecurityManager();
        if (sm != null) {
            System.setSecurityManager(null);
        }

    }
}
