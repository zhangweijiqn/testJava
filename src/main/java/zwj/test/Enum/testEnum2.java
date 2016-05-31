package zwj.test.Enum;

/**
 * Created by zhangwj on 16-1-29.
 * enum枚举中添加新方法
 */
enum Color {
    RED("红色", 1), GREEN("绿色", 2), BLANK("白色", 3), YELLO("黄色", 4);
    //必须在enum实例序列的最后添加一个分号，自定的方法要有相应的变量和构造方法

    // 成员变量
    private String name;    //红色，绿色，白色，黄色
    private int index;      //1,2,3,4

    // 构造方法
    private Color(String name, int index) {
        this.name = name;
        this.index = index;
    }

    //普通方法,通过index获取enum
    public static Color getName(int index) {
        for (Color c : Color.values()) {
            if (c.getIndex() == index) {
                return c;//可以返回的有c,c.name,c.index三种均可
            }
        }
        return null;
    }
    //通过name获取enum
    public static String getName(String name) {
        for (Color c : Color.values()) {
            if (c.getName().equals(name)) {
                return c.name;
            }
        }
        return null;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}

public class testEnum2 {
    public static void main(String[] args) {
        System.out.println(Color.getName(1));
        System.out.println(Color.getName("红色"));
    }
}
