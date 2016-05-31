package zwj.test.JLineTest;

import jline.console.ConsoleReader;

import java.io.IOException;

/**
 * Created by zhangwj on 15-12-6.
 */
public class TestJLine {
    public static void RunLine()throws IOException {
        ConsoleReader reader = new ConsoleReader();

        //添加自动补全
        MyCompleter myCompleter = new MyCompleter();
        reader.addCompleter(myCompleter);

        String line = null;
        do
        {
            line = reader.readLine("input>");
            if(line != null)
            {
                //TODO
                reader.println("got: "+line);
            }
        }
        while(line!=null && !line.equals("exit"));
    }
    public static void main(String[] args) throws IOException {
        RunLine();
    }
}
