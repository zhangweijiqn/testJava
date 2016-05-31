package zwj.test.redis;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * MemCached
 *
 <dependency>
 <groupId>com.whalin</groupId>
 <artifactId>Memcached-Java-Client</artifactId>
 <version>3.0.0</version>
 </dependency>

 */
public class MemCachedManager {

    private final static String CONFIGURATION_PATH = "memcached.properties";//memcached配置信息，需要配置memcached server
    private Log logger = LogFactory.getLog(getClass());

    // 创建全局的唯一实例
    private  MemCachedClient mcc = new MemCachedClient();

    private static MemCachedManager memCached = new MemCachedManager();

    protected MemCachedManager() {
        init();
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        InputStream is = Configuration.class.getClassLoader()
                .getResourceAsStream(CONFIGURATION_PATH);
        try {
            // 加载输入流
            properties.load(is);
            // 获得配置的各个属性
        } catch (IOException e) {
            logger.error("加载Memcached配置文件失败！", e);
        }

        return properties;
    }

    private void init() {
        Properties properties = loadProperties();

        // 设置与缓存服务器的连接池
        // 服务器列表和其权重
        String[] servers = {properties.getProperty("server")};
        Integer[] weights = {3};
        try {
            weights = new Integer[]{Integer.valueOf(properties.getProperty("weights"))};
        } catch (Exception e) {
            logger.error("初始化Memcached权重失败", e);
        }

        // 获取socke连接池的实例对象
        SockIOPool pool = SockIOPool.getInstance();

        // 设置服务器信息
        pool.setServers(servers);
        pool.setWeights(weights);

        // 设置初始连接数、最小和最大连接数以及最大处理时间
        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(250);
        try {
            pool.setMaxIdle(Long.valueOf(properties.getProperty("MaxIdle")));
        } catch (Exception e) {
            logger.error("初始化Memcached最大处理时间失败", e);
        }


        // 设置主线程的睡眠时间
        pool.setMaintSleep(30);

        // 设置TCP的参数，连接超时等
        pool.setNagle(false);
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);

        // 初始化连接池
        pool.initialize();

    }

    public static MemCachedManager getInstance() {
        return memCached;
    }

    public boolean add(String key, Object value) {
        return mcc.add(key, value);
    }

    public boolean add(String key, Object value, Date expiry) {
        return mcc.add(key, value, expiry);
    }

    public boolean set(String key, Object value) {
        return mcc.set(key, value);
    }

    public boolean set(String key, Object value, Date expiry) {
        return mcc.set(key, value, expiry);
    }

    public boolean replace(String key, Object value) {
        return mcc.replace(key, value);
    }

    public boolean replace(String key, Object value, Date expiry) {
        return mcc.replace(key, value, expiry);
    }

    public Object get(String key) {
        return mcc.get(key);
    }

    public boolean delete(String key) {
        return mcc.delete(key);
    }
}
