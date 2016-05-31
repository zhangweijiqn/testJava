package zwj.test.CompareTo;

/**
 * Created by zhangweijian on 2015/11/20.
 */
public class User implements Comparable<Object>{
    int id;
    String name;

    public User(int id,String name){
        this.id = id;
        this.name = name;
    }
    /*
     * Getters and Setters
    */
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(Object o) {
        if(this ==o){
            return 0;
        }
        else if (o!=null && o instanceof User) {
            User u = (User) o;
            if(id<=u.id){
                return -1;
            }else{
                return 1;
            }
        }else{
            return -1;
        }
    }
}
