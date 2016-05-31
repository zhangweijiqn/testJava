package zwj.web.Connection.testGetPost;

/*
* 需要配置pom：
*       <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpclient</artifactId>
            <version>4.5.1</version>
        </dependency>

        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpmime</artifactId>
            <version>4.5.1</version>
        </dependency>

        <dependency>
            <groupId>org.apache.httpcomponents</groupId>
            <artifactId>httpcore</artifactId>
            <version>4.4.4</version>
            <scope>compile</scope>
        </dependency>
* */

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpClient {

    public static String requireHttpPost(URI uri, List<NameValuePair> postPairs) {
        try {
            final HttpPost post = new HttpPost(uri);
            post.setEntity(new UrlEncodedFormEntity(postPairs, StandardCharsets.UTF_8));
            final String json = HttpManager.post(post);
            return json;
        }
        catch (Exception e) {
            e.printStackTrace();
//            throw new Exception(e.getMessage(), e);
        }
        return null;
    }

    public static String requireHttpGet(URI uri) {
        try {
            final HttpGet get = new HttpGet(uri);
            final String json = HttpManager.get(get);
            return json;
        } catch (Exception e) {
            e.printStackTrace();
//            throw new Exception(e.getMessage(), e);
        }
        return null;
    }
}
