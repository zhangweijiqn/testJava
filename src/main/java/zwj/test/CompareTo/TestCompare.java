package zwj.test.CompareTo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by zhangweijian on 2015/11/20.
 */
public class TestCompare {
        //�ر�дComparator,����User��id��User��������
        private static final Comparator<User> COMPARATOR = new Comparator<User>() {
            public int compare(User o1, User o2) {
                if(o1.getId()>o2.getId()){ return -1;}
                else if(o1.getId()==o2.getId()){ return 0;}
                else if(o1.getId()<o2.getId()) { return 1;}

                return o1.compareTo(o2);//��������User���compareTo�����Ƚ���������
            }
        };

        public static void main(String[] args) {
            ArrayList<User> student = new ArrayList<User>();
            User user1 = new User(1,"yueliming");
            User user2 = new User(2,"yueliming");

            student.add(user2);
            student.add(user1);

            //1��ʹ��Userʵ�ֵ�compareTo���������бȽ�
            Collections.sort(student);
            for(int i=0;i<student.size();i++){
                System.out.println(student.get(i).getId());
            }
            //2��ʹ��COMPARATOR����Ϊ�Ƚ���
            Collections.sort(student, COMPARATOR);//������д�õ�Comparator��student��������
            for(int i=0;i<student.size();i++){
                System.out.println(student.get(i).getId());
            }
        }
}
