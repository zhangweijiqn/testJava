package zwj.test;

/*
    将如下信息用代码表示,参考:http://www.lai18.com/content/6223597.html
    "国庆小长假要来了，说出你最想去的地方，现在去不了也没关系，说不定以后就实现了呢？"
*/
public class ComingNationalDay {

    private String placeWant; // the place you want to go
    private String placeReal; // the real place you will go

    public ComingNationalDay(String pWant, String pReal) {
        System.out.printf("national day is coming...");
        placeWant = pWant;
        placeReal = pReal;
    }

    public static void main(String[] args) {

        String placeWant = "XXX";  //the place you want to go
        String placeReal = "YYY";  //the place you will go
        ComingNationalDay cnd = new ComingNationalDay(placeWant, placeReal);
        cnd.test();

    }

    public String waitForSomeDay() {
        return "Maybe next time!";
    }

    public String go() {
        return "have fun!";
    }

    public String test() {
        if (!placeReal.equals(placeWant)) {
            return waitForSomeDay();
        } else {
            return go();
        }
    }
}


