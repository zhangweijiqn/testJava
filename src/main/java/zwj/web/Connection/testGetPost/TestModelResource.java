package zwj.web.Connection.testGetPost;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class TestModelResource {

    private static String managerUri = "http://192.168.177.77:8989";

    //Get请求
    public static void listDatabase(String userName) throws URISyntaxException {
        final URI uri = new URIBuilder(managerUri + "/ras/acl/listDatabases")
                .setParameter("userName", userName)
                .build();

        try {
            final HttpGet get = new HttpGet(uri);
            final String json = HttpManager.get(get);
            System.out.println(json);
        } catch (Exception e) {
        }
    }

    //Post请求
    public static void createDatabase(String userName, String databaseName) throws URISyntaxException {
        final List<NameValuePair> postPairs = new ArrayList<NameValuePair>();
        postPairs.add(new BasicNameValuePair("userName", userName));
        postPairs.add(new BasicNameValuePair("dbName", databaseName));
        postPairs.add(new BasicNameValuePair("actionType","create"));

        try {
            System.out.println(managerUri + "/ras/acl/createOrDropDbPermission");
            final HttpPost post = new HttpPost(managerUri + "/ras/acl/createOrDropDbPermission");
            post.setEntity(new UrlEncodedFormEntity(postPairs));
            final String json = HttpManager.post(post);
            System.out.println(json);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        String userName = "hadp";
        String databaseName = "abcde";

        listDatabase(userName);
        createDatabase(userName, databaseName);
        listDatabase(userName);

    }
}
