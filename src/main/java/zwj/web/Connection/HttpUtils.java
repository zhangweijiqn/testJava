package zwj.web.Connection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zwj.test.Json.JSONUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    public static String httpPostHandler(String url, List<BasicNameValuePair> nvps) {
        String msg = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            // obtain a http post request object
            HttpPost postRequest = new HttpPost(url);
            //postRequest.setHeader("Accept", "application/json");
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
            // post 参数 传递
            LOG.info("post url is : {}, content is : {}", url, JSONUtils.toJSON(nvps));
            postRequest.setEntity(new UrlEncodedFormEntity(nvps, StandardCharsets.UTF_8)); // 设置参数给Post

            HttpResponse response = client.execute(postRequest);
            HttpEntity entity = response.getEntity();
            LOG.info(response.getStatusLine().toString());
            if (entity != null) {
                LOG.info("Response content length: " + entity.getContentLength());
            }
            // 显示结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {

                sb.append(line);
            }
            if (entity != null) {
                EntityUtils.consume(entity);
            }
            msg = sb.toString();

        } catch (Exception e) {

            LOG.error("gettableContent error ", e);
            return msg;

        }
        return msg;
    }


    public static String httpGetHandler(String url) {
        String msg = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            // obtain a http post request object
            HttpGet getRequest = new HttpGet(url);
            getRequest.setHeader("Accept", "application/json");
            LOG.info("get url is {}", url);
            HttpResponse response = client.execute(getRequest);
            HttpEntity entity = response.getEntity();
            LOG.info(response.getStatusLine().toString());
            if (entity != null) {
                LOG.info("Response content length: " + entity.getContentLength());
            }
            // 显示结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            if (entity != null) {
                EntityUtils.consume(entity);
            }
            msg = sb.toString();

        } catch (IOException e) {

            LOG.error("httpGetHandler error ", e);
            return msg;

        }
        return msg;
    }

    public static String httpPostStringHandler(String url, String content) {
        String msg = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            // obtain a http post request object
            HttpPost postRequest = new HttpPost(url);
            postRequest.setHeader("Accept", "application/json");

            // create an http param containing summary of article
            LOG.info("post url is : {}, content is : {}", url, content);
            postRequest.setEntity(new StringEntity(content));
            // obtain the response
            HttpResponse response = client.execute(postRequest);
            HttpEntity entity = response.getEntity();
            LOG.info(response.getStatusLine().toString());
            if (entity != null) {
                LOG.info("Response content length: " + entity.getContentLength());
            }
            // 显示结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {

                sb.append(line);
            }
            if (entity != null) {
                EntityUtils.consume(entity);
            }
            msg = sb.toString();
        } catch (IOException e) {
            LOG.error("postStringHandler error ", e);
            return msg;
        }
        return msg;
    }

    // 自定义 HttpDelete 方法，自带的HttpDelete方法不能设置参数。参考：http://www.tuicool.com/articles/BjmQ7fU
    @NotThreadSafe
    static class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
        public static final String METHOD_NAME = "DELETE";
        public String getMethod() { return METHOD_NAME; }

        public HttpDeleteWithBody(final String uri) {
            super();
            setURI(URI.create(uri));
        }
        public HttpDeleteWithBody(final URI uri) {
            super();
            setURI(uri);
        }
        public HttpDeleteWithBody() { super(); }
    }

    // 自定义 HttpDelete 方法，自带的HttpDelete方法不能设置参数。参考：http://www.tuicool.com/articles/BjmQ7fU
    public static String httpDeleteStringHandler(String url, String content) {
        String msg = null;
        try {
            LOG.info("post url is : {}, content is : {}", url, content);
            HttpDeleteWithBody deleteRequest = new HttpDeleteWithBody(url);
            deleteRequest.setHeader("Accept", "application/json");
            deleteRequest.setEntity(new StringEntity(content));
            HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = client.execute(deleteRequest);
            HttpEntity entity = response.getEntity();
            LOG.info(response.getStatusLine().toString());
            if (entity != null) {
                LOG.info("Response content length: " + entity.getContentLength());
            }
            // 显示结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(entity.getContent(), "UTF-8"));

            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {

                sb.append(line);
            }
            if (entity != null) {
                EntityUtils.consume(entity);
            }
            msg = sb.toString();
        } catch (Exception e) {
            LOG.error("postStringHandler error ", e);
            return msg;
        }
        return msg;
    }

    public static void main(String[] args) throws Exception {
        String url = "http://xdata.jcloud.com/zeppelin/api/note/job/2BXV6VE3Z/20160910-164457_1252700371";
        Map<String,String> ret = new HashMap<String,String>();
        ret.put("server","http://103.235.246.206:8888");
        ret.put("code","http://103.235.246.206:8888");
        String content = JSONUtils.toJSON(ret);
        String msg = httpDeleteStringHandler(url, content);
        LOG.info(msg);
    }

}
