package zwj.test.callPython;

import org.python.core.PyFunction;
import org.python.core.PyString;
import org.python.util.PythonInterpreter;
import org.python.core.Py;
import org.python.core.PySystemState;
import zwj.test.Utils.shell.ShellResult;
import zwj.test.Utils.shell.ShellUtil;

import java.io.OutputStream;

/**
 * Created by zhangwj on 16-11-11.
 *  using java code to call python program, there are 2 methods:
 *  (1) 直接执行Python脚本代码
 *  (2) 执行python .py文件
 *  (3) 使用Runtime.getRuntime()执行脚本文件
 *  （4）使用os执行
 *  需要jython包
 */
public class testCallPython {

    public static void  testExecutePyCodes(){
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("print('this is a test!')");   ///执行python脚本
//        changePathByJava();
//        changPathByPython(interpreter);
        interpreter.execfile("src/main/java/zwj/test/callPython/input.py");   ///执行python文件，如果包含第三方包,调用不了
        String scrpt = "import sys;reload(sys);\n"
            + "from chatterbot import ChatBot\n"
            + "from chatterbot.trainers import ChatterBotCorpusTrainer\n"
            + "deepThought = ChatBot('deepThought')\n"
            + "deepThought.set_trainer(ChatterBotCorpusTrainer)\n"
            + "deepThought.train('chatterbot.corpus.chinese')\n";
        interpreter.exec(scrpt);
        PyFunction pyFunction = (PyFunction)interpreter.get("deepThought.get_response",PyFunction.class );  ///执行python函数
        System.out.println("Result: "+pyFunction.__call__(new PyString("你好")));
    }

    private static void changPathByPython(PythonInterpreter interpreter) {
        interpreter.exec("import sys");
        interpreter.exec("print sys.path");
        interpreter.exec("path = '/home/zhangwj/Applications/jython2.7.0/Lib'");
        interpreter.exec("sys.path.append(path)");
        interpreter.exec("print sys.path");
    }

    public static void changePathByJava(){
        PySystemState sys = Py.getSystemState();
        System.out.println(sys.path.toString());    // previous
        sys.path.add("/home/zhangwj/Applications/anaconda2/lib");
        System.out.println(sys.path.toString());   // later
    }

    public static void executePyFiles(){
        try {
            Process p = Runtime.getRuntime().exec("python src/main/java/zwj/test/callPython/input.py");
/*等待调用返回*/
            p.waitFor();
/*打印调用执行结果*/
            System.out.println(p.exitValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  static void executeByShell(){
        ShellResult ret = null;
        try {
            ret = ShellUtil.executeSync("python /home/zhangwj/MyProjects/testProjects/TestLanguageTech/Java_test/src/main/java/zwj/test/callPython/input.py", "my-shell-executor1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(ret);
    }

    public static void main(String[] args) {
//        executePyFiles();
        testExecutePyCodes();
//        executeByShell();
    }
}
