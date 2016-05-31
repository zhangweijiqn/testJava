package zwj.test.Iterator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangweijian on 2015/11/20.
 */

public class TestIterator<t> implements Iterable<t> {
    private t[] array = null;

    public TestIterator(t[] t) {
        this.array = t;
    }

    public Iterator<t> iterator() {
        // TODO Auto-generated method stub
        return new Iterator<t>() {
            private Integer index = 0;

            public boolean hasNext() {
                // TODO Auto-generated method stub
                return index!=array.length;
            }
            public t next() {
                return array[index++];
            }
        };
    }

    public static void main(String[] args) {
        Integer aa[]= new Integer[10];
        //���� Iterable �ӿ�ʵ��
        for(int i=0;i<10;++i)aa[i]=i+1;
        TestIterator<Integer>testIterator = new TestIterator<Integer>(aa);
        for(Integer a:testIterator){
            System.out.println(a);
        }
        //���� Iterator
        List<Integer> l = new ArrayList<Integer>();
        for(int i=0;i<10;++i)l.add(i*10);
        for (Iterator iter = l.iterator(); iter.hasNext();) {
            System.out.println(iter.next());
        }
    }
}