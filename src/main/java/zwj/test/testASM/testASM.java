package zwj.test.testASM;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangwj on 16-4-6.
 * ASM是一个java字节码操纵框架，它能被用来动态生成类或者增强既有类的功能。ASM 可以直接产生二进制 class 文件，也可以在类被加载入 Java 虚拟机之前动态改变类行为。
 * 测试对jar包进行安全检查
 * spring aop两种实现：cglib（ASM）和 InvocationHandler（java动态代理）
 */
public class testASM {
    public static void main(String[] args) throws Exception {
        FunctionJarSandboxChecker functionJarSandboxChecker = new FunctionJarSandboxChecker();

        ClassLoader classLoader = testASM.class.getClassLoader();
        //ClassLoader主要对类的请求提供服务，当JVM需要某类时，它根据名称向ClassLoader要求这个类，然后由ClassLoader返回 这个类的class对象。
        //ClassLoader负责载入系统的所有Resources（Class，文件，来自网络的字节流 等），通过ClassLoader从而将资源载入JVM，每个class都有一个reference，指向自己的ClassLoader。
        List<String> files = new ArrayList<String>();
        files.add("src/main/Resources/java_test-1.0-SNAPSHOT.jar");

        Set<String> result = functionJarSandboxChecker.check(classLoader,files);//返回的是jar包中不允许调用的方法
        //Set<String> result = functionJarSandboxChecker.check(classLoader,new File("src/main/Resources/java_test-1.0-SNAPSHOT.jar"));

        System.out.println(result.size());
        for (String str : result) {
            System.out.println(str);
        }
    }
}
