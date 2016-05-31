package zwj.test.JLineTest;

import jline.console.completer.Completer;

import java.util.List;

/**
 * Created by zhangwj on 15-12-6.
 * 实现自动补全功能
 */
public class MyCompleter implements Completer
{
    @Override
    public int complete(String buffer, int cursor, List candidates)
    {
//        buffer是当前用户输入的内容，cursor表示光标的位置，candidates表示你想补全的候选项。返回值很重要，表示你要再哪个位置补全你的内容
        System.out.println("buffer="+buffer+",cursor="+cursor);
        if(buffer.length() > 0 && cursor >0)
        {
            String substring = buffer.substring(0,cursor);
            if(substring.endsWith("he"))
            {
                candidates.add("hello");
                candidates.add("help");
                return cursor-2;
            }

        }
        return cursor;
    }
}