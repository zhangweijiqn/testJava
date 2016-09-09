package zwj.web.Connection;
import zwj.test.Json.JSONUtils;

import java.io.DataInputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 *
 * Connection��ʾһ��HTTP������ʹ�õ�����
 *
 * <p>
 * һ��Connection������һ��HTTP���������������ͬ, �����������Connection���ٿ��á�<br />
 *
 * һ��HTTP���������¹���: <br />
 * 1. ͨ��{ connect(Request req)}��������HTTP����, HTTPͷ�ᷢ��<br />
 * 2. ͨ��{ getOutputStream()}��������������HTTP body���� <br />
 * 3. { getResponse()}���HTTP��Ӧ������״̬��, HTTPͷ <br />
 * 4. { getInputStream()}��ȡHTTP��Ӧ��body�������� <br />
 * 5.{ disconnect()}����������ͷ���Դ
 * </p>
 *
 * Created by zhangweijian on 2015/11/30.
 */
public class testHttpURLConnection {
    public static void main(String[] args) {
        String validateURL = "http://bds.jcloud.com/api/u/loadProjects?userName=datajingdo_m";
        java.net.HttpURLConnection conn = null;
        try {
            URL url = new URL(validateURL);
            conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");

            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.connect();



            DataInputStream dis = new DataInputStream(conn.getInputStream());

            if (conn.getResponseCode() != java.net.HttpURLConnection.HTTP_OK) {
                System.out.println("!!!");
//                return  false;
            }
            else{
                System.out.println(conn.getResponseMessage());
                OutputStream outputStream = conn.getOutputStream();
                Map<String, List<String>> fields = conn.getHeaderFields();
                System.out.println(JSONUtils.toJSON(fields));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("");
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
