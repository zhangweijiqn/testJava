package zwj.test.Reflection;

/**
 * Created by zhangwj on 15-12-17.
 */
public class Person {
    private String name;

    public Person() {
    }

    private Integer id;

    public static int getCount() {
        return count;
    }

    public static void setCount(Integer count) {
        Person.count = count;
    }

    private static int count = 0;

    public Person(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }
}
