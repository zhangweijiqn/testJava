package zwj.test.UserDefException;

import zwj.test.Preconditions.StringUtils;

/**
 * Created by zhangwj on 15-12-4.
 */
public class UserDefException {

    public static void test(String str)throws OdpsException{
        try{
            if(StringUtils.isEmpty(str))
                throw new OdpsException("Response is null.");
        }catch (OdpsException e) {
            System.out.print(e);
        }
    }
    public static void testE(){
        try{
            String[] aaa=null;
            System.out.println(aaa[1]);

        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getStackTrace());
        }
    }
    public static void main(String[] args) throws OdpsException {
        testE();
    }
}
