package zwj.web.Connection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
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
import java.nio.charset.StandardCharsets;
import java.util.List;

public class HttpUtils {

    private static final Logger LOG = LoggerFactory.getLogger(HttpUtils.class);

    public static String httpPostHandler(String url, List<BasicNameValuePair> nvps) {
        String msg = null;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            // obtain a http post request object
            HttpPost postRequest = new HttpPost(url);
            //提交的是form表单，参数在header中是 a=aaa&b=bbb的形式
            postRequest.setHeader("Content-Type", "application/x-www-form-urlencoded");
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
            //提交的参数在header中是json的形式
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

    public static void main(String[] args) {
        String url = String.format("%s/v2/ras/jmr/getJmrClusterList?userName=%s", "http://192.168.178.80:8083", "datajingdo_m");
        String result =  httpGetHandler(url);
        System.out.println(result);
    }

}
