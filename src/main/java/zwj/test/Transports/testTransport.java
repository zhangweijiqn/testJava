package zwj.test.Transports;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zwj.test.Json.JSONUtils;
import zwj.test.Transports.beans.ProjectListObject;
import zwj.test.Transports.rest.ResourceBuilder;
import zwj.test.Transports.rest.RestClient;
import zwj.test.Transports.transport.DefaultTransport;
import zwj.test.Transports.transport.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

/**
 * test request and response
 * Created by zhangweijian on 2015/11/30.
 */
public class testTransport {
    public static final String EndPoint= "http://bds.jcloud.com";
    private static Logger logger = LoggerFactory.getLogger(testTransport.class);

    public static void loadProjects(Account account,String projectId,String projectName) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", account.getUserName());
        params.put("projectId",projectId);
        params.put("projectName",projectName);
        String resource= ResourceBuilder.loadProjectResource();//  /loadProjects
        Map<String, String> headers=null;
        byte[] body = new byte[256];
        String method="GET";

        RestClient client = new RestClient(new DefaultTransport());
        client.setEndpoint(EndPoint);
        //headers, body Ϊ�յ����
        Response resp = client.request(resource, method, params, headers, body);
        String resp_body = new String(resp.getBody(),"UTF-8");//���ֽ���ת��Ϊutf8�����ַ���
        Map<String,Object>rets = JSONUtils.parseJSON2Map(resp_body);
//        for (Map.Entry<String, Object> entry : rets.entrySet()) {
//            System.out.println("key: " + entry.getKey() + " and value: " + entry.getValue());
//        }
        if(rets.get("success").equals(true)){
            logger.info("msg:" + rets.get("msg"));
            //��dataת��Ϊlist���󣬷ֱ����
            String projects_str = JSONUtils.toJSON(rets.get("data"));
            JSONArray jsonArray = JSONArray.fromObject(projects_str);
            List<ProjectListObject> projects = (List<ProjectListObject>)JSONArray.toCollection(jsonArray, ProjectListObject.class);
            for(ProjectListObject project:projects){
                logger.info(project.getName());
            }
        }
        else{
            logger.error("error:", rets.get("msg"));
        }
//        for (String key : rets.keySet()) {
//            logger.info("key= "+ key + " and value= " + rets.get(key));
//        }
    }

    public static void createProjects(Account account,String projectName,String desc) throws Exception {
        Map<String, String> params = new HashMap<String, String>();
        params.put("userName", account.getUserName());
        params.put("name",projectName);
        params.put("desc", desc);
        String resource= ResourceBuilder.createProjectResource();//  /loadProjects
        Map<String, String> headers=null;
        byte[] body = new byte[256];
        String method="GET";

        RestClient client = new RestClient(new DefaultTransport());
        client.setEndpoint(EndPoint);
        //headers, body both null
        Response resp = client.request(resource, method, params, headers, body);
        String resp_body = new String(resp.getBody(),"UTF-8");//���ֽ���ת��Ϊutf8�����ַ���
        Map<String,Object>rets = JSONUtils.parseJSON2Map(resp_body);
//        for (Map.Entry<String, Object> entry : rets.entrySet()) {
//            logger.info("key: " + entry.getKey() + " and value: " + entry.getValue());
//        }
        if(rets.get("success").equals(true)) {
            logger.info("msg={}",rets.get("msg"));
        }else{
            logger.info("error={}", rets.get("msg"));
        }
    }

    public static void removeProject(Account account){

    }

    public static void main(String[] args) throws Exception {

        Account account = new Account();
        account.setUserName("datajingdo_m");
        try {
            //load projects
            loadProjects(account,null,null);
            //create projects
            String projectName="project_test001";
            String desc="test_project";
            createProjects(account,projectName,desc);
            //removeProjects

        }catch (Exception e){
            logger.error("error", e);
        }
    }
}
