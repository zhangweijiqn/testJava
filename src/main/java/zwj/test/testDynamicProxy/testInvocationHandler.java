package zwj.test.testDynamicProxy;
/**
 * Created by zhangwj on 16-4-7.
 * AOPTest，java动态代理
 * 代理模式的作用是：为其他对象提供一种代理以控制对这个对象的访问.
 * 好处：（1）可以省去new具体类的过程；（2）在某些情况下，一个客户不想或者不能直接引用另一个对象，而代理对象可以在客户端和目标对象之间起到中介的作用(类比火车票代售点)。
 * 博文说明参考：http://www.cnblogs.com/xiaoluo501395377/p/3383130.html
 */

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.InvocationHandler;


public class testInvocationHandler {

    /*   动作   */
    interface Dog {
        void info();
        void run();
    }

    static class GunDog implements Dog {

        public void info() {
            System.out.println("I am a gundog!");
        }

        public void run() {
            System.out.println("I run fast!");
        }

    }

    /*  切面    */
    static class Authority {
        //第一个拦截方法
        public void first() {
            System.out.println("------第一个切面方法------");
        }
        //第二个拦截方法
        public void last() {
            System.out.println("------最后一个切面方法------");
        }
    }

    /*   AOP   */
    static class MyInvocationHandler implements InvocationHandler {
        //需要被代理的对象
        private Object target;

        public void setTarget(Object target) {
            this.target = target;
        }
        //执行动态代理对象的所有方法时，都会被替换成执行如下的invoke方法
        public Object invoke(Object proxy, Method method, Object[] args)
                throws Exception {//
            Authority authority = new Authority();
            //执行权限对象中的in方法
            authority.first();
            System.out.println("正在执行的方法：" + method);
            //if (args != null) {
            //System.out.print("执行该方法时传入的实参：");
            //for (Object val : args) {
            //System.out.println(val);
            //}
            //} else {
            //System.out.println("调用该方法没有实参！");
            //}
            System.out.print("执行结果：");
            Object result = method.invoke(target, args);
            //执行权限对象中的out方法
            authority.last();
            return result;
        }
    }

    static class MyProxyFactory {
            //为制定的target生成动态代理对象
        public static Object getProxy(Object target) throws Exception {
            //创建一个MyInvocationHandler对象
            MyInvocationHandler handler = new MyInvocationHandler();
            //为MyInvocationHandler设置target对象
            handler.setTarget(target);
            //创建并返回一个动态代理
            return Proxy.newProxyInstance(target.getClass().getClassLoader(),
                    target.getClass().getInterfaces(), handler);
        }
    }

    public static void main(String[] args) throws Exception {
        //创建一个原始的GunDog对象，作为target
        Dog target = new GunDog();
        //以制定的target来创建动态代理对象
        Dog dog = (Dog) MyProxyFactory.getProxy(target);
        dog.info();//调用的时候会调用代理类的invoke方法
        dog.run();
    }
}



