/**
 * Created by zhangweijian on 2015/10/9.
 */
package zwj.test.collections;

import java.util.*;

public class Collections {
    /******    List接口：LinkedList实现为链表，ArrayList实现为数组，Vector是线程安全的数组   ******/
    public static int count=0;//java静态变量可以直接定义后初始化
    Collections(){
        ++count;
    }
    public static void main(String args[]){
        List<Integer>Nums = new ArrayList<Integer>();//List定义后要用new初始化
        // 如果没有指定参数，默认初始化大小为ten，在知道具体大小的情况下可以先传递一个初始size，即new ArrayList<Integer>(100)，可以直接查看ArrayList构造函数代码
        for (int i=0;i<10;++i){
            Nums.add(i);
        }
        List<Map<String,Object>> ret = new LinkedList<Map<String, Object>>();//String,List,Map,Object,LinkedList首字母均大写

        /*   hashMap存储的是（k,v)映射，hashMap本质上是一个数组，每个元素为一个链表（保存有冲突的元素），对k取hashcode映射到hash地址中，保存的v   */
        Map<String,Object>map = new  HashMap<String,Object>();//java中记得使用new 初始化
        for(Integer tem:Nums){              //遍历集合元素的写法
            map.put(tem + "", new Collections());//tem+""是默认将整型转换为String
        }
        ret.add(map);
        for(Map<String,Object>ds : ret){
            for(int i = 0 ;i< 10;++i){
                Collections numCount = (Collections) ds.get(i+"");//get方法要强制转换Collections
                System.out.println(numCount.count);//输出结果都是10，不同的Collections对象的count指向同一个内存地址
            }
        }

        /*  HashSet的实现是HashMap，只不过它是只存储的Key（不能重复）*/
        /*  系统采用 Hash 算法决定集合元素的存储位置 */
        Set<String> small= new HashSet<String>();
        small.add("abc");
        small.add("bcd");
        small.add("cde");

        //求两个子集的交集
        List<String> big = new ArrayList<String>();
        big.add("abc");
        big.add("abcd");
        big.add("bcd");
        big.add("abcde");
        //遍历集合交删除集合中满足条件的元素
        for(int i=0;i<big.size();++i){
            Boolean isExist = small.contains(big.get(i));
            System.out.println(isExist);
            if(isExist){
                removeElem(big, i);
                --i;
            }
        }
        for(String b:big) System.out.println(b);

    }

    private static <T> void removeElem(List<T> big, int i) {   //List类型是复杂类型，传递引用可以修改值，由于可以传递多种类型，这里使用泛型
        big.remove(i);
    }
}
