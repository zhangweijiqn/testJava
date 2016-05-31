package zwj.test.Enum;

/**
 * Created by zhangwj on 16-1-29.
 * enum常量基本用法
 */
enum Signal {   //不能定义为public,一个文件内只能有一个public
    RED, GREEN, BLANK, YELLOW
}

public class testEnum1 {


        public static void change(Signal color) {
            switch (color) {
                case RED:
                    color = Signal.GREEN;
                    System.out.println("红色");
                    break;
                case YELLOW:
                    color = Signal.RED;
                    System.out.println("黄色");
                    break;
                case GREEN:
                    color = Signal.YELLOW;
                    System.out.println("绿色");
                    break;
            }
        }

    public static void main(String[] args) {
        for (Signal e : Signal.values()) {
            System.out.println(e.toString());
        }
        Signal color = Signal.RED;
        change(color);
    }
}
