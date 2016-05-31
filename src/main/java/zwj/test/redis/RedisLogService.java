package zwj.test.redis;

import com.google.gson.Gson;
import com.jd.data.redis.ConnectionFactoryBuilder;
import com.jd.data.redis.RedisUtils;
import com.jd.data.redis.connection.RedisAccessException;
import org.apache.log4j.Logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 安装好redis后执行 ./src/redis-server & 即可启动
 */
public class RedisLogService {

    private static final Logger logger = Logger.getLogger(RedisLogService.class);
    private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS ");
    private static final int EXPIRE_TIME = 7200;

    private RedisUtils redisUtils;

    public RedisLogService() {
        ConnectionFactoryBuilder connectionFactoryBuilder = new ConnectionFactoryBuilder();
        connectionFactoryBuilder.setMaxActive(1000);
        connectionFactoryBuilder.setMaxIdle(8);
        connectionFactoryBuilder.setMaxWait(1000);
        connectionFactoryBuilder.setMasterConfString("127.0.0.1:6379");
        redisUtils = new RedisUtils(connectionFactoryBuilder);
    }

    /*    <!-- Redis -->
    <bean id="redisUtils" class="com.jd.data.spring.RedisClientFactoryBean">
        <!-- 单个应用中的链接池最大链接数-->
        <property name="maxActive" value="1000"/>
        <!-- 单个应用中的链接池最大空闲数-->
        <property name="maxIdle" value="8"/>
        <!-- 单个应用中的链接池取链接时最大等待时间，单位：ms-->
        <property name="maxWait" value="1000"/>
        <!-- 设置在每一次取对象时测试ping-->
        <property name="testOnBorrow" value="false"/>
        <!-- 设置redis connect request response timeout 单位:ms-->
        <property name="timeout" value="2000"/>
        <!-- master redis server 设置 -->
        <!-- host:port:password[可选,password中不要有":"],redis server顺序信息一定不要乱，请按照分配顺序填写，乱了就可能会出现一致性hash不同，造成不命中cache情况-->
        <property name="masterConfString" value="172.18.149.134:6379"/>
    </bean>*/


    public void setSQLResultSet(String key, String sqlResultSet)
    {
        try
        {
            Gson gson = new Gson();
            String value = gson.toJson(sqlResultSet);

            this.redisUtils.rpush(key,value);
            this.redisUtils.expire(key, EXPIRE_TIME);
        } catch (RedisAccessException e) {
            logger.error("time:" + dateFormat.format(new Date()) + "redis rPush error", e);
        }
    }


    public void set(String key, String value)
    {
        try
        {
            this.redisUtils.set(key, value);
            this.redisUtils.expire(key, EXPIRE_TIME);
        } catch (RedisAccessException e) {
            logger.error("time:" + dateFormat.format(new Date()) + "redis rPush error", e);
        }
    }

    public String get(String key)
    {
        String jsonString=null;
        try {
            jsonString = this.redisUtils.get(key);
        }
        catch (RedisAccessException e) {
            logger.error("time:" + dateFormat.format(new Date()) + "redis lpop error", e);
        }
        return jsonString;
    }

    public Long remove(String key)
    {
        Long result=null;
        try {
            result = this.redisUtils.del(key);
        }
        catch (RedisAccessException e) {
            logger.error("time:" + dateFormat.format(new Date()) + "redis lpop error", e);
        }
        return result;
    }


    public void rpush(String key, String value)
    {
        try
        {
            this.redisUtils.rpush(key,value);
            this.redisUtils.expire(key, EXPIRE_TIME);
        } catch (RedisAccessException e) {
            logger.error("time:" + dateFormat.format(new Date()) + "redis rPush error", e);
        }
    }

    public List<String> lpop(String key)
    {
        List<String> projectInfoList = new ArrayList<String>();
        try {
            while (true) {
                String jsonString = this.redisUtils.lpop(key);
                if (jsonString == null)
                {
                    break;
                }
                projectInfoList.add(jsonString);
            }
        }
        catch (RedisAccessException e) {
            logger.error("time:" + dateFormat.format(new Date()) + "redis lpop error", e);
        }
        return projectInfoList;
    }


    public List<String> getSQLResultSetList(String key)
    {
        List sQLResultSetList = new ArrayList();
        try {
            String sQLResultSet = null;
            while (true) {
                String jsonString = this.redisUtils.lpop(key);
                if (jsonString == null)
                {
                    break;
                }
                Gson gson = new Gson();
                sQLResultSet = gson.fromJson(jsonString, String.class);
                sQLResultSetList.add(sQLResultSet);
            }
        }
        catch (RedisAccessException e) {
            logger.error("time:" + dateFormat.format(new Date()) + "redis lpop error", e);
        }
        return sQLResultSetList;
    }

}
